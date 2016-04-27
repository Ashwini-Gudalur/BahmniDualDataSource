package org.bahmni.custom.data;

import org.bahmni.custom.Utils;

/**
 * Created by sandeepe on 12/04/16.
 */
public class AccountVoucherLine {
    private int id;
    private double amount;
    private double unreconciled;
    private double amountOriginal;
    private Utils.TRN_TYPE type;
    private String SOName;
    private String SOCareSetting;
    private Boolean tribal;
    private double allocation;
    private int SOId;
    private Integer categoryId;

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    private String voucherNumber;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setUnreconciled(double unreconciled) {
        this.unreconciled = unreconciled;
    }

    public double getUnreconciled() {
        return unreconciled;
    }

    public void setAmountOriginal(double amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public double getAmountOriginal() {
        return amountOriginal;
    }

    public void setType(Utils.TRN_TYPE type) {
        this.type = type;
    }

    public Utils.TRN_TYPE getType() {
        return type;
    }

    public void setSOName(String SOName) {
        this.SOName = SOName;
    }

    public String getSOName() {
        return SOName;
    }

    public void setSOCareSetting(String SOCareSetting) {
        this.SOCareSetting = SOCareSetting;
    }

    public String getSOCareSetting() {
        return SOCareSetting;
    }

    public void setTribal(Boolean tribal) {
        this.tribal = tribal;
    }

    public Boolean getTribal() {
        return tribal;
    }

    public void setAllocation(double allocation) {
        this.allocation = allocation;
    }

    public double getAllocation() {
        return allocation;
    }

    public void setSOId(int SOId) {
        this.SOId = SOId;
    }

    public int getSOId() {
        return SOId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
}
