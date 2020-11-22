package shravan.nyshadh.billmaker.Modal;

import java.io.Serializable;

public class Customer implements Serializable {
    private Integer CustomerId;
    private String CustomerName;
    private String CustomerPhone;
    private String CustomerPhone2;
    private String CustomerEmail;
    private String CustomerAge;
    private String CustomerGender;
    private String CustomerAddress;
    private String CustomerRemarks;
    private String CustomerRightIPD;
    private String CustomerLeftIPD;
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
