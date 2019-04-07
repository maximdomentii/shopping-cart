package com.zavod.shoppingcartservice.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptDetails {

    private Map<String, ReceiptProduct> productsMap;
    private BigDecimal subTotal;
    private BigDecimal total;

    public ReceiptDetails(){
        this.productsMap = new HashMap<>();
        this.subTotal = new BigDecimal(0);
        this.total = new BigDecimal(0);
    }

    public void addProduct(ReceiptProduct product){
        addProductToProductsList(product);
        recomputeSubTotal();
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

    @Override
    public String toString() {
        return "ReceiptDetails{" +
                "productsMap=" + productsMap +
                ", subTotal=" + subTotal +
                ", total=" + total +
                '}';
    }

    private void addProductToProductsList(ReceiptProduct product){
        ReceiptProduct receiptProduct;
        if (productsMap.containsKey(product.getName())){
            receiptProduct = productsMap.get(product.getName());
            receiptProduct.incrementCty();
        } else {
            receiptProduct = new ReceiptProduct(product);
        }
        productsMap.put(product.getName(), receiptProduct);
    }

    private void recomputeSubTotal(){
        this.subTotal = new BigDecimal(0);
        for(ReceiptProduct product : productsMap.values()){
            this.subTotal = this.subTotal.add(product.getPriceForAllProducts());
        }
    }

    private void recomputeTotal(){
        this.total = new BigDecimal(0);
        for(ReceiptProduct product : productsMap.values()){
            this.total = this.total.add(product.getPriceForAllProducts());
        }
    }
}
