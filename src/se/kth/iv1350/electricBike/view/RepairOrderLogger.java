package se.kth.iv1350.electricBike.view;

import java.io.IOException;
import se.kth.iv1350.electricBike.integration.RepairOrderDTO;
import se.kth.iv1350.electricBike.util.FileLogger;
import se.kth.iv1350.electricBike.util.LogHandler;

/**
 * An observer that writes updated repair orders to a log file, so
 * technicians and receptionists have a persistent record of changes.
 * The actual file handling is delegated to a {@link FileLogger}.
 */
public class RepairOrderLogger extends RepairOrderObserverTemplate {
    private static final String LOG_FILE_NAME = "repair-order-log.txt";
    private final FileLogger fileLogger;
    private final LogHandler logger;

    /**
     * Creates a new instance and opens the log file for writing. An
     * existing log file with the same name is overwritten.
     *
     * @param logger The log handler used to record errors that occur while
     *               writing a repair order update to the log file.
     * @throws IOException if the log file cannot be opened.
     */
    public RepairOrderLogger(LogHandler logger) throws IOException {
        this.fileLogger = new FileLogger(LOG_FILE_NAME);
        this.logger = logger;
    }

    @Override
    protected void doHandleRepairOrderUpdate(RepairOrderDTO order) {
        fileLogger.log(order.toString());
    }

    @Override
    protected void handleErrors(Exception e) {
        logger.logException(e);
    }
}
