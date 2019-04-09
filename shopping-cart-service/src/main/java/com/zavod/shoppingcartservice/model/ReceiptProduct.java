package com.zavod.shoppingcartservice.model;

import com.zavod.shoppingcartservice.entity.DiscountRule;

import java.math.BigDecimal;
import java.util.*;

public class ReceiptProduct {

    private String name;
    private int cty;
    private BigDecimal priceForOneProduct;
    private BigDecimal priceForAllProducts;

    private Map<DiscountRule, ReceiptProductDiscount> discountMap;

    public ReceiptProduct(ReceiptProduct product){
        this.name = product.name;
        this.cty = product.cty;
        this.priceForOneProduct = product.priceForOneProduct;
        this.priceForAllProducts = product.priceForAllProducts;
        this.discountMap = product.discountMap;
    }

    public ReceiptProduct(String name, int cty, BigDecimal pricePerItem){
        this.name = name;
        this.cty = cty;
        this.priceForOneProduct = pricePerItem;
        this.priceForAllProducts = pricePerItem.multiply(new BigDecimal(cty));
        this.discountMap = new HashMap<>();
    }

    public ReceiptProduct(String name, int cty, BigDecimal pricePerItem, Set<DiscountRule> discountRules){
        this.name = name;
        this.cty = cty;
        this.priceForOneProduct = pricePerItem;
        this.priceForAllProducts = pricePerItem.multiply(new BigDecimal(cty));
        this.discountMap = new HashMap<>();

        for (DiscountRule discountRule : discountRules){
            ReceiptProductDiscount receiptProductDiscount = new ReceiptProductDiscount(discountRule.getDescription());
            receiptProductDiscount.setDiscountedAmount(getDiscountedAmount(discountRule));
            this.discountMap.put(discountRule, receiptProductDiscount);
        }
    }

    public String getName() {
        return name;
    }

    public int getCty() {
        return cty;
    }

    public BigDecimal getPriceForOneProduct() {
        return priceForOneProduct;
    }

    public BigDecimal getPriceForAllProducts() {
        return priceForAllProducts;
    }

    public List<ReceiptProductDiscount> getDiscounts() {
        return new ArrayList<>(discountMap.values());
    }

    public BigDecimal getTotalDiscount() {
        BigDecimal discount = BigDecimal.ZERO;
        for (ReceiptProductDiscount receiptProductDiscount : getDiscounts()){
            discount = discount.add(receiptProductDiscount.getDiscountedAmount());
        }
        return discount;
    }

    public void incrementCtyBy(int newItems){
        this.cty += newItems;
        BigDecimal newItemsPrice = this.priceForOneProduct.multiply(new BigDecimal(newItems));
        this.priceForAllProducts = this.priceForAllProducts.add(newItemsPrice);

        for (Map.Entry<DiscountRule, ReceiptProductDiscount> entry : this.discountMap.entrySet()){
            entry.getValue().setDiscountedAmount(getDiscountedAmount(entry.getKey()));
        }
    }

    @Override
    public String toString() {
        return "ReceiptProduct{" +
                "name='" + name + '\'' + ", " +
                "cty=" + cty + ", " +
                "priceForOneProduct=" + priceForOneProduct + ", " +
                "priceForAllProducts=" + priceForAllProducts + ", " +
                "discounts=" + getDiscounts() + ", " +
                "totalDiscount=" + getTotalDiscount() + '}';
    }

    private BigDecimal getDiscountedAmount(DiscountRule discountRule){
        int multiplyFactor =  this.cty / discountRule.getNumberOfItems();

        BigDecimal discountedAmountPerOneRuleApply = this.priceForOneProduct.multiply(
                new BigDecimal((double)discountRule.getDiscountPercentage()/100.00));

        return discountedAmountPerOneRuleApply.multiply(new BigDecimal(multiplyFactor));
    }
}
