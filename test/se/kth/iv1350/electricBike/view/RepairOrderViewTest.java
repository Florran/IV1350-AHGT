package se.kth.iv1350.electricBike.view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.electricBike.integration.RepairOrderDTO;
import se.kth.iv1350.electricBike.integration.RepairTaskDTO;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the RepairOrderView observer, which prints updated
 * repair orders to System.out.
 */
public class RepairOrderViewTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;
    private RepairOrderDTO order;

    @BeforeEach
    public void setUp() {
        RepairTaskDTO task = new RepairTaskDTO("Byt motorkabel", 450.0, false);
        order = new RepairOrderDTO(
                "A1B2C",
                "Newly created",
                "Motor stängs av i uppförsbacke",
                "0705556767",
                "0001",
                "2025-05-22T10:30",
                "2025-05-29T10:30",
                450.0,
                0.0,
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
    public void testPrintsUpdatedOrder() {
        new RepairOrderView().repairOrderUpdated(order);

        String output = captured.toString();
        assertTrue(output.contains("A1B2C"),
                "Observern ska skriva ut den uppdaterade orderns id.");
        assertTrue(output.contains("Newly created"),
                "Observern ska skriva ut orderns status.");
    }

    @Test
    public void testPrintsOrderContents() {
        new RepairOrderView().repairOrderUpdated(order);

        String output = captured.toString();
        assertTrue(output.contains("Kabelglapp vid motorns anslutning"),
                "Observern ska skriva ut de diagnostiska fynden.");
        assertTrue(output.contains("Byt motorkabel"),
                "Observern ska skriva ut reparationsåtgärderna.");
    }
}
