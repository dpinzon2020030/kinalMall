/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;


import org.diegopinzon.bean.Administracion;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;
import org.diegopinzon.report.GenerarReporte;


/*
* @date 4 jun. 2021
* @time 12:34:46
* @author Diego Eduardo Pinzon Rangel
*/
public class AdministracionController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Administracion> listaAdministracion;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    private int validacion = 0;

    private Operaciones operacion = Operaciones.NINGUNO;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TableView tblAdministracion;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private Button btnGuardar;
    @FXML
    private ImageView ivNuevo;
    @FXML
    private ImageView ivModificar;
    @FXML
    private ImageView ivEliminar;

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

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();

        try {
            //CallableStatement stmt;
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                listado.add(new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                )
                );
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }

    public void cargarDatos() {
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));
        deshabilitarCampos();

    }

    @FXML
    public void habilitarCampos() {
        txtId.setEditable(false);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }

    @FXML
    public void seleccionarElemento(MouseEvent event) {
        try {
            txtId.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
            txtDireccion.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Has seleccionado un campo sin datos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void agregarAdministracion() {
        Administracion registro = new Administracion();
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarAdministracion(?,?)}");
            stmt.setString(1, registro.getDireccion());
            stmt.setString(2, registro.getTelefono());
            stmt.execute();
            cargarDatos();
            limpiar();
            deshabilitarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarAdministracion() {
        Administracion registro = (Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarAdministracion(?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDireccion());
            stmt.setString(3, registro.getTelefono());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarAdministracion() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarAdministracion(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal Mall");
                alert.setHeaderText(null);
                alert.setContentText("Se van a eliminar todos los registros con ese codigo.");
                alert.show();
            }
            System.err.println("\nSe produjo un error al intentar eliminar el registro en administracion");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiar() {
        txtId.clear();
        txtDireccion.clear();
        txtTelefono.clear();
    }

    public void deshabilitarCampos() {
        txtId.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }

    public boolean verificacion() {
        return !((txtDireccion.getText().equals("")) || ((txtTelefono.getText().equals(""))));

    }

    public boolean validarTelefono(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{8}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();
    }

    @FXML
    public void regresarAdministracion(MouseEvent event) {
        escenarioPrincipal.menuPrincipal();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
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
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/cancelar.png"));
                break;
            case GUARDAR:
                if (verificacion()) {
                    if (validarTelefono(txtTelefono.getText())) {
                        agregarAdministracion();
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
                        alert.setContentText("Por favor, Ingrese un numero correcto.");
                        alert.show();
                    }

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
                if (verificacion()) {
                    habilitarCampos();
                    btnModificar.setText("Actualizar");
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/listo.png"));
                    btnEliminar.setText("Cancelar");
                    ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/cancelar.png"));
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
                if (verificacion()) {
                    if (validarTelefono(txtTelefono.getText())) {
                        editarAdministracion();
                        cargarDatos();
                        limpiar();
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
                        alert.setContentText("Por favor, Ingrese un numero correcto.");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llenar todos los datos.");
                    alert.show();
                }
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
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                btnEliminar.setText("Eliminar");
                ivEliminar.setImage(new Image("/org/diegopinozn/resource/images/eliminar.png"));
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO://Eliminar
                if (verificacion()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Seguro que desea eliminar?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarAdministracion();
                        cargarDatos();
                        limpiar();
                        deshabilitarCampos();
                    } else if (respuesta.get() == ButtonType.CANCEL) {
                        limpiar();
                        deshabilitarCampos();
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
    public boolean existElementoSeleccionado() {
        return tblAdministracion.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void reporte(ActionEvent event) {
        Map parametros = new HashMap();
        if(existElementoSeleccionado()){
            int codigoAdministracion = ((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getId();
            parametros.put("id", codigoAdministracion);
            GenerarReporte.getInstance().mostrarReporte("ReporteAdministracionPorId.jasper" , parametros ,"Reporte de Adminitracion/Empleados" );
        }else{
            GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper" , parametros ,"Reporte de Adminitracion" );
        }   
    }

    @FXML
    private void mostrarEscenarioTipoCliente(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaTipoCliente();
    }

    @FXML
    void mostrarVistaDepartamentos(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaDepartamentos();
    }

    @FXML
    void mostrarVistaLocales(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaLocales();
    }

    @FXML
    void mostrarVistaCargos(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaCargos();
    }

}
