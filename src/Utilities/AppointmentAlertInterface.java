/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.time.LocalTime;

/**
 *
 * @author julie
 */
public interface AppointmentAlertInterface {
    
    // String object returning a completed heading message.
    // this is more efficient than typing out all of the code every time. 
    String apptAlertMsg(LocalTime time, String name);
    
} 

