/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import org.diegopinzon.bean.TipoCliente;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;

/*
* @date 18 jun. 2021
* @time 20:46:47
* @author Diego Eduardo Pinzon Rangel
*/
public class TipoClienteController implements Initializable{
    
    
    private Principal escenarioPrincipal;

    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TableView tblTipoCliente;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private ImageView ivNuevo;
    @FXML
    private ImageView ivModificar;
    @FXML
    private ImageView ivEliminar;

    private enum Operaciones {
        NUEVO, NINGUNO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR;
    }

    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<TipoCliente> listaTipoCliente;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;
    }

    public void cargarDatos() {
        tblTipoCliente.setItems(getTipoCliente());
        colId.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
        deshalibilitarCampos();
    }

     @FXML
    public void seleccionarElemento() {
        try {
            txtId.setText(String.valueOf(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId()));
            txtDescripcion.setText(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        } catch (Exception e) {

        }
    }

    public void habilitarCampos() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(true);
    }

    public void deshalibilitarCampos() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    public void limpiar() {
        txtId.clear();
        txtDescripcion.clear();
    }

    public void agregarTipoCliente() {
        TipoCliente registro = new TipoCliente();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarTipoCliente(?)}");
            pstmt.setString(1, registro.getDescripcion());
            pstmt.execute();
            cargarDatos();
            limpiar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editarTipoCliente() {
        TipoCliente registro = (TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarTipoCliente(?,?)}");
            pstmt.setInt(1, registro.getId());
            pstmt.setString(2, registro.getDescripcion());
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarTipoCliente() {
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarTipoCliente(?)}");
            pstmt.setInt(1, Integer.parseInt(txtId.getText()));
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean verificar() {
        return !txtDescripcion.getText().equals("");
    }

    @FXML
    void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                limpiar();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/guardar.png"));
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/eliminar.png"));
                break;
            case GUARDAR:
                if (verificar()) {
                    agregarTipoCliente();
                    operacion = Operaciones.NINGUNO;
                    btnNuevo.setText("Nuevo");
                    btnModificar.setText("Modificar");
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                    ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/agregar.png"));
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llenar todos los datos.");
                    alert.show();
                }
                break;
        }
    }
    
    @FXML
    void modificar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (verificar()) {
                    habilitarCampos();
                    btnModificar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, seleccione un registro.");
                    alert.show();
                }
                break;
            case ACTUALIZAR:
                    if (verificar()) {
                    editarTipoCliente();
                    limpiar();
                    cargarDatos();
                    btnModificar.setText("Modificar");
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                    btnEliminar.setText("Eliminar");
                    ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/eliminar.png"));
                    btnNuevo.setDisable(false);
                    btnReporte.setDisable(false);
                    operacion = Operaciones.NINGUNO;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llenar todos los datos.");
                    alert.show();
                }
            case GUARDAR:
                limpiar();
                deshalibilitarCampos();
                btnNuevo.setText("Nuevo");
                btnModificar.setText("Modificar");
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/agregar.png"));
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                operacion = Operaciones.NINGUNO;
                break;
        }
    }
    
    @FXML
    void eliminar(ActionEvent event) {
        switch (operacion) {
            case ACTUALIZAR:
                limpiar();
                deshalibilitarCampos();
                btnNuevo.setDisable(false);
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setText("Modificar");
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                btnEliminar.setText("Eliminar");
                ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/eliminar.png"));
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (verificar()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿ Seguro que desea eliminar ?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarTipoCliente();
                        cargarDatos();
                        limpiar();
                        deshalibilitarCampos();
                    } else if (respuesta.get() == ButtonType.CANCEL) {
                        limpiar();
                        deshalibilitarCampos();
                    }
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, seleccione un registro.");
                    alert.show();
                }
                break;
        }

    }
    
    @FXML
    void reporte(ActionEvent event) {

    }

    @FXML
    void regresarTipoCliente(MouseEvent event) {
        this.escenarioPrincipal.mostrarEscenaClientes();
    }
}

