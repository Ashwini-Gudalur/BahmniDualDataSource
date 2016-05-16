package org.bahmni.custom.data;

/**
 * Created by sandeepe on 13/05/16.
 */
public class IPCostItem implements BahmniReportObject{
    private String type;

    private double tribalAmt;
    private double tribalCount;
    private double nonTribalAmt;
    private double nonTribalCount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTribalAmt() {
        return tribalAmt;
    }

    public void setTribalAmt(double tribalAmt) {
        this.tribalAmt = tribalAmt;
    }

    public double getTribalCount() {
        return tribalCount;
    }

    public void setTribalCount(double tribalCount) {
        this.tribalCount = tribalCount;
    }

    public double getNonTribalAmt() {
        return nonTribalAmt;
    }

    public void setNonTribalAmt(double nonTribalAmt) {
        this.nonTribalAmt = nonTribalAmt;
    }

    public double getNonTribalCount() {
        return nonTribalCount;
    }

    public void setNonTribalCount(double nonTribalCount) {
        this.nonTribalCount = nonTribalCount;
    }

    public double getTotalAmt() {
        return tribalAmt+nonTribalAmt;
    }

    public double getTotalCount() {
        return tribalCount+nonTribalCount;
    }

    @Override
    public String toString() {
        return "IPCostItem{" +
                "type='" + type + '\'' +
                ", tribalAmt=" + tribalAmt +
                ", tribalCount=" + tribalCount +
                ", nonTribalAmt=" + nonTribalAmt +
                ", nonTribalCount=" + nonTribalCount +
                ", totalAmt=" + getTotalAmt() +
                ", totalCount=" + getTotalCount() +
                "}\n";
    }
}
