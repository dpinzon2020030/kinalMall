/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegopinzon.controller;

import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.diegopinzon.bean.Administracion;
import org.diegopinzon.bean.CuentasPorPagar;
import org.diegopinzon.bean.Proveedores;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;


/**
 * FXML Controller class
 *
 * @author Diego Pinzon
 */
public class CuentasPorPagarController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<String> listaEstadoPago;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    private Operaciones operacion = Operaciones.NINGUNO;

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
    private ImageView ivEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNumeroFactura;

    @FXML
    private ComboBox<Administracion> cmbAdministracion;

    @FXML
    private ComboBox<Proveedores> cmbProveedores;

    @FXML
    private TextField txtValorNetoPago;

    @FXML
    private JFXDatePicker dtpFechaLimite;

    @FXML
    private TableView tblCuentasPorPagar;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNumeroFactura;

    @FXML
    private TableColumn colFechaLimite;

    @FXML
    private TableColumn colEstadoPago;

    @FXML
    private TableColumn colValorPago;

    @FXML
    private TableColumn colAdministracion;

    @FXML
    private TableColumn colProveedores;
    
    @FXML
    private ComboBox cmbEstadoPago;

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

    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {

            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            resultado = stmt.executeQuery();

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
            System.err.println("Se produjo un error al consultar la tabla administracion en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                resultado.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores()}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Proveedores registro = new Proveedores();
                registro.setId(rs.getInt("id"));
                registro.setNit(rs.getString("nit"));
                registro.setServicioPrestado(rs.getString("servicioPrestado"));
                registro.setTelefono(rs.getString("telefono"));
                registro.setDireccion(rs.getString("direccion"));
                registro.setSaldoFavor(rs.getBigDecimal("saldoFavor"));
                registro.setSaldoContra(rs.getBigDecimal("saldoContra"));
                lista.add(registro);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Se produjo un error al consultar la tabla locales en la base de datos.");
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
        listaProveedores = FXCollections.observableArrayList(lista);
        return listaProveedores;
    }

    public ObservableList<CuentasPorPagar> getCuentasPorPagar() {
        ArrayList<CuentasPorPagar> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorPagar()}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new CuentasPorPagar(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getDate("fechaLimitePago"),
                        rs.getString("estadoPago"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getInt("codigoAdministracion"),
                        rs.getInt("codigoProveedor")
                )
                );
            }

        } catch (SQLException e) {
            System.err.println("Se produjo un error al consultar la tabla cuentas por pagar en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        listaCuentasPorPagar = FXCollections.observableArrayList(lista);
        return listaCuentasPorPagar;
    }
    public ObservableList cargarComboVox(){
        ArrayList<String> estaPago = new ArrayList<>();
        estaPago.add("Pendiente");
        estaPago.add("Cancelado");
        listaEstadoPago = FXCollections.observableArrayList(estaPago);
        return listaEstadoPago;
    }

    public void cargarDatos() {
        tblCuentasPorPagar.setItems(getCuentasPorPagar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("id"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFechaLimite.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("estadoPago"));
        colValorPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, BigDecimal>("valorNetoPago"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoAdministracion"));
        colProveedores.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoProveedor"));
        cmbAdministracion.setItems(getAdministracion());
        cmbProveedores.setItems(getProveedores());
        cmbEstadoPago.setItems(cargarComboVox());
        desactivarControles();
    }

    private Administracion buscarAdministracion(int id) {
        Administracion registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Administracion(
                        rs.getInt("Id"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

    public Proveedores buscarProveedores(int id) {
        Proveedores proveedor = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarProveedores(?)");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                proveedor = new Proveedores(
                        rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"));
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar un Proveedor con el id " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return proveedor;
    }
    public CuentasPorPagar buscarCuentasPorPagar(int id) {
        CuentasPorPagar cuentasPorPagar = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarCuentasPorPagar(?)");
            pstmt.setInt(1, id);
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cuentasPorPagar = new CuentasPorPagar(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getDate("fechaLimitePago"),
                        rs.getString("estadoPago"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getInt("codigoAdministracion"),
                        rs.getInt("codigoProveedor"));
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar un cuentas por cobrar con el id " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cuentasPorPagar;
    }


    @FXML
    public void seleccionarElemento() {
        if (existElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            dtpFechaLimite.setValue(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago().toLocalDate());
            cmbEstadoPago.getSelectionModel().select(buscarCuentasPorPagar(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getId()));
            txtValorNetoPago.setText(String.valueOf(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbProveedores.getSelectionModel().select(buscarProveedores(((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            
        }

    }

    public void activarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(true);
        dtpFechaLimite.setDisable(false);
        cmbEstadoPago.setDisable(false);
        txtValorNetoPago.setEditable(true);
        cmbAdministracion.setDisable(false);
        cmbProveedores.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(false);
        dtpFechaLimite.setDisable(true);
        cmbEstadoPago.setDisable(true);
        txtValorNetoPago.setEditable(false);
        cmbAdministracion.setDisable(true);
        cmbProveedores.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNumeroFactura.clear();
        dtpFechaLimite.getEditor().clear();
        cmbEstadoPago.valueProperty().set(null);
        txtValorNetoPago.clear();
        cmbAdministracion.valueProperty().set(null);
        cmbProveedores.valueProperty().set(null);

    }

    public void eliminarCuentasPorPagar() {
        if (existElementoSeleccionado()) {
            CuentasPorPagar cuentaPagar = (CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem();


            PreparedStatement pstmt = null;

            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorPagar(?)}");
                pstmt.setInt(1, cuentaPagar.getId());
                pstmt.execute();
            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar el registro con el id " + cuentaPagar.getId());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean validarNumeroReal(String numero) {
        String patron = "^[0-9]{1,8}([.][0-9]{2})?";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public void editarCuentasPorPagar() {
        CuentasPorPagar cuentaPagar = new CuentasPorPagar();
        cuentaPagar.setId(Integer.parseInt(txtId.getText()));
        cuentaPagar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaPagar.setFechaLimitePago(Date.valueOf(dtpFechaLimite.getValue()));
        cuentaPagar.setEstadoPago(String.valueOf(cmbEstadoPago.getSelectionModel().getSelectedItem()));
        cuentaPagar.setValorNetoPago(new BigDecimal(txtValorNetoPago.getText()));
        cuentaPagar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaPagar.setCodigoProveedor(((Proveedores) cmbProveedores.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorPagar(?, ?, ?, ?, ?, ?, ?)}");

            pstmt.setInt(1, cuentaPagar.getId());
            pstmt.setString(2, cuentaPagar.getNumeroFactura());
            pstmt.setDate(3, cuentaPagar.getFechaLimitePago());
            pstmt.setString(4, cuentaPagar.getEstadoPago());
            pstmt.setBigDecimal(5, cuentaPagar.getValorNetoPago());
            pstmt.setInt(6, cuentaPagar.getCodigoAdministracion());
            pstmt.setInt(7, cuentaPagar.getCodigoProveedor());
            pstmt.execute();

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar editar una Cuenta por cobrar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void agregarCuentasPorPagar() {
        CuentasPorPagar cuentaPagar = new CuentasPorPagar();
        cuentaPagar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaPagar.setFechaLimitePago(Date.valueOf(dtpFechaLimite.getValue()));
        cuentaPagar.setEstadoPago(String.valueOf(cmbEstadoPago.getSelectionModel().getSelectedItem()));
        cuentaPagar.setValorNetoPago(new BigDecimal(txtValorNetoPago.getText()));
        cuentaPagar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaPagar.setCodigoProveedor(((Proveedores) cmbProveedores.getSelectionModel().getSelectedItem()).getId());
        

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorPagar(?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, cuentaPagar.getNumeroFactura());
            pstmt.setDate(2, cuentaPagar.getFechaLimitePago());
            pstmt.setString(3, cuentaPagar.getEstadoPago());
            pstmt.setBigDecimal(4, cuentaPagar.getValorNetoPago());
            pstmt.setInt(5, cuentaPagar.getCodigoAdministracion());
            pstmt.setInt(6, cuentaPagar.getCodigoProveedor());

            System.out.println(pstmt.toString());

            pstmt.execute();

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar agregar un nueva Cuenta por cobrar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean existElementoSeleccionado() {
        return tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
                activarControles();
                limpiarControles();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/guardar.png"));
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/cancelar.png"));
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNetoPago);

                ArrayList<JFXDatePicker> listaDatePicker = new ArrayList<>();
                listaDatePicker.add(dtpFechaLimite);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbProveedores);
                listaComboBox.add(cmbAdministracion);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    agregarCuentasPorPagar();
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                    operacion = Operaciones.NINGUNO;
                    btnNuevo.setText("Nuevo");
                    btnModificar.setText("Modificar");
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                    ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/agregar.png"));
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llene todos los campos.");
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (existElementoSeleccionado()) {
                    activarControles();
                    btnModificar.setText("Actualizar");
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/listo.png"));
                    btnEliminar.setText("Cancelar");
                    ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/cancelar.png"));
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;

                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un elemento");
                    alert.show();

                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNetoPago);

                ArrayList<JFXDatePicker> listaDatePicker = new ArrayList<>();
                listaDatePicker.add(dtpFechaLimite);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbProveedores);
                listaComboBox.add(cmbAdministracion);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarNumeroReal(txtValorNetoPago.getText())) {
                        editarCuentasPorPagar();
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
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("kinal Mall");
                        alert.setHeaderText(null);
                        alert.setContentText("Por favor, ingrese un numero valido.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llene todos los campos.");
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
            case NINGUNO://Eliminar
                if (existElementoSeleccionado()) {

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Seguro que desea eliminar?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarCuentasPorPagar();
                        cargarDatos();
                        limpiarControles();
                        desactivarControles();
                    } else if (respuesta.get() == ButtonType.CANCEL) {
                        limpiarControles();
                        desactivarControles();
                    }
                    break;
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }
        }
    }

    @FXML
    void regresarCuentasPorPagar(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaProveedores();
    }

    @FXML
    void reporte(ActionEvent event) {

    }

}
