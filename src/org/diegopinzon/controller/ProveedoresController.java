    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import org.diegopinzon.bean.Proveedores;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;

/*
* @date 14 jul. 2021
* @time 20:59:15
* @author Diego Eduardo Pinzon Rangel
*/
public class ProveedoresController implements Initializable {
    
    private Principal escenarioPrincipal;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<Proveedores> listaProveedores;

    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView ivNuevo;

    @FXML
    private Button btnModificar;

    @FXML
    private ImageView ivModificar;

    @FXML
    private ImageView ivEliminar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNit;

    @FXML
    private TextField txtServicioPrestado;

    @FXML
    private TableView tblProveedores;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNit;

    @FXML
    private TableColumn colServicioPrestado;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colDireccion;

    @FXML
    private TableColumn colSaldoFavor;

    @FXML
    private TableColumn colSaldoContra;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtSaldoFavor;

    @FXML
    private TextField txtSaldoContra;
    
    @FXML
    private TextField txtSaldoLiquido;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
        
        String nit = "";
        if (validarNit(nit)) {
            nit = nit.replace("-", "");
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Proveedores(
                        rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra")
                )
                );
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaProveedores = FXCollections.observableArrayList(lista);
        return listaProveedores;
    }
    
    public boolean validarNit(String nit) {        
        String patron = "^[0-9]{6,}[-]?[0-9]{1}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(nit);

        return matcher.matches();
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtSaldoContra.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtNit.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtNit.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtSaldoLiquido.setEditable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtSaldoContra.clear();
        txtSaldoFavor.clear();
        txtNit.clear();
        txtServicioPrestado.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtSaldoLiquido.clear();
    }
    
    public BigDecimal saldoLiquido() {
        BigDecimal saldoFavor = new BigDecimal(txtSaldoFavor.getText());
        BigDecimal saldoContra = new BigDecimal(txtSaldoContra.getText());
        BigDecimal resultado = saldoFavor.subtract(saldoContra);
        return resultado;
    }
    
    public boolean validarTelefono(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{8}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();
    }

    public boolean verificacion() {
        if((txtNit.getText().equals("")) || (txtServicioPrestado.getText().equals("")) || (txtTelefono.getText().equals(""))
                || (txtDireccion.getText().equals("")) || (txtSaldoFavor.getText().equals("")) || (txtSaldoContra.getText().equals(""))){
            return false;
        }else{
            return true;
        }
    }

    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colId.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("id"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nit"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccion"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoContra"));

        desactivarControles();
    }
    
    public void seleccionarElemento() {
        try {
            txtId.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
            txtNit.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit()));
            txtServicioPrestado.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado()));
            txtTelefono.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono()));
            txtDireccion.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion()));
            txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtSaldoLiquido.setText(String.valueOf(saldoLiquido()));
            
            
        } catch (Exception e) {

        }
    }

    public void agregarProveedores() {
        Proveedores registro = new Proveedores();
        registro.setNit(txtNit.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setSaldoFavor(BigDecimal.valueOf(Double.parseDouble(txtSaldoFavor.getText())));
        registro.setSaldoContra(BigDecimal.valueOf(Double.parseDouble(txtSaldoContra.getText())));

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarProveedores(?,?,?,?,?,?)}");
            stmt.setString(1, registro.getNit());
            stmt.setString(2, registro.getServicioPrestado());
            stmt.setString(3, registro.getTelefono());
            stmt.setString(4, registro.getDireccion());
            stmt.setBigDecimal(5, registro.getSaldoFavor());
            stmt.setBigDecimal(6, registro.getSaldoContra());
            stmt.execute();
            cargarDatos();
            limpiarControles();
            desactivarControles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void editarProveedores() {
        Proveedores registro = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
        registro.setNit(txtNit.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setSaldoFavor(BigDecimal.valueOf(Double.parseDouble(txtSaldoFavor.getText())));
        registro.setSaldoContra(BigDecimal.valueOf(Double.parseDouble(txtSaldoContra.getText())));

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarProveedores(?,?,?,?,?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNit());
            stmt.setString(3, registro.getServicioPrestado());
            stmt.setString(4, registro.getTelefono());
            stmt.setString(5, registro.getDireccion());
            stmt.setBigDecimal(6, registro.getSaldoFavor());
            stmt.setBigDecimal(7, registro.getSaldoContra());
            stmt.execute();
            //System.out.println(admin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarProveedores() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarProveedores(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO: // nuevo
                activarControles();
                limpiarControles();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/guardar.png"));
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/cancelar.png"));
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                break;
            case GUARDAR:
                if (verificacion()) {
                    if (validarNit(txtNit.getText())) {
                        if (validarTelefono(txtTelefono.getText())) {
                            agregarProveedores();
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
                            alert.setContentText("Por favor,ingrese correctamente el telefono");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Kinal Mall");
                        alert.setHeaderText(null);
                        alert.setContentText("Por favor,ingrese correctamente el nit");
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

                    activarControles();
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
                    if (validarNit(txtNit.getText())) {
                        if (validarTelefono(txtTelefono.getText())) {
                            editarProveedores();
                            cargarDatos();
                            limpiarControles();
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
                            alert.setContentText("Por favor,ingrese correctamente el telefono");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Kinal Mall");
                        alert.setHeaderText(null);
                        alert.setContentText("Por favor,ingrese correctamente el nit");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, seleccione un registro.");
                    alert.show();
                }

                break;
            case GUARDAR:
                limpiarControles();
                desactivarControles();
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
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                btnModificar.setText("Modificar");
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                btnEliminar.setText("Eliminar");
                ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/eliminar.png"));
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                
                if (verificacion()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Seguro que desea eliminar?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarProveedores();
                        cargarDatos();
                        limpiarControles();
                        desactivarControles();
                    } else if (respuesta.get() == ButtonType.CANCEL) {
                        limpiarControles();
                        desactivarControles();
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
    void regresarProveedores(MouseEvent event) throws Exception {
        escenarioPrincipal.menuPrincipal();
    }
    
    @FXML
    void mostrarEscenarioCuentasPorPagar(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaCuentasPorPagar();
    }

}
