/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webclient.userinterface;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *  Dosent work
 * @author Bad-K
 */
public class DisplayTrayIcon {
    static TrayIcon trayIcon;
    public DisplayTrayIcon() {
        ShowTrayIcon();
    }
    
    public static void ShowTrayIcon(){
        if(!SystemTray.isSupported()){
            System.out.println("Error, pc bug");
            System.exit(0);
        }
        trayIcon = new TrayIcon(CreateIcon("BGTranss.png","Tray Icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        try{
            tray.add(trayIcon);
        }catch(AWTException e){
            
        }
        
    }
    protected static Image CreateIcon(String path, String desc){
        URL ImageUrl = DisplayTrayIcon.class.getResource(path);
        return (new ImageIcon(ImageUrl,desc)).getImage();
    }
    
}
