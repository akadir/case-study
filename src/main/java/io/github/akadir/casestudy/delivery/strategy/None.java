package io.github.akadir.casestudy.delivery.strategy;

import io.github.akadir.casestudy.delivery.Deliverable;

public class None implements DeliveryStrategy {
    @Override
    public double calculateDeliveryCost(Deliverable deliverable) {
        return 0;
    }
}
