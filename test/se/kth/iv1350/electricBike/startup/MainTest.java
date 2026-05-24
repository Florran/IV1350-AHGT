package se.kth.iv1350.electricBike.startup;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.electricBike.integration.RepairOrderRegistry;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the printouts produced by the main method. Main runs
 * the entire built-in program flow, so these tests verify that the run
 * prints the expected information to System.out.
 */
public class MainTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;

    @BeforeEach
    public void setUp() {
        RepairOrderRegistry.getInstance().clearAll();
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        RepairOrderRegistry.getInstance().clearAll();
    }

    @Test
    public void testMainPrintsSampleRun() {
        Main.main(new String[0]);

        assertFalse(captured.toString().isEmpty(),
                "Main ska skriva ut programflödet till konsolen.");
    }

    @Test
    public void testMainPrintsFoundCustomerInformation() {
        Main.main(new String[0]);

        assertTrue(captured.toString().contains("Customer1"),
                "Main ska driva flödet så att den hittade kundens information skrivs ut.");
    }

    @Test
    public void testMainPrintsReceipt() {
        Main.main(new String[0]);

        String output = captured.toString();
        assertTrue(output.contains("REPAIR ORDER:"),
                "Main ska driva flödet så att reparationsorderns kvitto skrivs ut.");
        assertTrue(output.contains("Total to pay:"),
                "Kvittot som skrivs ut ska innehålla summan att betala.");
    }

    @Test
    public void testMainDoesNotPrintStartupFailure() {
        Main.main(new String[0]);

        assertFalse(captured.toString().contains("Could not start application"),
                "Vid en lyckad körning ska startfelmeddelandet inte skrivas ut.");
    }
}
