package com.webclient.userinterface;

import bgapp.utiilities.SensorNeed;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static com.webclient.userinterface.BGFApp.ListSensors;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;


public class FXMLController implements Initializable {
    
    @FXML
    public MenuItem logout;

    @FXML
    private TextField textEmail;    
    @FXML
    private Label label;
    @FXML
    private Button buttonMin;
    @FXML
    public Button buttonReconnectSens;
    @FXML
    public Button buttonConsumeAtt;
    @FXML
    private TableView<SensorNeed> sourcesTable;
    @FXML
    private TableColumn<SensorNeed, String> col_estado;
    @FXML
    private TableColumn<SensorNeed, String> col_nombre;
    @FXML
    private TableColumn<SensorNeed, String> col_attributess;
    @FXML
    private TableColumn<SensorNeed, String> col_versionPlug;
    @FXML
    private TableColumn<SensorNeed, Integer> col_Acciones;
    
                
    @FXML
    public void handleButtonReconnectSensAction(ActionEvent event) {
        //label.setText("Hello World!");
    }
    @FXML
    public void handleButtonConsumeBatchAttributes(ActionEvent event) {
        //label.setText("Hello World!");
    }
    @FXML
    public void handleMenuItemLogout(ActionEvent event) {

    }
    @FXML
    public void handleButtonMinAction(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) buttonMin.getScene().getWindow();
        // do what you have to do
        stage.hide();
        //label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDataForAttributeTableSecond();
    }
    void initData(String email) {
      textEmail.setText(email);
    }    
    
    
    private void loadDataForAttributeTableSecond(){
        
        col_estado.setCellValueFactory(new PropertyValueFactory<>("Host"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("sensorNombre"));
        col_attributess.setCellValueFactory(new PropertyValueFactory<>("sensorCategory"));
        col_versionPlug.setCellValueFactory(new PropertyValueFactory<>("sensorVersion"));
        col_Acciones.setCellValueFactory(new PropertyValueFactory<>("PlayerId"));
        
        col_estado.setStyle("-fx-alignment: CENTER;");
        col_nombre.setStyle("-fx-alignment: CENTER;");
        col_attributess.setStyle("-fx-alignment: CENTER;");
        col_versionPlug.setStyle("-fx-alignment: CENTER;");
        col_Acciones.setStyle("-fx-alignment: CENTER;");
        
        ObservableList<SensorNeed> table_data2 = FXCollections.observableArrayList();
        for (SensorNeed SensorNeed0 : ListSensors){
            table_data2.add(SensorNeed0);
        }
        sourcesTable.setItems(table_data2);
    }
    
    public void reloadDataForTables(){
        ObservableList<SensorNeed> table_data2 = FXCollections.observableArrayList();
        for (SensorNeed SensorNeed0 : ListSensors){
            table_data2.add(SensorNeed0);
        }
        sourcesTable.setItems(table_data2);
    }
    

}
