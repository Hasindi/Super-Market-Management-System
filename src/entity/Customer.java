package entity;

public class Customer {
    private String CustID;
    private String CustTitle;
    private String CustName;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostCode;

    public Customer() {
    }

    public Customer(String custID, String custTitle, String custName, String custAddress, String city, String province, String postCode) {
        CustID = custID;
        CustTitle = custTitle;
        CustName = custName;
        CustAddress = custAddress;
        City = city;
        Province = province;
        PostCode = postCode;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getCustTitle() {
        return CustTitle;
    }

    public void setCustTitle(String custTitle) {
        CustTitle = custTitle;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "CustID='" + CustID + '\'' +
                ", CustTitle='" + CustTitle + '\'' +
                ", CustName='" + CustName + '\'' +
                ", CustAddress='" + CustAddress + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostCode='" + PostCode + '\'' +
                '}';
    }
}
