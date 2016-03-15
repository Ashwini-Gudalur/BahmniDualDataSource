package org.bahmni.custom.data;

/**
 * Created by sandeepe on 15/03/16.
 */
public class ReportLine implements BahmniReportObject {

    private double billAmountTribal;
    private double paidAmountTribal;
    private double refundAmountTribal;
    private double totalCharityTribal;
    private double totalDueTribal;

    private double billAmountNonTribal;
    private double paidAmountNonTribal;
    private double refundAmountNonTribal;
    private double totalCharityNonTribal;
    private double totalDueNonTribal;

    private double billTotal;
    private double refundTotal;
    private double charityTotal;
    private double collectedTotal;
    private double dueTotal;

    public double getTotalDueTribal() {
        return totalDueTribal;
    }

    public void setTotalDueTribal(double totalDueTribal) {
        this.totalDueTribal = totalDueTribal;
    }

    public double getTotalDueNonTribal() {
        return totalDueNonTribal;
    }

    public void setTotalDueNonTribal(double totalDueNonTribal) {
        this.totalDueNonTribal = totalDueNonTribal;
    }

    public double getDueTotal() {
        return dueTotal;
    }

    public void setDueTotal(double dueTotal) {
        this.dueTotal = dueTotal;
    }

    private String department;

    public double getBillAmountTribal() {
        return billAmountTribal;
    }

    public void setBillAmountTribal(double billAmountTribal) {
        this.billAmountTribal = billAmountTribal;
    }

    public double getPaidAmountTribal() {
        return paidAmountTribal;
    }

    public void setPaidAmountTribal(double paidAmountTribal) {
        this.paidAmountTribal = paidAmountTribal;
    }

    public double getRefundAmountTribal() {
        return refundAmountTribal;
    }

    public void setRefundAmountTribal(double refundAmountTribal) {
        this.refundAmountTribal = refundAmountTribal;
    }

    public double getTotalCharityTribal() {
        return totalCharityTribal;
    }

    public void setTotalCharityTribal(double totalCharityTribal) {
        this.totalCharityTribal = totalCharityTribal;
    }

    public double getBillAmountNonTribal() {
        return billAmountNonTribal;
    }

    public void setBillAmountNonTribal(double billAmountNonTribal) {
        this.billAmountNonTribal = billAmountNonTribal;
    }

    public double getPaidAmountNonTribal() {
        return paidAmountNonTribal;
    }

    public void setPaidAmountNonTribal(double paidAmountNonTribal) {
        this.paidAmountNonTribal = paidAmountNonTribal;
    }

    public double getRefundAmountNonTribal() {
        return refundAmountNonTribal;
    }

    public void setRefundAmountNonTribal(double refundAmountNonTribal) {
        this.refundAmountNonTribal = refundAmountNonTribal;
    }

    public double getTotalCharityNonTribal() {
        return totalCharityNonTribal;
    }

    public void setTotalCharityNonTribal(double totalCharityNonTribal) {
        this.totalCharityNonTribal = totalCharityNonTribal;
    }

    public double getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(double billTotal) {
        this.billTotal = billTotal;
    }

    public double getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(double refundTotal) {
        this.refundTotal = refundTotal;
    }

    public double getCharityTotal() {
        return charityTotal;
    }

    public void setCharityTotal(double charityTotal) {
        this.charityTotal = charityTotal;
    }

    public double getCollectedTotal() {
        return collectedTotal;
    }

    public void setCollectedTotal(double collectedTotal) {
        this.collectedTotal = collectedTotal;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
