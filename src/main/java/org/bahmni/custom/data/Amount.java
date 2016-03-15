package org.bahmni.custom.data;

/**
 * Created by sandeepe on 14/03/16.
 */
public class Amount {
    private double tribal;
    private double nonTribal;
    private int shopId;
    private String shopName;

    public void setTribal(double tribal) {
        this.tribal = tribal;
    }

    public double getTribal() {
        return tribal;
    }

    public void setNonTribal(double nonTribal) {
        this.nonTribal = nonTribal;
    }

    public double getNonTribal() {
        return nonTribal;
    }

    public int getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
