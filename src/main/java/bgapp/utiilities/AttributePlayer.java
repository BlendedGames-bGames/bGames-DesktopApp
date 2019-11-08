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
public class AttributePlayer
    {
        private int _id = 0;
        private String _name = "";
        private String _name_category = "";
        private int _data  = 0;
        private String _data_type  = "";
        private String _input_source = "";
        private String _date_time = "";
    
    
        //id_subattributes, nameat, namecategory, data, data_type, input_source, date_time, attributes_id_attributes

        public AttributePlayer(int Id, String Name, String NameCategory, int Data, String data_Type, String Source, String date_Time) {
            this._id = Id;
            this._name = Name;
            this._name_category = NameCategory;
            this._data  = Data;
            this._data_type  = data_Type;
            this._input_source = Source;
            this._date_time = date_Time;
        }
        public AttributePlayer() {
            this._id = 0;
            this._name = "";
            this._name_category = "";
            this._data  = 0;
            this._data_type  = "";
            this._input_source = "";
            this._date_time = "";
        }

        /**
         * @return the _input_source
         */
        public String getInput_source() {
            return _input_source;
        }

        /**
         * @param _input_source the _input_source to set
         */
        public void setInput_source(String _input_source) {
            this._input_source = _input_source;
        }

        /**
         * @return the _name_category
         */
        public String getName_category() {
            return _name_category;
        }

        /**
         * @param _name_category the _name_category to set
         */
        public void setName_category(String _name_category) {
            this._name_category = _name_category;
        }

        /**
         * @return the _name
         */
        public String getName() {
            return _name;
        }

        /**
         * @param _name the _name to set
         */
        public void setName(String _name) {
            this._name = _name;
        }

        /**
         * @return the data
         */
        public int getData() {
            return _data;
        }

        /**
         * @param _data the data to set
         */
        public void setData(int _data) {
            this._data = _data;
        }

        /**
         * @return the data_type
         */
        public String getData_type() {
            return _data_type;
        }

        /**
         * @param _data_type the data_type to set
         */
        public void setData_type(String _data_type) {
            this._data_type = _data_type;
        }

        /**
         * @return the date_time
         */
        public String getDate_time() {
            return _date_time;
        }

        /**
         * @param _date_time the date_time to set
         */
        public void setDate_time(String _date_time) {
            this._date_time = _date_time;
        }

        /**
         * @return the _id
         */
        public int getId() {
            return _id;
        }

        /**
         * @param _id the _id to set
         */
        public void setId(int _id) {
            this._id = _id;
        }
    
    }
