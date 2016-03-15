package org.bahmni.custom;

import org.bahmni.custom.data.BahmniReportObject;
import org.bahmni.custom.data.DepartmentReport;
import org.bahmni.custom.data.ReportLine;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sandeepe on 01/03/16.
 */
public abstract class AbstractBahmniReport{

    private DataSource erpDS;
    private DataSource emrDS;
    private Date startDate;
    private Map params;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    private Date endDate;

    protected JdbcTemplate setEmrJdbcTemplate() {
        return emrJdbcTemplate;
    }

    private JdbcTemplate emrJdbcTemplate;

    protected JdbcTemplate setErpJdbcTemplate() {
        return erpJdbcTemplate;
    }

    private JdbcTemplate erpJdbcTemplate;


    public void setErpDS(DataSource erpDS) {
        this.erpDS = erpDS;
        setErpJdbcTemplate(erpDS);
    }

    private void setErpJdbcTemplate(DataSource erpDS) {
        if(this.erpJdbcTemplate == null || erpDS != this.erpJdbcTemplate.getDataSource()) {
            erpJdbcTemplate = createJdbcTemplate(erpDS);
        }
    }

    public DataSource getErpDS() {
        return erpDS;
    }

    public void setEmrDS(DataSource emrDS) {
        this.emrDS = emrDS;
        setEmrJdbcTemplate(emrDS);
    }

    private void setEmrJdbcTemplate(DataSource emrDS) {
        if(this.emrJdbcTemplate == null || erpDS != this.emrJdbcTemplate.getDataSource()) {
            emrJdbcTemplate = createJdbcTemplate(emrDS);
        }
    }

    public DataSource getEmrDS() {
        return emrDS;
    }

    public abstract List<? extends BahmniReportObject> getReportData();

    protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getEmrJdbcTemplate() {
        return emrJdbcTemplate;
    }

    public JdbcTemplate getErpJdbcTemplate() {
        return erpJdbcTemplate;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public Map getParams() {
        return params;
    }
/*
    protected final Connection getConnection() throws CannotGetJdbcConnectionException {
        return DataSourceUtils.getConnection(this.getDataSource());
    }

    protected final void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.getDataSource());
    }*/

    protected List<ReportLine> convert(List<DepartmentReport> daybook) {
        List<ReportLine> out = new ArrayList<ReportLine>();
        for (DepartmentReport departmentReport : daybook) {
            ReportLine line = new ReportLine();
            line.setDepartment(departmentReport.getDepartment());
            DepartmentReport.ReportLine nonTribal = departmentReport.getNonTribal();
            if (nonTribal!=null){
                line.setBillAmountNonTribal(nonTribal.getTotalAmount());
                line.setPaidAmountNonTribal(nonTribal.getCollected());
                line.setRefundAmountNonTribal(nonTribal.getRefundAmount());
                line.setTotalCharityNonTribal(nonTribal.getCharity());
                line.setTotalDueNonTribal(nonTribal.getDueAmount());
            }
            DepartmentReport.ReportLine tribal = departmentReport.getTribal();
            if (tribal!=null){
                line.setBillAmountTribal(tribal.getTotalAmount());
                line.setPaidAmountTribal(tribal.getCollected());
                line.setRefundAmountTribal(tribal.getRefundAmount());
                line.setTotalCharityTribal(tribal.getCharity());
                line.setTotalDueTribal(tribal.getDueAmount());
            }
            DepartmentReport.ReportLine total = departmentReport.getTotal();
            if (total!=null){
                line.setBillTotal(total.getTotalAmount());
                line.setCharityTotal(total.getCharity());
                line.setCollectedTotal(total.getCollected());
                line.setDueTotal(total.getDueAmount());
                line.setRefundTotal(total.getRefundAmount());
            }
            out.add(line);
        }
        return out;
    }
}
