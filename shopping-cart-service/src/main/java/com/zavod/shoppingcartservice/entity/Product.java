package com.zavod.shoppingcartservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -4100695159090724071L;

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    @GeneratedValue
    private long barcode;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(name = "price_per_um")
    private BigDecimal pricePerUM;

    private boolean available;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public BigDecimal getPricePerUM() {
        return pricePerUM;
    }

    public void setPricePerUM(BigDecimal pricePerUM) {
        this.pricePerUM = pricePerUM;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
