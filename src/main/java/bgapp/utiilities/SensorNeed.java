/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgapp.utiilities;

import bgapp.utiilities.AttributePlayer;
import java.util.ArrayList;
import mod.sensors.Observer;

/**
 *
 * @author InTEracTIon User
 */
public class SensorNeed {
        private String sensorVersion;
        private String sensorCategory;
        private String sensorDescripcion;
        private ArrayList<AttributePlayer> listOfAttributes ;
        private int PlayerId ;
        private String Host;
        public ArrayList<Observer> Observadores;
        private String sensorNombre;
        public SensorNeed(){
            this.sensorVersion = null;
            this.sensorCategory=null;
            this.sensorDescripcion=null;
            this.listOfAttributes = new ArrayList<>();
            this.PlayerId =0;
            this.Host = null;
            this.Observadores = new ArrayList();
        }
        public SensorNeed(String NameApp,String VER,String Cat,String Des,ArrayList<AttributePlayer> LISTATT,int PlayerID,String Host,ArrayList<Observer> Obs){
            this.sensorNombre = NameApp;
            this.sensorVersion = VER;
            this.sensorCategory=Cat;
            this.sensorDescripcion=Des;
            this.listOfAttributes = LISTATT;
            this.PlayerId = PlayerID;
            this.Host = Host;
            this.Observadores = Obs;
        }
        
        /*La función notificar existe para informar a la aplicación principal o los observadores que se encuentren
        sobre un atributo específico.
        El atributo enviado debe ser uno procesado, por ejemplo, si alguien se mantiene
        concentrado mucho tiempo, gana un punto de "resistencia cognitiva" y este puntaje
        se puede almacenar luego en la Base de datos o puede ser utilizado cuando el
        jugador lo considere mejor. (Atributo capturado como característica)
        */
        public void notify(AttributePlayer AP) {
            Observadores.forEach((o) -> {
                o.update(AP);
            });
        }
        public void notifyOK() {
            Observadores.forEach((o) -> {
                o.update();
            });
        }
        
        /**
         * @return the sensorVersion
         */
        public String getSensorVersion() {
            return sensorVersion;
        }

        /**
         * @param sensorVersion the sensorVersion to set
         */
        public void setSensorVersion(String sensorVersion) {
            this.sensorVersion = sensorVersion;
        }

        /**
         * @return the sensorCategory
         */
        public String getSensorCategory() {
            return sensorCategory;
        }

        /**
         * @param sensorCategory the sensorCategory to set
         */
        public void setSensorCategory(String sensorCategory) {
            this.sensorCategory = sensorCategory;
        }

        /**
         * @return the sensorDescripcion
         */
        public String getSensorDescripcion() {
            return sensorDescripcion;
        }

        /**
         * @param sensorDescripcion the sensorDescripcion to set
         */
        public void setSensorDescripcion(String sensorDescripcion) {
            this.sensorDescripcion = sensorDescripcion;
        }

        /**
         * @return the listOfAttributes
         */
        public ArrayList<AttributePlayer> getListOfAttributes() {
            return listOfAttributes;
        }

        /**
         * @param listOfAttributes the listOfAttributes to set
         */
        public void setListOfAttributes(ArrayList<AttributePlayer> listOfAttributes) {
            this.listOfAttributes = listOfAttributes;
        }

        /**
         * @return the PlayerId
         */
        public int getPlayerId() {
            return PlayerId;
        }

        /**
         * @param PlayerId the PlayerId to set
         */
        public void setPlayerId(int PlayerId) {
            this.PlayerId = PlayerId;
        }

        /**
         * @return the Host
         */
        public String getHost() {
            return Host;
        }

        /**
         * @param Host the Host to set
         */
        public void setHost(String Host) {
            this.Host = Host;
        }
        
        
        /**
         * @return the sensorNombre
         */
        public String getSensorNombre() {
            return sensorNombre;
        }

        /**
         * @param sensorNombre the sensorNombre to set
         */
        public void setSensorNombre(String sensorNombre) {
            this.sensorNombre = sensorNombre;
        }
        
        
    }
