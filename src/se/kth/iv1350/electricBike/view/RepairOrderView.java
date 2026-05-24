package se.kth.iv1350.electricBike.view;

import se.kth.iv1350.electricBike.integration.RepairOrderDTO;
import se.kth.iv1350.electricBike.util.LogHandler;

/**
 * An observer that prints updated repair orders to <code>System.out</code>,
 * so technicians and receptionists can see changes as they happen.
 */
public class RepairOrderView extends RepairOrderObserverTemplate {
    private final LogHandler logger;

    /**
     * Creates a new instance.
     *
     * @param logger The log handler used to record errors that occur while
     *               printing a repair order update.
     */
    public RepairOrderView(LogHandler logger) {
        this.logger = logger;
    }

    @Override
    protected void doHandleRepairOrderUpdate(RepairOrderDTO order) {
        System.out.println(order);
    }

    @Override
    protected void handleErrors(Exception e) {
        logger.logException(e);
    }
}
