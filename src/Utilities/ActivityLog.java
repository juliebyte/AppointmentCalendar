/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

/**
 *
 * @author julie
 */
public class ActivityLog {
    
    private static final String FILENAME = "CalendarLog.txt";
    
    public void LoginLogger() {}

    // Keep a record of all login attempts
    public static void createActivityLog(String username, boolean result) {
        
        try (FileWriter writerFle = new FileWriter(FILENAME, true);
            BufferedWriter writerBuf = new BufferedWriter(writerFle);
            PrintWriter writerPrt = new PrintWriter(writerBuf)) {
            writerPrt.println(ZonedDateTime.now() + " " + username + " " + (result ? "Successful Login" : "Failure"));
            System.out.println("Wrote to log file");
        } catch (IOException ex) {
            System.out.println("Error in ActivityLog  " + ex.getMessage());
            System.out.println("Log error: " + ex.getMessage());
        }        
    }   
}