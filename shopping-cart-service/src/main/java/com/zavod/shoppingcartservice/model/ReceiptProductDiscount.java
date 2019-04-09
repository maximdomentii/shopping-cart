package com.zavod.shoppingcartservice.model;

import java.math.BigDecimal;

public class ReceiptProductDiscount {

    private String discountRuleName;
    private BigDecimal discountedAmount;

    public ReceiptProductDiscount(String discountRuleName){
        this.discountRuleName = discountRuleName;
        this.discountedAmount = BigDecimal.ZERO;
    }

    public String getDiscountRuleName() {
        return discountRuleName;
    }

    public BigDecimal getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(BigDecimal discountedAmount) {
        this.discountedAmount = discountedAmount;
    }

    @Override
    public String toString() {
        return "ReceiptProductDiscount{" +
                "discountRuleName='" + discountRuleName + '\'' + ", " +
                "discountedAmount=" + discountedAmount + '}';
    }
}
