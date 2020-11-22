package shravan.nyshadh.billmaker.Modal;

public class Presciber {
    private Integer prescriberId;
    private String prescriberName;
    private String prescriberEmail;
    private String prescriberPhone;
    private String prescriberAddress;

    public Presciber(Integer prescriberId, String prescriberName) {
        this.prescriberId = prescriberId;
        this.prescriberName = prescriberName;
    }

    public Presciber() {
    }

    public Integer getPrescriberId() {
        return prescriberId;
    }

    public void setPrescriberId(Integer prescriberId) {
        this.prescriberId = prescriberId;
    }

    public String getPrescriberName() {
        return prescriberName;
    }

    public void setPrescriberName(String prescriberName) {
        this.prescriberName = prescriberName;
    }

    public String getPrescriberEmail() {
        return prescriberEmail;
    }

    public void setPrescriberEmail(String prescriberEmail) {
        this.prescriberEmail = prescriberEmail;
    }

    public String getPrescriberPhone() {
        return prescriberPhone;
    }

    public void setPrescriberPhone(String prescriberPhone) {
        this.prescriberPhone = prescriberPhone;
    }

    public String getPrescriberAddress() {
        return prescriberAddress;
    }

    public void setPrescriberAddress(String prescriberAddress) {
        this.prescriberAddress = prescriberAddress;
    }
}
