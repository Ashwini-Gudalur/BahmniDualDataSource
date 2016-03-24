package org.bahmni.custom.reports;

import org.bahmni.custom.AbstractBahmniReport;
import org.bahmni.custom.Util;
import org.bahmni.custom.Utils;
import org.bahmni.custom.data.Amount;
import org.bahmni.custom.data.BahmniReportObject;
import org.bahmni.custom.data.DepartmentReport;
import org.bahmni.custom.data.ReportLine;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by sandeepe on 14/03/16.
 */
public class DayBook extends AbstractBahmniReport {

    private String CASHPOINT_PREFIX = "CashPoint";

    @Override
    public List<ReportLine> getReportData() {
        List<DepartmentReport> daybook = getDaybook();
        return convert(daybook);
    }
    enum AMOUNT_TYPE{BILLED,DISCOUNT,DUE,COLLECTED,REFUND};
    public List<DepartmentReport> getDaybook() {
        Map<String,EnumMap<AMOUNT_TYPE, Amount>> storeAmountMap = new HashMap<String, EnumMap<AMOUNT_TYPE, Amount>>();
        String billedAmountQuery = "select sum(amount_total+discount_amount),shop_id,rpa.\"x_Is_Tribal\",ss.name from sale_order so " +
                "  LEFT JOIN res_partner_attributes rpa on rpa.partner_id = so.partner_id " +
                "  LEFT JOIN sale_shop ss on ss.id=so.shop_id " +
                "WHERE   state!='draft' and cast(date_confirm as DATE) between ? and ? " +
                "GROUP BY shop_id,\"x_Is_Tribal\",ss.name";
        setAmount(billedAmountQuery, AMOUNT_TYPE.BILLED, storeAmountMap);

        String discountQry = "select sum(discount_amount) as \"Discount\" ,shop_id,rpa.\"x_Is_Tribal\",ss.name  from sale_order so " +
                "  LEFT JOIN res_partner_attributes rpa on rpa.partner_id = so.partner_id " +
                "  LEFT JOIN sale_shop ss on ss.id=so.shop_id " +
                "WHERE state!='draft' and cast(date_confirm as DATE) between ? and ? " +
                "GROUP BY shop_id,\"x_Is_Tribal\",ss.name";
        setAmount(discountQry, AMOUNT_TYPE.DISCOUNT, storeAmountMap);

        String dueQuery = "select sum(ai.residual) as Due,so.shop_id,rpa.\"x_Is_Tribal\",ss.name   from account_invoice ai " +
                "  INNER JOIN sale_order so on so.name=ai.origin and cast(date_confirm as DATE) between ? and ? " +
                "  LEFT JOIN res_partner_attributes rpa on rpa.partner_id=so.partner_id " +
                "  LEFT JOIN sale_shop ss on ss.id=so.shop_id " +
                "GROUP BY so.shop_id,\"x_Is_Tribal\",ss.name ";
        setAmount(dueQuery, AMOUNT_TYPE.DUE, storeAmountMap);

        String amtCollectedQry = "SELECT sum(avl.amount) as invoice,shop_id,rpa.\"x_Is_Tribal\",ss.name   from account_voucher_line avl" +
                "  INNER JOIN account_voucher av on av.id = avl.voucher_id and  avl.type='cr' " +
                "and cast(av.date_string as DATE) between ? and ? " +
                "INNER JOIN res_partner_attributes rpa on rpa.partner_id=av.partner_id " +
                "  LEFT JOIN sale_shop ss on ss.id=av.shop_id " +
                "GROUP BY shop_id,\"x_Is_Tribal\",ss.name  ";
        setAmount(amtCollectedQry, AMOUNT_TYPE.COLLECTED, storeAmountMap);
        String amtRefundQry = "SELECT sum(avl.amount) as invoice,shop_id,rpa.\"x_Is_Tribal\",ss.name  from account_voucher_line avl " +
                "  INNER JOIN account_voucher av on av.id = avl.voucher_id and  avl.type='dr' " +
                "  and cast(av.date_string as DATE) between ? and ? " +
                "INNER JOIN res_partner_attributes rpa on rpa.partner_id=av.partner_id " +
                "  LEFT JOIN sale_shop ss on ss.id=av.shop_id\n" +
                "GROUP BY shop_id,\"x_Is_Tribal\",ss.name  ";
        setAmount(amtRefundQry, AMOUNT_TYPE.REFUND, storeAmountMap);
        List<DepartmentReport> cosolidate = cosolidate(storeAmountMap);
        return cosolidate;

    }

