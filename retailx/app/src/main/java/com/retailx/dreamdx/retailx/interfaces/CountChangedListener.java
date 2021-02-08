package com.retailx.dreamdx.retailx.interfaces;

public interface CountChangedListener {

    void notifyCountDecreased(double count);
    void notifyCountIncreased(double count);
    void notifyCountChanged(double count);
    void notifyEditBtnClicked();
    void priceChanged(double price);
    void stockChanged(double stock);
}
