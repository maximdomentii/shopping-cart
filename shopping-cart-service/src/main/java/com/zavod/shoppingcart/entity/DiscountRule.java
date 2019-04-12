package com.zavod.shoppingcart.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "discount_rule")
public class DiscountRule implements Serializable {

    private static final long serialVersionUID = -3355071523887666183L;

    @Id
    @GeneratedValue
    private long discountRuleId;

    @NotEmpty
    private String description;

    private int numberOfItems;

    private double discountPercentage;

    private boolean enable;

    @ManyToMany(mappedBy = "discountRules")
    private Set<Product> products = new HashSet<>();

    public long getDiscountRuleId() {
        return discountRuleId;
    }

    public void setDiscountRuleId(long discountRuleId) {
        this.discountRuleId = discountRuleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountRule)) return false;
        DiscountRule that = (DiscountRule) o;
        return discountRuleId == that.discountRuleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountRuleId);
    }
}
