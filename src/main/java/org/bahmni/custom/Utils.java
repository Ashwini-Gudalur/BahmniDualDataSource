package org.bahmni.custom;

import org.bahmni.custom.data.SaleOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sandeepe on 02/03/16.
 */
public class Utils {
    public static Date getFormattedDate(String dateStr) {
        return getFormattedDate(dateStr,"dd-MMM-yyyy");

    }

    public static Date getFormattedDate(String dateStr, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

    }

    public static Object getObjectFromClassName(String reportClass) {
        try {
            Class<?> clazz = Class.forName(reportClass);
            Object classInstance = clazz.newInstance();
            return classInstance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCSString(List<SaleOrder> value) {
        if (value !=null && value.size()>0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (SaleOrder n : value) {
                nameBuilder.append(n.getId() + ",");
                // can also do the following
                // nameBuilder.append("'").append(n.replace("'", "''")).append("',");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    public static INV_TYPE getInvType(String type) {
        if ("out_refund".equals(type)) {
            return INV_TYPE.REFUND;
        }else if ("out_invoice".equals(type)) {
            return INV_TYPE.PAYMENT;
        }
        return INV_TYPE.PAYMENT;
    }

    public static INV_STATE getInvState(String type) {
        if ("open".equals(type)) {
            return INV_STATE.OPEN;
        }else if ("paid".equals(type)) {
            return INV_STATE.PAID;
        }
        return INV_STATE.OPEN;
    }

    public static boolean isNotEmptyList(List<SaleOrder> value) {
        return false;
    }


    public enum INV_TYPE {PAYMENT, REFUND};
    public enum INV_STATE {PAID, OPEN};
    public enum STATUS {STARTED, FAILED, SUCCESS};

    public enum GENDER {M, F, O};

    public enum VISIT_TYPES {UNKNOWN, IPD, OPD};
    public static boolean isEmptyList(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmptyMap(Map list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmptyString(String departmentForSO) {
        return departmentForSO==null || departmentForSO.length()==0 || departmentForSO.trim().length()==0;
    }

    public enum TRN_TYPE {DR, CR};
    public static TRN_TYPE getTransactionType(String type) {
        if ("dr".equals(type)) {
            return TRN_TYPE.DR;
        }else if ("cr".equals(type)) {
            return TRN_TYPE.CR;
        }
        return TRN_TYPE.CR;
    }

}
