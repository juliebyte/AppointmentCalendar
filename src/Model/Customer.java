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
public class Customer {
    private int customerId;
    private String customerName;
    private int addressId;
   

    // The Customer Constructor
    public Customer(int customerId, String customerName, int addressId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
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

    
    
    @Override
    public String toString()  {
        return this.getCustomerName();
    }
     
    /**
     * This method will return a customer formatted for display    
     */
    //public String toString()  {
    //    return String.format("%s of %s %s %s", customerName, address, city, country);          
    //}
       
}
