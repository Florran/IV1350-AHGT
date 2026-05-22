package se.kth.iv1350.electricBike.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the repair order printout, that is
 * Printer.printRepairOrder.
 */
public class PrinterTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;
    private RepairOrderDTO order;

    @BeforeEach
    public void setUp() {
        RepairTaskDTO task = new RepairTaskDTO("Byt motorkabel", 450.0, false);
        order = new RepairOrderDTO(
                "A1B2C",
                "Accepted",
                "Motor stängs av i uppförsbacke",
                "0705556767",
                "0001",
                "2025-05-22T10:30",
                "2025-05-29T10:30",
                450.0,
                50.0,
                List.of("Kabelglapp vid motorns anslutning"),
                List.of(task));

        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testPrintsOrderIdentity() {
        new Printer().printRepairOrder(order);

        String output = captured.toString();
        assertTrue(output.contains("REPAIR ORDER:"),
                "Kvittot ska innehålla rubriken för reparationsordern.");
        assertTrue(output.contains("A1B2C"),
                "Kvittot ska innehålla reparationsorderns id.");
    }

    @Test
    public void testPrintsTaskAndProblem() {
        new Printer().printRepairOrder(order);

        String output = captured.toString();
        assertTrue(output.contains("Motor stängs av i uppförsbacke"),
                "Kvittot ska innehålla felbeskrivningen.");
        assertTrue(output.contains("Byt motorkabel"),
                "Kvittot ska innehålla reparationsåtgärderna.");
    }

    @Test
    public void testPrintsCostsWithDiscount() {
        new Printer().printRepairOrder(order);

        String output = captured.toString();
        assertTrue(output.contains("Discount applied:"),
                "Kvittot ska visa den tillämpade rabatten.");
        assertTrue(output.contains("Total to pay: 400.0 kr"),
                "Kvittot ska visa summan att betala efter rabatt.");
    }

    @Test
    public void testPrintsTotalWithoutDiscountOmitsDiscountLine() {
        RepairTaskDTO task = new RepairTaskDTO("Byt motorkabel", 450.0, false);
        RepairOrderDTO orderWithoutDiscount = new RepairOrderDTO(
                "A1B2C",
                "Accepted",
                "Motor stängs av i uppförsbacke",
                "0705556767",
                "0001",
                "2025-05-22T10:30",
                "2025-05-29T10:30",
                450.0,
                0.0,
                List.of("Kabelglapp vid motorns anslutning"),
                List.of(task));

        new Printer().printRepairOrder(orderWithoutDiscount);

        String output = captured.toString();
        assertTrue(output.contains("Total to pay: 450.0 kr"),
                "Utan rabatt ska summan att betala vara hela totalkostnaden.");
        assertFalse(output.contains("Discount applied:"),
                "Utan rabatt ska ingen rabattrad skrivas ut.");
    }

    @Test
    public void testPrintsNoneForEmptyDiagnosticsAndTasks() {
        RepairOrderDTO emptyOrder = new RepairOrderDTO(
                "A1B2C",
                "Newly created",
                "Motor stängs av i uppförsbacke",
                "0705556767",
                "0001",
                "2025-05-22T10:30",
                "2025-05-29T10:30",
                0.0,
                0.0,
                List.of(),
                List.of());

        new Printer().printRepairOrder(emptyOrder);

        String output = captured.toString();
        assertTrue(output.contains("Diagnostic results: (none)"),
                "Utan diagnostiska fynd ska kvittot visa (none).");
        assertTrue(output.contains("Repair tasks: (none)"),
                "Utan reparationsåtgärder ska kvittot visa (none).");
    }
}
