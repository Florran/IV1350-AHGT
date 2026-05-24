package se.kth.iv1350.electricBike.view;

import java.io.IOException;
import se.kth.iv1350.electricBike.controller.Controller;
import se.kth.iv1350.electricBike.integration.CustomerDTO;
import se.kth.iv1350.electricBike.integration.CustomerNotFoundException;
import se.kth.iv1350.electricBike.integration.DatabaseFailureException;
import se.kth.iv1350.electricBike.integration.RepairOrderRegistry;
import se.kth.iv1350.electricBike.model.discount.DiscountStrategy;
import se.kth.iv1350.electricBike.model.discount.LoyaltyDiscount;
import se.kth.iv1350.electricBike.util.LogHandler;

/**
 * Represents the view layer used to run the application flow.
 */
public class View {

    private Controller contr;
    private ErrorMessageHandler errorMsgHandler;
    private LogHandler logger;

    /**
     * The view constructor.
     *
     * @param contr Controller reference so view can make calls to controller.
     */
    public View(Controller contr) throws IOException {
        this.contr = contr;
        this.errorMsgHandler = new ErrorMessageHandler();
        this.logger = new LogHandler();
        contr.addRepairOrderObserver(new RepairOrderView());
        contr.addRepairOrderObserver(new RepairOrderLogger());
    }

    /**
     * Runs a hardcoded execution of the basic repair order flow.
     */
    public void fakeExecution() {
        System.out.println("--- Sample run: Repair Electric Bike, basic flow ---\n");

        String customerPhone = "0705556767";
        String problemDescription = "Motor stängs av i uppförsbacke";

        System.out.println("--- Kunden lämnar in cykeln ---");
        System.out.println("Receptionist letar upp kund med telefonnummer " + customerPhone + "...");

        CustomerDTO foundCustomer;
        try {
            foundCustomer = contr.findCustomer(customerPhone);
        } catch (CustomerNotFoundException exc) {
            errorMsgHandler.showErrorMsg("Ingen kund hittades med telefonnummer "
                    + exc.getNumber() + ". Kontrollera numret och försök igen!");
            return;
        }

        System.out.println("Kund hittades:");
        System.out.println(foundCustomer);

        String bikeSerial = foundCustomer.getBikeSerialNo();
        System.out.println("\nReceptionist skriver in felet för cykel " + bikeSerial + ": " + problemDescription);
        contr.createRepairOrder(problemDescription, customerPhone, bikeSerial);
        System.out.println("Systemet har skapat reparationsorder.");

        String orderId = contr.findRepairOrderByNumber(customerPhone).getId();

        System.out.println("\n--- Tekniker börjar arbeta ---");
        System.out.println("Tekniker inspekterar cykeln och registrerar diagnostiska fynd...");
        contr.addDiagnosticResult(orderId, "Kabelglapp vid motorns anslutning");
        contr.addDiagnosticResult(orderId, "Slitet batterifäste");

        System.out.println("\nTekniker föreslår reparationer...");
        contr.addRepairTask(orderId, "Byt ut och löd om motorkabel", 450.0);
        contr.addRepairTask(orderId, "Montera nytt batterifäste", 200.0);

        System.out.println("\n--- Kunden kommer tillbaka för att hämta cykeln ---");
        System.out.println("Receptionist har redan informerats om uppdaterad order via RepairOrderView.");

        System.out.println("\nApplicerar rabatt (LoyaltyDiscount)...");
        DiscountStrategy loyaltyDiscount = new LoyaltyDiscount();
        double finalPrice = contr.calculateTotalCost(orderId, loyaltyDiscount);
        System.out.println("Totalkostnad efter rabatt: " + finalPrice + " kr");

        System.out.println("\nKunden accepterar reparationen.");
        System.out.println("\n--- Skrivare skriver ut kvitto ---");
        contr.acceptRepairOrder(orderId, loyaltyDiscount);

        System.out.println("\n--- Demonstration av felhantering (simulerat databasfel) ---");
        try {
            contr.findRepairOrderById(RepairOrderRegistry.DB_FAILURE_TRIGGER_ID);
        } catch (DatabaseFailureException exc) {
            errorMsgHandler.showErrorMsg("Operationen kunde inte slutföras. Försök igen senare.");
            logger.logException(exc);
        }
    }
}