package com.zavod.shoppingcartservice.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptDetails {

    private Map<String, ReceiptProduct> productsMap;
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal total;

    public ReceiptDetails(){
        this.productsMap = new HashMap<>();
        this.subTotal = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.totalDiscount = BigDecimal.ZERO;
    }

    public void addProduct(ReceiptProduct product){
        addProductToProductsList(product);
        recomputeSubTotal();
        recomputeTotalDiscount();
        recomputeTotal();
    }

    public List<ReceiptProduct> getProducts() {
        return new ArrayList<>(productsMap.values());
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    @Override
    public String toString() {
        return "ReceiptDetails{" +
                "products=" + getProducts() + ", " +
                "subTotal=" + subTotal + ", " +
                "totalDiscount=" + totalDiscount + ", " +
                "total=" + total + '}';
    }

    private void addProductToProductsList(ReceiptProduct product){
        ReceiptProduct receiptProduct;
        if (productsMap.containsKey(product.getName())){
            receiptProduct = productsMap.get(product.getName());
            receiptProduct.incrementCtyBy(product.getCty());
        } else {
            receiptProduct = new ReceiptProduct(product);
        }
        productsMap.put(product.getName(), receiptProduct);
    }

    private void recomputeSubTotal(){
        this.subTotal = BigDecimal.ZERO;
        for(ReceiptProduct product : productsMap.values()){
            this.subTotal = this.subTotal.add(product.getPriceForAllProducts());
        }
    }

    private void recomputeTotalDiscount(){
        this.totalDiscount = BigDecimal.ZERO;
        for(ReceiptProduct product : productsMap.values()){
            this.totalDiscount = this.totalDiscount.add(product.getTotalDiscount());
        }
    }

    private void recomputeTotal(){
        this.total = BigDecimal.ZERO;
        for(ReceiptProduct product : productsMap.values()){
            this.total = this.total.add(product.getPriceForAllProducts());
        }
        this.total = this.total.subtract(this.totalDiscount);
    }

}
