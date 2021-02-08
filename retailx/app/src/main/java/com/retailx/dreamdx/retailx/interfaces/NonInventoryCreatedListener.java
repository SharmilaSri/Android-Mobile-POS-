package com.retailx.dreamdx.retailx.interfaces;

import com.retailx.dreamdx.retailx.POJO.Product;

public interface NonInventoryCreatedListener {

    void itemAddedToCart(Product p);
    void itemCreated(Product p);
}