    private List<DepartmentReport> cosolidate(Map<String, EnumMap<AMOUNT_TYPE, Amount>> storeAmountMap) {
        List<DepartmentReport> list = new ArrayList<DepartmentReport>();

        for (Map.Entry<String, EnumMap<AMOUNT_TYPE, Amount>> shop : storeAmountMap.entrySet()) {
            DepartmentReport rpt = new DepartmentReport();
            rpt.setDepartment(shop.getKey());
            EnumMap<AMOUNT_TYPE, Amount> value = shop.getValue();
            Amount billedAmount = value.get(AMOUNT_TYPE.BILLED);
            Amount discoutAmount = value.get(AMOUNT_TYPE.DISCOUNT);
            Amount collectedAmount = value.get(AMOUNT_TYPE.COLLECTED);
            Amount refundAmount = value.get(AMOUNT_TYPE.REFUND);
            Amount dueAmount = value.get(AMOUNT_TYPE.DUE);
            DepartmentReport.ReportLine notTribal = new DepartmentReport.ReportLine((discoutAmount!=null)?discoutAmount.getNonTribal():0,
                    (collectedAmount!=null)?collectedAmount.getNonTribal():0,
                    (billedAmount!=null)?billedAmount.getNonTribal():0,
                    (refundAmount!=null)?refundAmount.getNonTribal():0,
                    (dueAmount!=null)?dueAmount.getNonTribal():0);
            DepartmentReport.ReportLine tribal = new DepartmentReport.ReportLine((discoutAmount!=null)?discoutAmount.getTribal():0,
                    (collectedAmount!=null)?collectedAmount.getTribal():0,
                    (billedAmount!=null)?billedAmount.getTribal():0,
                    (refundAmount!=null)?refundAmount.getTribal():0,
                    (dueAmount!=null)?dueAmount.getTribal():0);
            DepartmentReport.ReportLine total = new DepartmentReport.ReportLine(
                    (discoutAmount!=null)?discoutAmount.getNonTribal():0+ ((discoutAmount!=null)?discoutAmount.getTribal():0),
                    (collectedAmount!=null)?collectedAmount.getNonTribal():0+((collectedAmount!=null)?collectedAmount.getTribal():0),
                    (billedAmount!=null)?billedAmount.getNonTribal():0+((billedAmount!=null)?billedAmount.getTribal():0),
                    (refundAmount!=null)?refundAmount.getNonTribal():0+((refundAmount!=null)?refundAmount.getTribal():0),
                    (dueAmount!=null)?dueAmount.getNonTribal():0+((dueAmount!=null)?dueAmount.getTribal():0));
            rpt.setTotal(total);
            rpt.setNonTribal(notTribal);
            rpt.setTribal(tribal);
            list.add(rpt);
        }
        return list;
    }

    private void setAmount(String billedAmountQuery, final AMOUNT_TYPE billed, final Map<String, EnumMap<AMOUNT_TYPE, Amount>> storeAmountMap) {
        getErpJdbcTemplate().query(billedAmountQuery, new Object[]{getStartDate(),getEndDate()},new RowMapper<Void>() {
            public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                String shopName = resultSet.getString(4);
                if (Utils.isEmptyString(shopName)){
                    return null;
                }
                if (shopName.toLowerCase().startsWith(CASHPOINT_PREFIX.toLowerCase())){
                    shopName = CASHPOINT_PREFIX;
                }
                EnumMap<AMOUNT_TYPE, Amount> stringAmountMap = storeAmountMap.get(shopName);
                if (stringAmountMap == null) {
                    stringAmountMap = new EnumMap<AMOUNT_TYPE, Amount>(AMOUNT_TYPE.class);
                    storeAmountMap.put(shopName, stringAmountMap);
                }
                Amount amt = stringAmountMap.get(billed);
                if (amt == null) {
                    amt = new Amount();
                    stringAmountMap.put(billed, amt);
                }
                if (Boolean.valueOf(resultSet.getString(3))) {
                    double currValue = amt.getTribal();
                    amt.setTribal(resultSet.getDouble(1) + currValue);
                } else {
                    double currValue = amt.getNonTribal();
                    amt.setNonTribal(resultSet.getDouble(1) + currValue);
                }
                amt.setShopId(resultSet.getInt(2));
                amt.setShopName(shopName);
                return null;
            }
        });

    }
}
