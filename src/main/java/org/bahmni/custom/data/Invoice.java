package org.bahmni.custom.data;


import org.bahmni.custom.Utils;

/**
 * Created by sandeepe on 14/03/16.
 */
public class Invoice {
    private double residual;
    private double amount_total;
    private Utils.INV_STATE state;
    private boolean tribal;

    public Utils.INV_TYPE getType() {
        return type;
    }

    public void setType(Utils.INV_TYPE type) {
        this.type = type;
    }

    public Utils.INV_STATE getState() {
        return state;
    }

    public void setState(Utils.INV_STATE state) {
        this.state = state;
    }

    private Utils.INV_TYPE type;
    private int order_id;
    private int invoice_id;

    public double getResidual() {
        return residual;
    }

    public void setResidual(double residual) {
        this.residual = residual;
    }

    public double getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(double amount_total) {
        this.amount_total = amount_total;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public void setTribal(String tribal) {
        this.tribal = Boolean.valueOf(tribal);
    }

    public boolean isTribal() {
        return tribal;
    }
}
