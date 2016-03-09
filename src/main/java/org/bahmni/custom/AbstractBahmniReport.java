package org.bahmni.custom;

import org.bahmni.custom.data.BahmniReportObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
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
}
