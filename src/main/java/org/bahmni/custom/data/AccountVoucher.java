package org.bahmni.custom.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeepe on 12/04/16.
 */
public class AccountVoucher {
    private int id;
    private double amount;
    private double balanceAmount;
    private double balanceBefore;
    private String number;
    private double refundWithoutReason;
    private double customerDue;
    private double advanceWithoutReason;
    private double paymentWithoutReason;
    private Boolean tribal;

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

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }


    public void setBalanceBefore(double balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountVoucher voucher = (AccountVoucher) o;
        if (id != voucher.id) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = id;
        /*long temp;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(balanceAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(balanceBefore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (number != null ? number.hashCode() : 0);*/
        return result;
    }

    public List<AccountVoucherLine> getCrLines() {
        return crLines;
    }

    public void setCrLines(List<AccountVoucherLine> crLines) {
        this.crLines = crLines;
    }


    public void addCrLine(AccountVoucherLine line) {
        crLines.add(line);
    }

    public void addDrLine(AccountVoucherLine line) {
        drLines.add(line);
    }

    private List<AccountVoucherLine> crLines = new ArrayList<AccountVoucherLine>();
    private List<AccountVoucherLine> drLines = new ArrayList<AccountVoucherLine>();

    public List<AccountVoucherLine> getDrLines() {
        return drLines;
    }

    public void setDrLines(List<AccountVoucherLine> drLines) {
        this.drLines = drLines;
    }

    public void setRefundWithoutReason(double refundWithoutReason) {
        this.refundWithoutReason = refundWithoutReason;
    }

    public double getRefundWithoutReason() {
        return refundWithoutReason;
    }

    public void setCustomerDue(double customerDue) {
        this.customerDue = customerDue;
    }

    public double getCustomerDue() {
        return customerDue;
    }

    public void setAdvanceWithoutReason(double advanceWithoutReason) {
        this.advanceWithoutReason = advanceWithoutReason;
    }

    public double getAdvanceWithoutReason() {
        return advanceWithoutReason;
    }

    public void setPaymentWithoutReason(double paymentWithoutReason) {
        this.paymentWithoutReason = paymentWithoutReason;
    }

    public double getPaymentWithoutReason() {
        return paymentWithoutReason;
    }

    public void setTribal(Boolean tribal) {
        this.tribal = tribal;
    }

    public Boolean getTribal() {
        return tribal;
    }
}
