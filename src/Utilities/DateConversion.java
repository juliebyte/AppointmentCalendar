/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author carolyn.sher
 */
public class DateConversion {
    
       public static final SimpleDateFormat dateFormatWithSecs = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       public static final DateTimeFormatter dateFormatterWithSecs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       public static final DateTimeFormatter dateFormatterNoSecs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");      
       public static final DateFormat civilianTime = new SimpleDateFormat("hh:mm aa");
       public static final DateFormat militaryTime = new SimpleDateFormat("HH:mm:ss");
       
       // Converts UTC from database to LocalDateTime for display.
       public static LocalDateTime fromUTCToLocalTime (Timestamp timeUTC) {
           LocalDateTime localDateTime = timeUTC.toLocalDateTime();
           ZonedDateTime zonedLocalDateTime = localDateTime.atZone (ZoneId.of("UTC"));
           ZonedDateTime zonedUTC = zonedLocalDateTime.withZoneSameInstant(ZoneId.systemDefault());
           String stringTime = zonedUTC.format(dateFormatterWithSecs);
           LocalDateTime formattedLocalDateTime = LocalDateTime.parse(stringTime, dateFormatterWithSecs);
           return formattedLocalDateTime;
       } 
       
       
    // Converts LocalDateTime to UTC for storage in database.
    public static Timestamp fromLocalTimetoUTC(LocalDateTime localDT) {
        ZonedDateTime localDTZoned = localDT.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = localDTZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return Timestamp.valueOf(utcZoned.toLocalDateTime());
    }    
    
    // Takes the date and time from the user input, combines them, and formats to localdatetime.
    public static LocalDateTime combineAppointmentDateTime(LocalDate localApptDate, String localApptTime) throws ParseException {
    String reformattedMilitary = militaryTime.format(civilianTime.parse(localApptTime));
    String stringDateTime = localApptDate + " " + reformattedMilitary;
    LocalDateTime formattedDateTime = LocalDateTime.parse(stringDateTime, dateFormatterWithSecs);
    return formattedDateTime;
    }
}
    