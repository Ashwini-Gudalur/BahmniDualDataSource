package org.bahmni.custom.reports;

import org.bahmni.custom.AbstractBahmniReport;
import org.bahmni.custom.Utils;
import org.bahmni.custom.data.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by sandeepe on 15/03/16.
 */
public class DepartmentCollectionOld extends AbstractBahmniReport{
    @Override
    public List<ReportLine> getReportData() {
        List<DepartmentReport> daybook = getSaleOrdersForTodayWithDepartment();
        return convert(daybook);
    }

    public List<DepartmentReport> getSaleOrdersForTodayWithDepartment() {

        String commonCatIdQuery = "SELECT id from product_category where name = 'Common' limit 1";
        Integer commonsId = getErpJdbcTemplate().queryForObject(commonCatIdQuery, Integer.class);

        String orderCategQuery = "SELECT order_id,max(pt.categ_id),so.discount_amount,so.amount_total+so.discount_amount,rpa.\"x_Is_Tribal\" " +
                "FROM sale_order_line sol " +
                "LEFT JOIN product_product pp on pp.id = sol.product_id " +
                "LEFT JOIN product_template pt on pt.id = pp.product_tmpl_id " +
                "LEFT JOIN sale_order so on so.id = sol.order_id "+
                "LEFT JOIN res_partner_attributes rpa on rpa.partner_id=so.partner_id "+
                "where pt.categ_id not in (" +commonsId+
                ") and order_id in " +
                "(SELECT id FROM sale_order where state!='draft' and cast(date_confirm as DATE) between ? and ? and care_setting='opd') " +
                "GROUP BY order_id,so.discount_amount,so.amount_total,rpa.\"x_Is_Tribal\"";
        List<SaleOrder> saleOrders = getErpJdbcTemplate().query(orderCategQuery, new Object[]{getStartDate(),getEndDate()},new RowMapper<SaleOrder>() {
            public SaleOrder mapRow(ResultSet resultSet, int i) throws SQLException {
                SaleOrder so = new SaleOrder();
                so.setId(resultSet.getInt(1));
                so.setAmount_total(resultSet.getDouble(4));
                so.setDiscount_amount(resultSet.getDouble(3));
                so.setTribal(resultSet.getString(5));
                so.setCategId(resultSet.getInt(2));
                return so;
            }
        });

        String department = (String) getParams().get("department");
        if (!Utils.isEmptyString(department)){
            department= " where department_name='"+department+"'";
        }else {
            department = "";
        }

        String deptQuery = "select department_name,category_id from syncjob_department_category_mapping "+department;
        final Map<String,List<Integer>> deptCatMap = new HashMap<String, List<Integer>>();
        getErpJdbcTemplate().query(deptQuery, new RowMapper<Void>() {
            public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                String deptName = resultSet.getString(1);
                List<Integer> catList = deptCatMap.get(deptName);
                if (catList==null){
                    catList=new ArrayList<Integer>();
                    deptCatMap.put(deptName,catList);
                }
                catList.add(resultSet.getInt(2));
                return null;
            }
        });
        final Map<String,List<SaleOrder>> departmentSOMap = getSaleOrdersForDepartments(saleOrders,deptCatMap);
        final Map<String,List<Invoice>> departmentInvoiceMap = new HashMap<String, List<Invoice>>();
        for (Map.Entry<String, List<SaleOrder>> dept : departmentSOMap.entrySet()) {
            String deptName = dept.getKey();
            List<Invoice> invList = getInvoiceForDept(dept.getValue());
            departmentInvoiceMap.put(deptName,invList);
        }
        List<DepartmentReport> departmentReports = new ArrayList<DepartmentReport>();
        for (Map.Entry<String, List<SaleOrder>> dept : departmentSOMap.entrySet()) {
            DepartmentReport departmentReport = new DepartmentReport();

            double billAmountTribal = getTotalSum(dept.getValue(),true);
            double totalCharityTribal = getCharitySum(dept.getValue(), true);
            double paidAmountTribal = getPaidAmount(departmentInvoiceMap.get(dept.getKey()), true);
            double refundAmountTribal = getRefundAmount(departmentInvoiceMap.get(dept.getKey()), true);

            double billAmountNonTribal = getTotalSum(dept.getValue(), false);
            double totalCharityNonTribal = getCharitySum(dept.getValue(), false);
            double paidAmountNonTribal = getPaidAmount(departmentInvoiceMap.get(dept.getKey()), false);
            double refundAmountNonTribal = getRefundAmount(departmentInvoiceMap.get(dept.getKey()), false);

            departmentReport.setDepartment(dept.getKey().trim());
            departmentReport.setTribal(new DepartmentReport.ReportLine(totalCharityTribal, paidAmountTribal, billAmountTribal,refundAmountTribal));
            departmentReport.setNonTribal(new DepartmentReport.ReportLine(totalCharityNonTribal, paidAmountNonTribal, billAmountNonTribal,refundAmountNonTribal));
            departmentReport.setTotal(new DepartmentReport.ReportLine(totalCharityTribal + totalCharityNonTribal,
                    paidAmountTribal + paidAmountNonTribal, billAmountTribal + billAmountNonTribal,refundAmountTribal+refundAmountNonTribal));
            departmentReports.add(departmentReport);
        }

