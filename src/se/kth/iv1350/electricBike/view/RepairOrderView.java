package se.kth.iv1350.electricBike.view;

import se.kth.iv1350.electricBike.integration.RepairOrderDTO;

/**
 * An observer that prints updated repair orders to <code>System.out</code>,
 * so technicians and receptionists can see changes as they happen.
 */
public class RepairOrderView extends RepairOrderObserverTemplate {
    @Override
    protected void doHandleRepairOrderUpdate(RepairOrderDTO order) {
        System.out.println(order);
    }

    @Override
    protected void handleErrors(Exception e) {
        System.err.println("Could not display repair order update: " + e.getMessage());
    }
}
