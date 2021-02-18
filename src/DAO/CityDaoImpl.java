/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Address;
import Model.City;
import Model.CityCountry;
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
public class CityDaoImpl {    
    
     public static City getSpecificCity(Address address) throws SQLException, Exception{
        
        DBConnection.getConnection();
        
        String sqlStatement = "select * FROM city WHERE cityId  = '" + address.getCityId() + "'";  
         
        DBQuery.makeQuery(sqlStatement);
           City cityResult;
           ResultSet result = DBQuery.getResult();
           while(result.next()){
                int cityIdResult = result.getInt("cityId");
                String city = result.getString("city");
                int countryId = result.getInt("countryId");         
                cityResult = new City(cityIdResult, city, countryId);
                return cityResult;
           }
        return null;
    }
     
     
    public static ObservableList<City> getAllCities() throws SQLException, Exception{
        ObservableList<City> allCities = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            String sqlStatement = "select * from city";          
            DBQuery.makeQuery(sqlStatement);
            ResultSet result = DBQuery.getResult();
             while(result.next()){
                int cityIdResult = result.getInt("cityId");
                String city = result.getString("city");
                int countryId = result.getInt("countryId");     
                City cityResult = new City(cityIdResult, city, countryId);
                allCities.add(cityResult);                
            }
        return allCities;
        }
     
     
    public static ObservableList<CityCountry> getCitiesCountries() throws SQLException, Exception{
        ObservableList<CityCountry> allCities = FXCollections.observableArrayList();    
        DBConnection.getConnection();
            String sqlStatement = "select city.city, country.country from city join country on city.countryId = country.countryId; ";          
            DBQuery.makeQuery(sqlStatement);
            ResultSet result = DBQuery.getResult();
             while(result.next()){
                String city = result.getString("city");
                String country = result.getString("country");     
                CityCountry cityResult = new CityCountry(city, country);
                allCities.add(cityResult);                
            }
        return allCities;
        }
    
    
}
