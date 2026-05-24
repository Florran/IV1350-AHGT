package se.kth.iv1350.electricBike.view;

import se.kth.iv1350.electricBike.integration.RepairOrderDTO;
import se.kth.iv1350.electricBike.model.RepairOrderObserver;

/**
 * Template class for observers that get notified about repair order updates.
 * Subclasses implement {@link #doHandleRepairOrderUpdate(RepairOrderDTO)} with
 * the actual handling, and {@link #handleErrors(Exception)} with what to do
 * if the handling fails.
 */
public abstract class RepairOrderObserverTemplate implements RepairOrderObserver {

    @Override
    public final void repairOrderUpdated(RepairOrderDTO order) {
        handleRepairOrderUpdate(order);
    }

    private void handleRepairOrderUpdate(RepairOrderDTO order) {
        try {
            doHandleRepairOrderUpdate(order);
        } catch (Exception e) {
            handleErrors(e);
        }
    }

    /**
     * Performs the actual handling of the repair order update.
     *
     * @param order A snapshot of the repair order after the update.
     * @throws Exception if the update cannot be handled.
     */
    protected abstract void doHandleRepairOrderUpdate(RepairOrderDTO order) throws Exception;

    /**
     * Called when {@link #doHandleRepairOrderUpdate} throws.
     *
     * @param e The exception that was thrown.
     */
    protected abstract void handleErrors(Exception e);
}
