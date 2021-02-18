/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author julie
 */
public class ApptType {
 
    private String year;
    private String month;
    private String type;
    private int countType; 
    

    public ApptType(String year, String month, String type, int countType) {
        this.year = year;
        this.month = month; 
        this.type = type;
        this.countType = countType;
    }    

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
        
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }
    
  
    
    
}
