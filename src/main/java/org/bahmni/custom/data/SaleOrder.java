package org.bahmni.custom.data;

/**
 * Created by sandeepe on 12/03/16.
 */
public class SaleOrder {
    private int id;
    private double amount_total;
    private double discount_amount;
    private boolean tribal;
    private int categId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(double amount_total) {
        this.amount_total = amount_total;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SaleOrder saleOrder = (SaleOrder) o;

        return id == saleOrder.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setTribal(String tribal) {
        this.tribal = Boolean.valueOf(tribal);
    }

    public boolean isTribal() {
        return tribal;
    }

    public void setCategId(int categId) {
        this.categId = categId;
    }

    public int getCategId() {
        return categId;
    }
}
