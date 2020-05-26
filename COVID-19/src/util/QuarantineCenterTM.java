package util;

public class QuarantineCenterTM {
    private String quarantineCenterId;
    private String quarantineCenterName;
    private String city;
    private String district;
    private String capacity;
    private String headName;
    private String headContactNumber;
    private String quarantineCenterContactNumber1;
    private String quarantineCenterContactNumber2;
    private String quarantineCenterEmail;

    public QuarantineCenterTM() {
    }

    public QuarantineCenterTM(String quarantineCenterId, String quarantineCenterName, String city, String district, String capacity, String headName, String headContactNumber, String quarantineCenterContactNumber1, String quarantineCenterContactNumber2, String quarantineCenterEmail) {
        this.quarantineCenterId = quarantineCenterId;
        this.quarantineCenterName = quarantineCenterName;
        this.city = city;
        this.district = district;
        this.capacity = capacity;
        this.headName = headName;
        this.headContactNumber = headContactNumber;
        this.quarantineCenterContactNumber1 = quarantineCenterContactNumber1;
        this.quarantineCenterContactNumber2 = quarantineCenterContactNumber2;
        this.quarantineCenterEmail = quarantineCenterEmail;
    }

    public String getQuarantineCenterId() {
        return quarantineCenterId;
    }

    public void setQuarantineCenterId(String quarantineCenterId) {
        this.quarantineCenterId = quarantineCenterId;
    }

    public String getQuarantineCenterName() {
        return quarantineCenterName;
    }

    public void setQuarantineCenterName(String quarantineCenterName) {
        this.quarantineCenterName = quarantineCenterName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadContactNumber() {
        return headContactNumber;
    }

    public void setHeadContactNumber(String headContactNumber) {
        this.headContactNumber = headContactNumber;
    }

    public String getQuarantineCenterContactNumber1() {
        return quarantineCenterContactNumber1;
    }

    public void setQuarantineCenterContactNumber1(String quarantineCenterContactNumber1) {
        this.quarantineCenterContactNumber1 = quarantineCenterContactNumber1;
    }

    public String getQuarantineCenterContactNumber2() {
        return quarantineCenterContactNumber2;
    }

    public void setQuarantineCenterContactNumber2(String quarantineCenterContactNumber2) {
        this.quarantineCenterContactNumber2 = quarantineCenterContactNumber2;
    }

    public String getQuarantineCenterEmail() {
        return quarantineCenterEmail;
    }

    public void setQuarantineCenterEmail(String quarantineCenterEmail) {
        this.quarantineCenterEmail = quarantineCenterEmail;
    }

    @Override
    public String toString() {
        return quarantineCenterName;
    }
}
