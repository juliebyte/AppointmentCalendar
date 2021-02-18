/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


/**
 *  The Customer class 
 * @author julie
 */
public class CustomerAllDetails {
    private int customerId;
    private String customerName;
    private int addressId;
    private String address;
    private int cityId;
    private String city;
    private int countryId;
    private String country; 
    private String phone;

    // The Customer Constructor
    public CustomerAllDetails(int customerId, String customerName, int addressId, Boolean active,
            String address, int cityId, String city, int countryId, 
            String country, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.address = address;
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.country = country;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
    /**
     * This method will return a customer formatted for display    
     */
    //public String toString()  {
    //    return String.format("%s of %s %s %s", customerName, address, city, country);          
    //}    
    
}
