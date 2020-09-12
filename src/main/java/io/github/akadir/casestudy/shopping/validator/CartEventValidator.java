package io.github.akadir.casestudy.shopping.validator;

import io.github.akadir.casestudy.product.Product;

public abstract class CartEventValidator {
    private CartEventValidator next;

    /**
     * Builds chains of middleware objects.
     */
    public CartEventValidator linkWith(CartEventValidator next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(Product product, int amount);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    protected boolean checkNext(Product product, int amount) {
        if (next == null) {
            return true;
        }

        return next.check(product, amount);
    }
}
