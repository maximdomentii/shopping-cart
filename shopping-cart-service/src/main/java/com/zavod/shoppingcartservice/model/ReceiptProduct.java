package com.zavod.shoppingcartservice.model;

import java.math.BigDecimal;

public class ReceiptProduct {

    private String name;
    private int cty;
    private BigDecimal priceForOneProduct;
    private BigDecimal priceForAllProducts;

    public ReceiptProduct(ReceiptProduct product){
        this.name = product.name;
        this.cty = product.cty;
        this.priceForOneProduct = product.priceForOneProduct;
        this.priceForAllProducts = product.priceForAllProducts;
    }

    public ReceiptProduct(String name, int cty, BigDecimal pricePerItem){
        this.name = name;
        this.cty = cty;
        this.priceForOneProduct = pricePerItem;
        this.priceForAllProducts = pricePerItem.multiply(new BigDecimal(cty));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCty() {
        return cty;
    }

    public void setCty(int cty) {
        this.cty = cty;
    }

    public BigDecimal getPriceForOneProduct() {
        return priceForOneProduct;
    }

    public void setPriceForOneProduct(BigDecimal priceForOneProduct) {
        this.priceForOneProduct = priceForOneProduct;
    }

    public BigDecimal getPriceForAllProducts() {
        return priceForAllProducts;
    }

    public void setPriceForAllProducts(BigDecimal priceForAllProducts) {
        this.priceForAllProducts = priceForAllProducts;
    }

    public void incrementCty(){
        this.cty++;
        this.priceForAllProducts = this.priceForAllProducts.add(this.priceForOneProduct);
    }

    @Override
    public String toString() {
        return "ReceiptProduct{" +
                "name='" + name + '\'' +
                ", cty=" + cty +
                ", priceForOneProduct=" + priceForOneProduct +
                ", priceForAllProducts=" + priceForAllProducts +
                '}';
    }
}
