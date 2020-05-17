/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;

import bgapp.utiilities.SensorNeed;
import bgapp.utiilities.AttributePlayer;
import static com.webclient.userinterface.BGFApp.ListSensors;
import static com.webclient.userinterface.BGFApp.myControllerHandle;

/**
 *  The observer of Sensors
 * @author InTEracTIon User
 */
public class ObsPlayer implements Observer {

    public ObsPlayer(){
    }
    
    @Override
    public void update(AttributePlayer AP) {
        System.out.println("Observer recibe Attribute of Player");
    }

    @Override
    public void update() {
        System.out.println("Observer see conection of Sensor"); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void update(SensorNeed SN){
        System.out.println("Observer recibe Sensor \""+SN.getSensorName()+"\" information and add to list for new sensors");
        //System.out.println("AGREGO UN SN CORRECTAEMNTE ----------------***************----------***********-----------------************--------"+ SN.getPlayerId());
        //System.out.println("AGREGO UN SN CORRECTAEMNTE ----------------***************----------***********-----------------************--------"+ SN.getSensorCategory());
        //System.out.println("AGREGO UN SN CORRECTAEMNTE ----------------***************----------***********-----------------************--------"+ SN.getSensorDescripcion());
        ListSensors.add(SN);
        myControllerHandle.reloadDataForTables();
    }

}
