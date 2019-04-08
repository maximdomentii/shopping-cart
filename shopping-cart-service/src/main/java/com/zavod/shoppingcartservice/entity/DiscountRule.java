package com.zavod.shoppingcartservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
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

    private double discoutPercentage;

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

    public double getDiscoutPercentage() {
        return discoutPercentage;
    }

    public void setDiscoutPercentage(double discoutPercentage) {
        this.discoutPercentage = discoutPercentage;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
