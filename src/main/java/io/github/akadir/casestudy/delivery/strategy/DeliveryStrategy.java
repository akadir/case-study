package io.github.akadir.casestudy.delivery.strategy;

import io.github.akadir.casestudy.delivery.Deliverable;

public interface DeliveryStrategy {
    double calculateDeliveryCost(Deliverable deliverable);
}
