/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.City;
import Model.Country;
import Utilities.DBConnection;
import Utilities.DBQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author julie
 */
public class CountryDaoImpl {  
    
     public static Country getSpecificCountry(City city) throws SQLException, Exception{
        
        DBConnection.getConnection();
        
        String sqlStatement = "select * FROM country WHERE countryId  = '" + city.getCountryId() + "'"; 
         
        DBQuery.makeQuery(sqlStatement);
           Country countryResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int countryIdResult = result.getInt("countryId");
                String country = result.getString("country");       
                countryResult = new Country(countryIdResult, country);
                return countryResult;
           }
        return null;
    }
    public static ObservableList<Country> getAllCountries() throws SQLException, Exception{
        ObservableList<Country> allCountries = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            String sqlStatement = "select * from country";          
            DBQuery.makeQuery(sqlStatement);
            ResultSet result = DBQuery.getResult();
             while(result.next()){
                int countryIdResult = result.getInt("countryId");
                String country = result.getString("country");    
                Country countryResult = new Country(countryIdResult, country);
                allCountries.add(countryResult);                
            }
        return allCountries;
        }
    
    
}
