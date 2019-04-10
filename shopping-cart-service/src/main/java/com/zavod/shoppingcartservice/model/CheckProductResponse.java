package com.zavod.shoppingcartservice.model;

public class CheckProductResponse {

    private long barcode;
    private String name;
    private boolean available;

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "CheckProductResponse{" +
                "barcode=" + barcode + ", " +
                "name='" + name + '\'' + ", " +
                "available=" + available + '}';
    }
}
