package com.zavod.shoppingcartservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -4100695159090724071L;

    @Id
    @GeneratedValue
    private long productId;

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

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "product_discount_rule_mapping",
            joinColumns = { @JoinColumn(name = "product_id")},
            inverseJoinColumns = { @JoinColumn(name = "discount_rule_id")}
    )
    private Set<DiscountRule> discountRules = new HashSet<>();

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public Set<DiscountRule> getDiscountRules() {
        return discountRules;
    }

    public void setDiscountRules(Set<DiscountRule> discountRules) {
        this.discountRules = discountRules;
    }
}
