/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgapp.utiilities;

/**
 *
 * @author InTEracTIon User
 */
public class PlayerSummaryAttribute
    {
        private int Id = 0;
        private String Name = "";
        private int data  = 0;
        private String data_type  = "";
        private String date_time = "";
        /**
         * @return the _name
         */
        public String getName() {
            return Name;
        }

        /**
         * @param Name the _name to set
         */
        public void setName(String Name) {
            this.Name = Name;
        }

        /**
         * @return the data
         */
        public int getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(int data) {
            this.data = data;
        }

        /**
         * @return the data_type
         */
        public String getData_type() {
            return data_type;
        }

        /**
         * @param data_type the data_type to set
         */
        public void setData_type(String data_type) {
            this.data_type = data_type;
        }

        /**
         * @return the date_time
         */
        public String getDate_time() {
            return date_time;
        }

        /**
         * @param date_time the date_time to set
         */
        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        /**
         * @return the _id
         */
        public int getId() {
            return Id;
        }

        /**
         * @param Id the _id to set
         */
        public void setId(int Id) {
            this.Id = Id;
        }
    
    }
