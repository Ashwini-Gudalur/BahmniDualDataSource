package org.bahmni.custom.reports;

import org.bahmni.custom.AbstractBahmniReport;
import org.bahmni.custom.Utils;
import org.bahmni.custom.data.IPCostItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeepe on 13/05/16.
 */
public class IPCostAnalysis extends AbstractBahmniReport{

    IPCostItem getChargeTypeDetails(String type){
        String sql = "SELECT" +
                "  sum(pwithtax) as medicine," +
                "  tribal as med_tribal, " +
                "  count(DISTINCT order_partner_id) med_patient_count " +
                "FROM (SELECT " +
                "        sol.id, " +
                "        sol.price_unit, " +
                "        sum(at.amount), " +
                "        sol.order_partner_id, " +
                "        (sol.price_unit+(sol.price_unit*coalesce(sum(at.amount), 0))) * sol.product_uom_qty AS pwithtax, " +
                "        rpa.\"x_Is_Tribal\"                      AS tribal " +
                "      FROM sale_order_line sol " +
                "        INNER JOIN syncjob_visit vsr ON " +
                "                                       sol.create_date BETWEEN vsr.visit_startdate AND vsr.visit_stopdate AND " +
                "                                       sol.order_partner_id = vsr.erp_patient_id " +
                "                                       AND vsr.visit_type_id = 1 AND " +
                "                                       vsr.visit_stopdate BETWEEN '2016-01-01' AND '2016-06-01' " +
                "        LEFT JOIN sale_order_tax sot ON sol.id = sot.order_line_id " +
                "        LEFT JOIN account_tax at ON at.id = sot.tax_id " +
                "        LEFT JOIN res_partner_attributes rpa ON sol.order_partner_id = rpa.partner_id " +
                " " +
                "      WHERE product_id IN ( " +
                "        SELECT pp.id " +
                "        FROM product_product pp " +
                "          INNER JOIN product_template pt ON pp.product_tmpl_id = pt.id " +
                "        WHERE pt.categ_id IN (SELECT category_id " +
                "                              FROM syncjob_chargetype_category_mapping " +
                "                              WHERE chargetype_name = '" +type+
                "')) " +
                "            AND (sol.create_date BETWEEN '2016-01-01' AND '2016-06-01') " +
                "            AND (sol.state = 'confirmed') " +
                "      GROUP BY sol.id, sol.order_partner_id, rpa.\"x_Is_Tribal\") AS sale_details " +
                "GROUP BY tribal";
        final IPCostItem item = new IPCostItem();
        getErpJdbcTemplate().query(sql, new RowMapper<Void>() {
            public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                if(Boolean.valueOf(resultSet.getString(2))){
                    item.setTribalAmt(item.getTribalAmt()+resultSet.getDouble(1));
                    item.setTribalCount(item.getTribalCount()+resultSet.getDouble(3));
                }else{
                    item.setNonTribalAmt(item.getNonTribalAmt() + resultSet.getDouble(1));
                    item.setNonTribalCount(item.getNonTribalCount() + resultSet.getDouble(3));
                }
                return null;
            }
        });
        return item;

    }

    List<String> getAllChargeTypes(){
        String sql = "SELECT DISTINCT chargetype_name from syncjob_chargetype_category_mapping";
        List<String> chargeTypes = getErpJdbcTemplate().query(sql, new RowMapper<String>() {

            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1);
            }
        });
        return chargeTypes;
    }

    IPCostItem getTotalPatientCount(){
        String sql = "select count(visit_uuid),rpa.\"x_Is_Tribal\" as tribal from syncjob_visit vsr " +
                " LEFT JOIN res_partner_attributes rpa ON vsr.erp_patient_id = rpa.partner_id " +
                "where vsr.visit_type_id = 1 and vsr.visit_stopdate BETWEEN '2016-01-01' AND '2016-06-01' " +
                "GROUP BY tribal";
        final IPCostItem item = new IPCostItem();
        getErpJdbcTemplate().query(sql, new RowMapper<Void>() {
            public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                if(Boolean.valueOf(resultSet.getString(2))){
                    item.setTribalCount(item.getTribalCount() + resultSet.getDouble(1));
                }else{
                    item.setNonTribalCount(item.getNonTribalCount() + resultSet.getDouble(1));
                }
                return null;
            }
        });
        return item;
    }


    IPCostItem getPaidAmount(){
        String sql = "select sum(t.paid),rpa.\"x_Is_Tribal\" as tribal from syncjob_visit sv " +
                "  INNER JOIN visit_so_payment_rln t on t.visit_id = sv.id " +
                " LEFT JOIN res_partner_attributes rpa ON sv.erp_patient_id = rpa.partner_id " +
                "where " +
                " sv.visit_type_id = 1 and sv.visit_stopdate BETWEEN '2016-01-01' AND '2016-06-01' " +
                "GROUP BY tribal";
        final IPCostItem item = new IPCostItem();
        getErpJdbcTemplate().query(sql, new RowMapper<Void>() {
            public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                if(Boolean.valueOf(resultSet.getString(2))){
                    item.setTribalAmt(item.getTribalAmt() + resultSet.getDouble(1));
                }else{
                    item.setNonTribalAmt(item.getNonTribalAmt() + resultSet.getDouble(1));
                }
                return null;
            }
        });
        return item;
    }

    public List<IPCostItem> getCostItems() {
        List<IPCostItem> ret = new ArrayList<IPCostItem>();
        List<String> allChargeTypes = getAllChargeTypes();
        IPCostItem totalPatientCount = getTotalPatientCount();
        if (!Utils.isEmptyList(allChargeTypes)){
            for (String allChargeType : allChargeTypes) {
                IPCostItem chargeTypeDetails = getChargeTypeDetails(allChargeType);
                tallyLine(chargeTypeDetails,totalPatientCount);
                chargeTypeDetails.setType(allChargeType);
                ret.add(chargeTypeDetails);
            }
        }
        IPCostItem total = new IPCostItem();
        total.setType("Total");
        for (IPCostItem ipCostItem : ret) {
            total.setTribalCount(ipCostItem.getTribalCount()+total.getTribalCount());
            total.setNonTribalCount(ipCostItem.getNonTribalCount()+total.getNonTribalCount());
            total.setNonTribalAmt(ipCostItem.getNonTribalAmt()+total.getNonTribalAmt());
            total.setTribalAmt(ipCostItem.getTribalAmt()+total.getTribalAmt());
        }
        ret.add(total);

        IPCostItem paidAmount = getPaidAmount();
        paidAmount.setType("Amount Paid");
        paidAmount.setTribalCount(paidAmount.getTribalAmt() / totalPatientCount.getTribalCount());
        paidAmount.setNonTribalCount(paidAmount.getNonTribalAmt() / totalPatientCount.getNonTribalCount());
        ret.add(paidAmount);

        IPCostItem difference = getPaidAmount();
        difference.setType("Difference");
        difference.setTribalCount(total.getTribalCount() - paidAmount.getTribalCount());
        difference.setNonTribalCount(total.getNonTribalCount() - paidAmount.getNonTribalCount());
        difference.setNonTribalAmt(total.getNonTribalAmt() - paidAmount.getNonTribalAmt());
        difference.setTribalAmt(total.getTribalAmt() - paidAmount.getTribalAmt());
        ret.add(difference);
        System.out.println(totalPatientCount);
        return ret;
    }

    private void tallyLine(IPCostItem chargeTypeDetails, IPCostItem totalPatientCount) {
        chargeTypeDetails.setTribalCount(chargeTypeDetails.getTribalAmt()/totalPatientCount.getTribalCount());
        chargeTypeDetails.setNonTribalCount(chargeTypeDetails.getNonTribalAmt()/totalPatientCount.getNonTribalCount());
    }

    public List<IPCostItem> getReportData() {
        return getCostItems();
    }
}
