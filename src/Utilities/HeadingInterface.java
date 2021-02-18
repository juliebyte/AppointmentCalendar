/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 * Lambda for constructing messages to use in headings and titles.
 * @author julie
 */
public interface HeadingInterface {
    
    // String object returning a completed heading message.
    // this is more efficient than typing out all of the code every time. 
    String getMessage(String msg);
    
}
