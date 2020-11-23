package shravan.nyshadh.billmaker.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Customer implements Serializable {
    @SerializedName("cust_id")
    @Expose
    private Integer CustomerId;
    @SerializedName("cust_name")
    @Expose
    private String CustomerName;
    @SerializedName("cust_phone")
    @Expose
    private String CustomerPhone;
    @SerializedName("cust_phone2")
    @Expose
    private String CustomerPhone2;
    @SerializedName("cust_email")
    @Expose
    private String CustomerEmail;
    @SerializedName("cust_age")
    @Expose
    private String CustomerAge;
    @SerializedName("cust_gender")
    @Expose
    private String CustomerGender;
    @SerializedName("cust_address")
    @Expose
    private String CustomerAddress;
    @SerializedName("cust_remarks")
    @Expose
    private String CustomerRemarks;
    @SerializedName("cust_right_IPD")
    @Expose
    private String CustomerRightIPD;
    @SerializedName("cust_left_IPD")
    @Expose
    private String CustomerLeftIPD;
    @SerializedName("prescriber_id")
    @Expose
    private Integer prescriberId;

    public Integer getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Integer CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String CustomerPhone) {
        this.CustomerPhone = CustomerPhone;
    }

    public String getCustomerPhone2() {
        return CustomerPhone2;
    }

    public void setCustomerPhone2(String CustomerPhone2) {
        this.CustomerPhone2 = CustomerPhone2;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String CustomerEmail) {
        this.CustomerEmail = CustomerEmail;
    }

    public String getCustomerAge() {
        return CustomerAge;
    }

    public void setCustomerAge(String CustomerAge) {
        this.CustomerAge = CustomerAge;
    }

    public String getCustomerGender() {
        return CustomerGender;
    }

    public void setCustomerGender(String CustomerGender) {
        this.CustomerGender = CustomerGender;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }

    public String getCustomerRemarks() {
        return CustomerRemarks;
    }

    public void setCustomerRemarks(String CustomerRemarks) {
        this.CustomerRemarks = CustomerRemarks;
    }

    public String getCustomerRightIPD() {
        return CustomerRightIPD;
    }

    public void setCustomerRightIPD(String CustomerRightIPD) {
        this.CustomerRightIPD = CustomerRightIPD;
    }

    public String getCustomerLeftIPD() {
        return CustomerLeftIPD;
    }

    public void setCustomerLeftIPD(String CustomerLeftIPD) {
        this.CustomerLeftIPD = CustomerLeftIPD;
    }

    public Integer getPrescriberId() {
        return prescriberId;
    }

    public void setPrescriberId(Integer prescriberId) {
        this.prescriberId = prescriberId;
    }
}
