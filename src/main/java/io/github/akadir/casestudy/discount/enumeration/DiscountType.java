package io.github.akadir.casestudy.discount.enumeration;

public enum DiscountType {
    RATE {
        @Override
        public double calculateDiscount(double price, double discountRate) {
            if (isApplicable(price, discountRate)) {
                return price * (discountRate * 0.01);
            } else {
                return 0;
            }
        }

        @Override
        public boolean isApplicable(double price, double discountRate) {
            return discountRate > 0 && discountRate < 100;
        }
    }, AMOUNT {
        @Override
        public double calculateDiscount(double price, double discountAmount) {
            if (isApplicable(price, discountAmount)) {
                return discountAmount;
            } else {
                return 0;
            }
        }

        @Override
        public boolean isApplicable(double price, double discount) {
            return price >= discount && discount > 0;
        }
    };

    public abstract double calculateDiscount(double price, double discount);

    public abstract boolean isApplicable(double price, double discount);
}