        return departmentReports;
    }

    private double getRefundAmount(List<Invoice> invoices, boolean tribal) {
        double total = 0;
        if(!Utils.isEmptyList(invoices)) {
            for (Invoice inv : invoices) {
                if (inv.isTribal()==tribal){
                    if (inv.getType().equals(Utils.INV_TYPE.REFUND)){
                        total += (inv.getAmount_total()-inv.getResidual());
                    }
                }
            }
        }
        return total;
    }

    private double getPaidAmount(List<Invoice> invoices, boolean tribal) {
        double total = 0;
        if(!Utils.isEmptyList(invoices)) {
            for (Invoice inv : invoices) {
                if (inv.isTribal()==tribal){
                    if (inv.getType().equals(Utils.INV_TYPE.PAYMENT)) {
                        total += (inv.getAmount_total() - inv.getResidual());
                    }
                }
            }
        }
        return total;
    }

    private double getCharitySum(List<SaleOrder> value, boolean tribal) {
        double total = 0;
        if(!Utils.isEmptyList(value)) {
            for (SaleOrder saleOrder : value) {
                if (saleOrder.isTribal()==tribal){
                    total += saleOrder.getDiscount_amount();
                }
            }
        }
        return total;
    }

    private double getTotalSum(List<SaleOrder> value, boolean tribal) {
        double total = 0;
        if(!Utils.isEmptyList(value)) {
            for (SaleOrder saleOrder : value) {
                if (saleOrder.isTribal()==tribal){
                    total += saleOrder.getAmount_total();
                }
            }
        }
        return total;
    }

    private List<Invoice> getInvoiceForDept(List<SaleOrder> value) {
        String invoiceQuery = "SELECT residual, amount_total,state,type,soir.order_id,soir.invoice_id,rpa.\"x_Is_Tribal\" from account_invoice ai " +
                "INNER JOIN sale_order_invoice_rel soir on soir.invoice_id=ai.id and soir.order_id in (" +Utils.getCSString(value)+
                ") " +
                "LEFT JOIN res_partner_attributes rpa on rpa.partner_id=ai.partner_id "+
                "where (ai.type='out_invoice' or ai.type = 'out_refund') and (ai.state='paid' or ai.state='open')";

        List<Invoice> invLst = getErpJdbcTemplate().query(invoiceQuery,  new RowMapper<Invoice>() {
            public Invoice mapRow(ResultSet resultSet, int i) throws SQLException {
                Invoice inv = new Invoice();
                inv.setResidual(resultSet.getDouble(1));
                inv.setAmount_total(resultSet.getDouble(2));
                inv.setState(Utils.getInvState(resultSet.getString(3)));
                inv.setType(Utils.getInvType(resultSet.getString(4)));
                inv.setOrder_id(resultSet.getInt(5));
                inv.setInvoice_id(resultSet.getInt(6));
                inv.setTribal(resultSet.getString(7));
                return inv;
            }
        });
        return invLst;
    }

    private Map<String, List<SaleOrder>> getSaleOrdersForDepartments(List<SaleOrder> saleOrderCatMap, Map<String, List<Integer>> deptCatMap) {
        Map<String,List<SaleOrder>> departmentSOMap = new HashMap<String, List<SaleOrder>>();
        for (SaleOrder saleOrderIntegerEntry : saleOrderCatMap) {
            String departmentForSO = getDepartmentForSO(saleOrderIntegerEntry, deptCatMap);
            if (Utils.isEmptyString(departmentForSO)){
                continue;
            }
            List<SaleOrder> catList = departmentSOMap.get(departmentForSO);
            if (catList==null){
                catList=new ArrayList<SaleOrder>();
                departmentSOMap.put(departmentForSO,catList);
            }
            catList.add(saleOrderIntegerEntry);
        }
        return departmentSOMap;
    }

    private String getDepartmentForSO(SaleOrder so, Map<String, List<Integer>> deptCatMap) {
        for (Map.Entry<String, List<Integer>> stringListEntry : deptCatMap.entrySet()) {
            List<Integer> value = stringListEntry.getValue();
            if (value.contains(so.getCategId())){
                return stringListEntry.getKey();
            }
        }
        return null;
    }
}
