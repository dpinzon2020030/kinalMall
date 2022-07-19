/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import org.diegopinzon.bean.Locales;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;


/*
* @date 18 jun. 2021
* @time 20:57:20
* @author Diego Eduardo Pinzon Rangel
*/
public class LocalesController implements Initializable {
    
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
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TableView tblLocales;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtMesesPendientes;
    @FXML
    private TextField txtValorAdministracion;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private CheckBox cbDisponibilidad;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TableColumn colMesesPendientes;
    @FXML
    private TableColumn colDisponibilidad;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministracion;
    @FXML
    private ImageView ivNuevo;
    @FXML
    private ImageView ivModificar;
    @FXML
    private ImageView ivEliminar;
    @FXML
    private TextField txtLocalesDisponibles;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    
    private boolean respuesta;
    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Locales> listaDisponibilidad;
    private int contador = 0;

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

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Locales registro = new Locales();
                registro.setId(rs.getInt("id"));
                registro.setSaldoFavor(rs.getBigDecimal("saldoFavor"));
                registro.setSaldoContra(rs.getBigDecimal("saldoContra"));
                registro.setMesesPendientes(rs.getInt("mesesPendientes"));
                registro.setDisponibilidad(rs.getBoolean("disponibilidad"));
                registro.setValorLocal(rs.getBigDecimal("valorLocal"));
                registro.setValorAdministracion(rs.getBigDecimal("valorAdministracion"));
                lista.add(registro);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }
    
    public int LocalesDisponibilidad() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            rs = pstmt.executeQuery();
            contador = contador - contador;
            while (rs.next()) {
                if (rs.getBoolean(5) == true) {
                    contador++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contador;
    }

    public void cargarDatos() {
        tblLocales.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorAdministracion"));
        txtLocalesDisponibles.setText(String.valueOf(LocalesDisponibilidad()));
        txtLocalesDisponibles.setText(String.valueOf(LocalesDisponibilidad()));
        deshabilitarCampos();
    }

    public void habilitarCampos() {
        txtId.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtMesesPendientes.setEditable(true);
        cbDisponibilidad.setDisable(false);
        txtValorLocal.setEditable(true);
        txtValorAdministracion.setEditable(true);
        txtLocalesDisponibles.setEditable(false);
    }

    public void seleccionarElemento() {
        try {
            txtId.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId()));
            txtSaldoFavor.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtMesesPendientes.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            cbDisponibilidad.setSelected(Boolean.parseBoolean(String.valueOf(((Locales) tblLocales.getSelectionModel()
                    .getSelectedItem()).getDisponibilidad())));
            txtValorLocal.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministracion.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
            txtSaldoLiquido.setText(String.valueOf(saldoLiquido()));
            txtLocalesDisponibles.setText(String.valueOf(LocalesDisponibilidad()));
        } catch (Exception e) {

        }
    }

    public BigDecimal saldoLiquido() {
        BigDecimal saldoFavor = new BigDecimal(txtSaldoFavor.getText());
        BigDecimal saldoContra = new BigDecimal(txtSaldoContra.getText());
        BigDecimal resultado = saldoFavor.subtract(saldoContra);
        return resultado;
    }

    public boolean checkBox() {
        if (cbDisponibilidad.isSelected() == false) {
            return false;

        } else if (cbDisponibilidad.isSelected() == true) {
            return true;
        }
        return respuesta;
    }

    public void camposVacios() {
        if (txtSaldoFavor.getText().equals("") || txtSaldoContra.getText().equals("") || txtMesesPendientes.getText().equals("")) {
            if (txtSaldoFavor.getText().equals("")) {
                String saldoFavor = txtSaldoFavor.getText();
                saldoFavor = saldoFavor.replace("", "0");
                txtSaldoFavor.setText(saldoFavor);
            }
            if (txtSaldoContra.getText().equals("")) {
                String saldoContra = txtSaldoContra.getText();
                saldoContra = saldoContra.replace("", "0");
                txtSaldoContra.setText(saldoContra);
            }
            if (txtMesesPendientes.getText().equals("")) {
                String mesesPendientes = txtMesesPendientes.getText();
                mesesPendientes = mesesPendientes.replace("", "0");
                txtMesesPendientes.setText(mesesPendientes);
            }
        }
    }
    
    public void agregarLocales() {
        Locales registro = new Locales();
        if ((txtSaldoFavor.getText().equals("")) || (txtSaldoContra.getText().equals("")) || (txtMesesPendientes.getText().equals(""))) {
            camposVacios();
            registro.setSaldoFavor(BigDecimal.valueOf(Double.parseDouble(txtSaldoFavor.getText())));
            registro.setSaldoContra(BigDecimal.valueOf(Double.parseDouble(txtSaldoContra.getText())));
            registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
            registro.setDisponibilidad(Boolean.parseBoolean(String.valueOf(checkBox())));
            registro.setValorLocal(BigDecimal.valueOf(Double.parseDouble(txtValorLocal.getText())));
            registro.setValorAdministracion(BigDecimal.valueOf(Double.parseDouble(txtValorAdministracion.getText())));
        } else {
            registro.setSaldoFavor(BigDecimal.valueOf(Double.parseDouble(txtSaldoFavor.getText())));
            registro.setSaldoContra(BigDecimal.valueOf(Double.parseDouble(txtSaldoContra.getText())));
            registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
            registro.setDisponibilidad(Boolean.parseBoolean(String.valueOf(checkBox())));
            registro.setValorLocal(BigDecimal.valueOf(Double.parseDouble(txtValorLocal.getText())));
            registro.setValorAdministracion(BigDecimal.valueOf(Double.parseDouble(txtValorAdministracion.getText())));
        }

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarLocales(?,?,?,?,?,?)}");
            stmt.setBigDecimal(1, registro.getSaldoFavor());
            stmt.setBigDecimal(2, registro.getSaldoContra());
            stmt.setInt(3, registro.getMesesPendientes());
            stmt.setBoolean(4, registro.getDisponibilidad());
            stmt.setBigDecimal(5, registro.getValorLocal());
            stmt.setBigDecimal(6, registro.getValorAdministracion());
            //stmt.executeUpdate();
            stmt.execute();
            cargarDatos();
            limpiar();
            deshabilitarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarLocales() {
        Locales registro = (Locales) tblLocales.getSelectionModel().getSelectedItem();
        if ((txtSaldoFavor.getText().equals("")) || (txtSaldoContra.getText().equals("")) || (txtMesesPendientes.getText().equals(""))) {
            camposVacios();
            registro.setSaldoFavor(BigDecimal.valueOf(Double.parseDouble(txtSaldoFavor.getText())));
            registro.setSaldoContra(BigDecimal.valueOf(Double.parseDouble(txtSaldoContra.getText())));
            registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
            registro.setDisponibilidad(Boolean.parseBoolean(String.valueOf(checkBox())));
            registro.setValorLocal(BigDecimal.valueOf(Double.parseDouble(txtValorLocal.getText())));
            registro.setValorAdministracion(BigDecimal.valueOf(Double.parseDouble(txtValorAdministracion.getText())));
        } else {
            registro.setSaldoFavor(BigDecimal.valueOf(Double.parseDouble(txtSaldoFavor.getText())));
            registro.setSaldoContra(BigDecimal.valueOf(Double.parseDouble(txtSaldoContra.getText())));
            registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
            registro.setDisponibilidad(Boolean.parseBoolean(String.valueOf(checkBox())));
            registro.setValorLocal(BigDecimal.valueOf(Double.parseDouble(txtValorLocal.getText())));
            registro.setValorAdministracion(BigDecimal.valueOf(Double.parseDouble(txtValorAdministracion.getText())));
        }

        PreparedStatement stmt = null;
        try {
            //CallableStatement stmt

            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarLocales(?,?,?,?,?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setBigDecimal(2, registro.getSaldoFavor());
            stmt.setBigDecimal(3, registro.getSaldoContra());
            stmt.setInt(4, registro.getMesesPendientes());
            stmt.setBoolean(5, registro.getDisponibilidad());
            stmt.setBigDecimal(6, registro.getValorLocal());
            stmt.setBigDecimal(7, registro.getValorAdministracion());
            stmt.execute();
            //System.out.println(admin);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void eliminarLocales() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarLocales(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Se van a eliminar todos los registros con ese codigo.");
                alert.show();
            }
            System.err.println("\nSe produjo un error al intentar eliminar el registro en locales");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiar() {
        txtId.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
        cbDisponibilidad.setSelected(false);
        txtValorLocal.clear();
        txtValorAdministracion.clear();
        txtSaldoLiquido.clear();
    }

    public void deshabilitarCampos() {
        txtId.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        cbDisponibilidad.setDisable(true);
        txtValorLocal.setEditable(false);
        txtValorAdministracion.setEditable(false);
        txtSaldoLiquido.setEditable(false);
        txtLocalesDisponibles.setEditable(false);
    }

    public boolean verificacion() {
        if ((txtValorLocal.getText().equals("")) || (txtValorAdministracion.getText().equals("")) || (txtMesesPendientes.getText().equals(""))) {
            return false;
        } else {
            return true;
        }

    }

    @FXML
    public void regresarAdministracion(MouseEvent event) throws Exception {
        escenarioPrincipal.menuPrincipal();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        switch (operacion) {
            case NINGUNO: // nuevo
                habilitarCampos();
                limpiar();
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
                    agregarLocales();
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
                //metodo actualizar
                if (verificacion()) {
                    editarLocales();
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
                    alert.setContentText("Por favor, llene todos los campos.");
                    alert.show();
                }
                break;
            case GUARDAR://cancelar cuando cancela una insercion
                limpiar();
                deshabilitarCampos();
                btnNuevo.setText("Nuevo");
                btnModificar.setText("Modificar");
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/agregar.png"));
                ivModificar.setImage(new Image("/org/diegopinzin/resource/images/escribir.png"));
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
                ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/eliminar.png"));
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
                        eliminarLocales();
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

    @FXML
    private void reporte(ActionEvent event) {

    }

    @FXML
    private void regresarLocales(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaAdministracion();
    }
    
}
