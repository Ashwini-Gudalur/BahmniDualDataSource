package org.bahmni.custom.data;

/**
 * Created by sandeepe on 14/03/16.
 */
public class DepartmentReport {

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ReportLine getTribal() {
        return tribal;
    }

    public void setTribal(ReportLine tribal) {
        this.tribal = tribal;
    }

    public ReportLine getNonTribal() {
        return nonTribal;
    }

    public void setNonTribal(ReportLine nonTribal) {
        this.nonTribal = nonTribal;
    }

    public ReportLine getTotal() {
        return total;
    }

    public void setTotal(ReportLine total) {
        this.total = total;
    }

    private String department;
    private ReportLine tribal;
    private ReportLine nonTribal;
    private ReportLine total;

    @Override
    public String toString() {
        return "DepartmentReport{" +
                "department='" + department + '\'' +
                ", tribal=" + tribal +
                ", nonTribal=" + nonTribal +
                ", total=" + total +
                '}';
    }

    public static class ReportLine{
        public double getDueAmount() {
            return dueAmount;
        }

        private double dueAmount=0;

        public ReportLine(double charity, double collected, double totalAmount,double refundAmt, double dueAmt) {
            this(charity,collected,totalAmount,refundAmt);
            this.dueAmount = dueAmt;
        }

        public double getRefundAmount() {
            return refundAmount;
        }

        private double refundAmount;

        public ReportLine(double charity, double collected, double totalAmount,double refundAmt) {
            this.charity = charity;
            this.collected = collected;
            this.totalAmount = totalAmount;
            this.refundAmount = refundAmt;
        }

        @Override
        public String toString() {
            return "ReportLine{" +
                    "charity=" + charity +
                    ", collected=" + collected +
                    ", refundAmount=" + refundAmount +
                    ", totalAmount=" + totalAmount +
                    '}';
        }

        private double charity;
        private double collected;

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public double getCollected() {
            return collected;
        }

        public void setCollected(double collected) {
            this.collected = collected;
        }

        public double getCharity() {
            return charity;
        }

        public void setCharity(double charity) {
            this.charity = charity;
        }

        private double totalAmount;
    }


}
