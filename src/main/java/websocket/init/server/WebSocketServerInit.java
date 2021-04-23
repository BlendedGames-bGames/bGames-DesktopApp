/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.init.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Bad-K
 */
public class WebSocketServerInit{
    private String cmd = "cd websocket";
    private String dd = "dir";
    private String initSocket = "node index.js";
    private String initSocket2 = "node indexDevices.js";

    public WebSocketServerInit(String OS) throws IOException {
        File currentDirFile = new File("websocket");
        cmd = currentDirFile.getAbsolutePath();
        if ("Unix".equals(OS)) {
            cmd = "cd websocket";
            dd = "";
            initSocket = "node index.js";
            initSocket2 = "node indexDevices.js";
            
        }else{
            //System.out.println("You run Windows OS ? haha");
        }
    }
    
    
    //Stop
    public void stop() throws IOException{
        initSocket = "node stop.js";
        Process process4 = Runtime.getRuntime().exec("cmd.exe /c "+ initSocket,null,new File(cmd)); // npm stop

    }
    
    
    public void run() {
        
        try {
            // PRUEBAAS!!!
            
            
            Process process2 = Runtime.getRuntime().exec("cmd.exe /c "+ initSocket,null,new File(cmd));
            //Process process3 = Runtime.getRuntime().exec("cmd.exe /c "+ initSocket2,null,new File(cmd)); // npm stop
            
            
            // PRUEBAAS!!!
        } catch (IOException ex) {
            Logger.getLogger(WebSocketServerInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
