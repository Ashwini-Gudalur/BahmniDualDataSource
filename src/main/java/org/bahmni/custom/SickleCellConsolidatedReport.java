package org.bahmni.custom;

import org.bahmni.custom.data.Patient;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sandeepe on 02/03/16.
 */
public class SickleCellConsolidatedReport extends AbstractBahmniReport {
    @Override
    public List<Patient> getReportData() {

        Integer integer = getErpJdbcTemplate().queryForObject("select count(*) from res_partner", Integer.class);

        System.out.println("Sandeep:Check jdbc:" + integer);

        String sql = "select sv.erp_patient_id,rp.ref,rp.name,spe.gender,rpa.address1,sv.visit_startdate," +
                "sv.visit_stopdate,case when sv.diagnoses ISNULL then '' END as Diagnosis from syncjob_visit sv" +
                "  inner join syncjob_sickle_cell_patient sscp on sscp.erp_id=sv.erp_patient_id AND" +
                "                                                 sv.visit_type='General' " +
                "  inner JOIN syncjob_patient_extn spe on spe.erp_id=sv.erp_patient_id" +
                "  INNER JOIN res_partner rp on rp.id=sscp.erp_id" +
                "  inner JOIN res_partner_address rpa on rp.id = rpa.partner_id";
        List<Patient> patients = getErpJdbcTemplate().query(sql,
//                new Object[]{getStartDate(), getEndDate(), getStartDate(), getEndDate()},
                new RowMapper<Patient>() {
                    public Patient mapRow(ResultSet rs, int rownumber) throws SQLException {
                        Patient e = new Patient();
                        e.setId(rs.getInt(1));
                        e.setBahmniId(rs.getString(2));
                        e.setName(rs.getString(3));
                        e.setGender(rs.getString(4));
                        e.setAddress(rs.getString(5));
                        e.setVisitStart(rs.getString(6));
                        e.setVisitEnd(rs.getString(7));
                        e.setDiagnosis(rs.getString(8));
                        return e;
                    }
                });
        System.out.println("Sandeep:PatientSize:" + patients.size());
        return patients;
    }
}
