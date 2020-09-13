package io.github.akadir.casestudy.delivery.strategy;

import io.github.akadir.casestudy.delivery.Deliverable;

public class FedexDelivery implements DeliveryStrategy {
    private final double costPerProduct;
    private final double costPerDelivery;

    public FedexDelivery(double costPerProduct, double costPerDelivery) {
        this.costPerProduct = costPerProduct;
        this.costPerDelivery = costPerDelivery;
    }

    @Override
    public double calculateDeliveryCost(Deliverable deliverable) {
        if (deliverable.getDeliveryCount() == 0) {
            return 0;
        } else {
            return costPerDelivery * deliverable.getDeliveryCount() + costPerProduct * deliverable.getProductCount();
        }
    }
}
