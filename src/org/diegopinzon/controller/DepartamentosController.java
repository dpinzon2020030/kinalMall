/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

import org.diegopinzon.bean.Departamentos;
import org.diegopinzon.bean.Locales;
import org.diegopinzon.db.Conexion;

import org.diegopinzon.system.Principal;


/*
* @date 18 jun. 2021
* @time 21:08:38
* @author Diego Eduardo Pinzon Rangel
*/
public class DepartamentosController implements Initializable {
    
    private Principal escenarioPrincipal;

    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView ivNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private ImageView ivModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TableView tblDepartamentos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO, REGRESO;
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<Departamentos> listaDepartamentos;

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

    public ObservableList<Departamentos> getDepartamentos() {
        ArrayList<Departamentos> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDepartamentos}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Departamentos registro = new Departamentos();
                registro.setId(rs.getInt("id"));
                registro.setNombre(rs.getString("nombre"));
                lista.add(registro);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(lista);
        return listaDepartamentos;
    }

    
    public void cargarDatos() {
        tblDepartamentos.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombre"));
    }

    public void habilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(true);
    }

    public void seleccionarElemento() {
        try {
            txtId.setText(String.valueOf(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getNombre());
            btnModificar.setDisable(false);
            btnEliminar.setDisable(false);
        } catch (Exception e) {

        }
    }
    
     public void agregarDepartamentos() {
        Departamentos registro = new Departamentos();
        registro.setNombre(txtNombre.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDepartamentos(?)}");
            stmt.setString(1, registro.getNombre());
            //stmt.executeUpdate();
            stmt.execute();
            cargarDatos();
            limpiar();
            deshabilitarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarDepartamentos() {
        Departamentos registro = (Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombre(txtNombre.getText());

        try {
            //CallableStatement stmt
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarDepartamentos(?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNombre());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void eliminarDepartamentos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDepartamentos(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiar() {
        txtId.clear();
        txtNombre.clear();
    }

    public void deshabilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(false);
    }

    public boolean verificarCampos() {
        if ((txtNombre.getText().equals(""))) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO: 
                habilitarCampos();
                limpiar();
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/guardar.png"));
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/eliminar.png"));
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (verificarCampos()) {
                    agregarDepartamentos();
                    cargarDatos();
                    limpiar();
                    btnNuevo.setText("Nuevo");
                    btnModificar.setText("Modificar");
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                    ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/agregar.png"));
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                    operacion = Operaciones.NINGUNO;
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
    private void modificar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO:
                if (verificarCampos()) {
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

                // metodo actualizar
                editarDepartamentos();
                limpiar();
                cargarDatos();
                btnModificar.setText("Modificar");
                btnEliminar.setText("Eliminar");
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;
                break;
            case GUARDAR:
                limpiar();
                deshabilitarCampos();
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
    private void eliminar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case ACTUALIZAR://cancelar
                limpiar();
                deshabilitarCampos();
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setText("Modificar");
                btnEliminar.setText("Eliminar");
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (verificarCampos()) {
                    int i = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas eliminar?", "Respuesta",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (i == 0) {
                        eliminarDepartamentos();
                        limpiar();
                    } else if (i == 1) {
                        limpiar();
                    }
                    deshabilitarCampos();
                    limpiar();
                    cargarDatos();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor,  seleccione un registro.");
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void reporte(ActionEvent event) {

    }

    @FXML
    private void regresarDepartamentos(MouseEvent event) {
        try {
            escenarioPrincipal.menuPrincipal();
        } catch (Exception ex) {
            Logger.getLogger(DepartamentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
