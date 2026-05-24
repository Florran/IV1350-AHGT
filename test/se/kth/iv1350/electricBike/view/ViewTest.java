package se.kth.iv1350.electricBike.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.electricBike.controller.Controller;
import se.kth.iv1350.electricBike.integration.CustomerRegistry;
import se.kth.iv1350.electricBike.integration.Printer;
import se.kth.iv1350.electricBike.integration.RepairOrderRegistry;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the printouts in View. Only printouts that contain
 * information are tested. Printouts that exist only to make the view more
 * readable are not tested.
 */
public class ViewTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;
    private View view;

    @BeforeEach
    public void setUp() throws IOException {
        RepairOrderRegistry.getInstance().clearAll();
        CustomerRegistry customerReg = new CustomerRegistry();
        Printer printer = new Printer();
        Controller contr = new Controller(customerReg, printer);
        view = new View(contr);

        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        RepairOrderRegistry.getInstance().clearAll();
        if (view != null) {
            view.close();
            view = null;
        }
    }

    @Test
    public void testPrintsCustomerLookup() {
        view.fakeExecution();

        assertTrue(captured.toString().contains("0705556767"),
                "View ska skriva ut vilket telefonnummer kunden söks med.");
    }

    @Test
    public void testPrintsFoundCustomerDetails() {
        view.fakeExecution();

        String output = captured.toString();
        assertTrue(output.contains("Kund hittades:"),
                "View ska tala om att en kund hittades.");
        assertTrue(output.contains("Customer1"),
                "View ska skriva ut den hittade kundens uppgifter.");
    }

    @Test
    public void testPrintsRegisteredProblem() {
        view.fakeExecution();

        String output = captured.toString();
        assertTrue(output.contains("Motor stängs av i uppförsbacke"),
                "View ska skriva ut det registrerade felet.");
        assertTrue(output.contains("Systemet har skapat reparationsorder."),
                "View ska bekräfta att en reparationsorder skapats.");
    }

    @Test
    public void testPrintsAppliedDiscount() {
        view.fakeExecution();

        assertTrue(captured.toString().contains("Applicerar rabatt (LoyaltyDiscount)"),
                "View ska skriva ut vilken rabatt som tillämpas.");
    }

    @Test
    public void testPrintsFinalPriceAfterDiscount() {
        view.fakeExecution();

        assertTrue(captured.toString().contains("Totalkostnad efter rabatt:"),
                "View ska skriva ut totalkostnaden efter rabatt.");
    }

    @Test
    public void testPrintsErrorMessageOnSimulatedDatabaseFailure() {
        view.fakeExecution();

        assertTrue(captured.toString().contains("Operationen kunde inte slutföras"),
                "View ska visa ett felmeddelande för användaren vid det simulerade databasfelet.");
    }
}
