package io.github.akadir.casestudy.delivery;

import io.github.akadir.casestudy.delivery.strategy.DeliveryStrategy;

public interface Deliverable {
    int getProductCount();

    int getDeliveryCount();

    DeliveryStrategy getDeliveryStrategy();

    void setDeliveryStrategy(DeliveryStrategy deliveryStrategy);
}
