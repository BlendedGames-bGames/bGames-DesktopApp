/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket.init.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bad-K
 */
public class WebSocketServerInit{
    private String cmd = "cd websocket";
    private String dd = "dir";
    private String initSocket = "npm start";
    
    public WebSocketServerInit(String OS) throws IOException {
        System.out.println("EMPIEza!?");
        if ("Unix".equals(OS)) {
            cmd = "cd websocket";
            dd = "";
            initSocket = "npm start";
            
        }else{
            System.out.println("You run Windows OS ? haha");
        }
    }
    
        
    //Stop
    public void stop() throws IOException{
        initSocket = "npm stop";
        Runtime.getRuntime().exec("cmd.exe /c" + cmd +"&&"  +initSocket); // npm stop
    }
    
    
    public void run() {
        
        try {
            // PRUEBAAS!!!
            
            Runtime.getRuntime().exec("cmd.exe /c" + cmd +"&&" +dd+"&&" +initSocket);
            //Process process = Runtime.getRuntime().exec("cmd.exe /c" + cmd +"&&" +dd+"&&" +initSocket);
            //setProcessServer(process);
            System.out.println("HOOOOOOOOLAAAAAAAAAAAAAAAA");
            //setProcessServer(builder.start());
            
            
            // PRUEBAAS!!!
        } catch (IOException ex) {
            Logger.getLogger(WebSocketServerInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
