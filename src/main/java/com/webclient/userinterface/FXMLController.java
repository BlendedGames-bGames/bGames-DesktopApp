package com.webclient.userinterface;

import static com.webclient.userinterface.BGFApp.attributes;
import static com.webclient.userinterface.BGFApp.attributesAll;
import bgapp.utiilities.AttributePlayer;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static com.webclient.userinterface.BGFApp.ListSensors;
import javafx.stage.Stage;


public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button buttonMin;
    @FXML
    public Button buttonReconnectSens;
    @FXML
    public Button buttonConsumeAtt;
    @FXML
    private BarChart attributesBar;
    @FXML
    private TableView<AttributePlayer> attributesTable;
    @FXML
    private TableColumn<AttributePlayer,Integer> col_BPoints;
    @FXML
    private TableColumn<AttributePlayer,String> col_subAt;
    @FXML
    private TableColumn<AttributePlayer,String> col_at;
    @FXML
    private TableColumn<AttributePlayer,String> col_fecha;
    @FXML
    private TableColumn<AttributePlayer,String> col_fuente;
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
    
    private BGFApp mainApp;
                
    @FXML
    public void handleButtonReconnectSensAction(ActionEvent event) {
        //label.setText("Hello World!");
    }
    public void handleButtonConsumeBatchAttributes(ActionEvent event) {
        //label.setText("Hello World!");
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
        loadDataForBarChart();
        loadDataForAttributeTable();
        loadDataForAttributeTableSecond();
    }    
    
    
    //Inicializa los datos del usuario en el Bar Chart
    private void loadDataForBarChart(){
                
        //Serialización de los datos de la tabla
        XYChart.Series serieDeDatos = new XYChart.Series();
        serieDeDatos.setName("Atributos");
        for(int x=0;x<attributes.size();x++) {
            serieDeDatos.getData().add(new XYChart.Data<>(attributes.get(x).getName(),attributes.get(x).getData()));
        }
        
        //Agregar datos al Bar Chart!
        attributesBar.getData().add(serieDeDatos);
        attributesBar.setLegendVisible(false);
    }
    
    //Inicializa los datos del usuario en la Tabla de atributos
    private void loadDataForAttributeTable(){
        
        col_BPoints.setCellValueFactory(new PropertyValueFactory<>("data"));
        col_subAt.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_at.setCellValueFactory(new PropertyValueFactory<>("name_category"));
        col_fecha.setCellValueFactory(new PropertyValueFactory<>("date_time"));
        col_fuente.setCellValueFactory(new PropertyValueFactory<>("input_source"));
        
        
        
        //Inciializar Observable
        ObservableList<AttributePlayer> table_data = FXCollections.observableArrayList();
        
        for (AttributePlayer attributesAll1 : attributesAll) {
            table_data.add(attributesAll1);
        }
        //Agregar datos a la Tabla !
        attributesTable.setItems(table_data);
       
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
