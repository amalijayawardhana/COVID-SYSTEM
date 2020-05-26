package util;

public class HospitalTM {
    private String hospitalId;
    private String hospitalName;
    private String city;
    private String district;
    private String capacity;
    private String directorName;
    private String directorContactNumber;
    private String hospitalContactNumber1;
    private String hospitalContactNumber2;
    private String hospitalFaxNumber;
    private String hospitalEmail;


    public HospitalTM() {
    }

    public HospitalTM(String hospitalId, String hospitalName, String city, String district, String capacity, String directorName, String directorContactNumber, String hospitalContactNumber1, String hospitalContactNumber2, String hospitalFaxNumber, String hospitalEmail) {
        this.setHospitalId(hospitalId);
        this.setHospitalName(hospitalName);
        this.setCity(city);
        this.setDistrict(district);
        this.setCapacity(capacity);
        this.setDirectorName(directorName);
        this.setDirectorContactNumber(directorContactNumber);
        this.setHospitalContactNumber1(hospitalContactNumber1);
        this.setHospitalContactNumber2(hospitalContactNumber2);
        this.setHospitalFaxNumber(hospitalFaxNumber);
        this.setHospitalEmail(hospitalEmail);
    }


    public void setHospitalEmail(String hospitalEmail) {
        this.hospitalEmail = hospitalEmail;
    }



    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDirectorContactNumber() {
        return directorContactNumber;
    }

    public void setDirectorContactNumber(String directorContactNumber) {
        this.directorContactNumber = directorContactNumber;
    }

    public String getHospitalContactNumber1() {
        return hospitalContactNumber1;
    }

    public void setHospitalContactNumber1(String hospitalContactNumber1) {
        this.hospitalContactNumber1 = hospitalContactNumber1;
    }

    public String getHospitalContactNumber2() {
        return hospitalContactNumber2;
    }

    public void setHospitalContactNumber2(String hospitalContactNumber2) {
        this.hospitalContactNumber2 = hospitalContactNumber2;
    }

    public String getHospitalFaxNumber() {
        return hospitalFaxNumber;
    }

    public void setHospitalFaxNumber(String hospitalFaxNumber) {
        this.hospitalFaxNumber = hospitalFaxNumber;
    }

    public String getHospitalEmail() {
        return hospitalEmail;
    }
    @Override
    public String toString() {
        return hospitalName;
    }

   /* @Override
    public String toString() {
        return "HospitalTM{" +
                "hospitalId='" + hospitalId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", capacity='" + capacity + '\'' +
                ", directorName='" + directorName + '\'' +
                ", directorContactNumber='" + directorContactNumber + '\'' +
                ", hospitalContactNumber1='" + hospitalContactNumber1 + '\'' +
                ", hospitalContactNumber2='" + hospitalContactNumber2 + '\'' +
                ", hospitalFaxNumber='" + hospitalFaxNumber + '\'' +
                ", hospitalEmail='" + hospitalEmail + '\'' +
                '}';
    }*/
}
