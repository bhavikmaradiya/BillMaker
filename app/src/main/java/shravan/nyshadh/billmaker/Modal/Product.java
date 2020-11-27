package shravan.nyshadh.billmaker.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("unitprice")
    @Expose
    private String unitprice;
    @SerializedName("sellprice")
    @Expose
    private String sellprice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("totalprice")
    @Expose
    private String totalprice;
    @SerializedName("lense_index")
    @Expose
    private String lenseIndex;
    @SerializedName("coating")
    @Expose
    private String coating;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("basecurve")
    @Expose
    private String basecurve;
    @SerializedName("biometer")
    @Expose
    private String biometer;
    @SerializedName("watercontent")
    @Expose
    private String watercontent;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("comments")
    @Expose
    private String comments;

    private double discountPercentage = 0;

    public double getDiscountAmount() {
        return discountPercentage != 0 ? (Double.parseDouble(this.sellprice) * this.quantity * this.discountPercentage) / 100 : 0;
    }

    public double getPayable() {
        return (Double.parseDouble(this.sellprice) * this.quantity) - ((Double.parseDouble(this.sellprice) * this.quantity * this.discountPercentage) / 100);
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getLenseIndex() {
        return lenseIndex;
    }

    public void setLenseIndex(String lenseIndex) {
        this.lenseIndex = lenseIndex;
    }

    public String getCoating() {
        return coating;
    }

    public void setCoating(String coating) {
        this.coating = coating;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBasecurve() {
        return basecurve;
    }

    public void setBasecurve(String basecurve) {
        this.basecurve = basecurve;
    }

    public String getBiometer() {
        return biometer;
    }

    public void setBiometer(String biometer) {
        this.biometer = biometer;
    }

    public String getWatercontent() {
        return watercontent;
    }

    public void setWatercontent(String watercontent) {
        this.watercontent = watercontent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
