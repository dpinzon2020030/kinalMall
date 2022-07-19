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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import org.diegopinzon.bean.Administracion;
import org.diegopinzon.bean.Clientes;
import org.diegopinzon.bean.CuentasPorCobrar;
import org.diegopinzon.bean.Locales;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;


/*
* @date 30 jun. 2021
* @time 10:12:40
* @author Diego Eduardo Pinzon Rangel
*/
public class CuentasPorCobrarController implements Initializable {

     private Principal escenarioPrincipal;
    private ObservableList<CuentasPorCobrar> listaCuentasPorCobrar;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Clientes> listaClientes;
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
    private TableView tblCuentasPorCobrar;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colFactura;

    @FXML
    private TableColumn colAnio;

    @FXML
    private TableColumn colMes;

    @FXML
    private TableColumn colValorNetoPago;

    @FXML
    private TableColumn colEstadoPago;

    @FXML
    private TableColumn colAdministracion;

    @FXML
    private TableColumn colCliente;

    @FXML
    private TableColumn colLocal;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNumeroFactura;

    @FXML
    private Spinner<Integer> spnAnio;
    private SpinnerValueFactory<Integer> valueFactoryAnio;

    @FXML
    private Spinner<Integer> spnMes;
    private SpinnerValueFactory<Integer> valueFactoryMes;

    @FXML
    private ComboBox<Administracion> cmbAdministracion;

    @FXML
    private ComboBox<Clientes> cmbCliente;

    @FXML
    private ComboBox<Locales> cmbLocal;

    @FXML
    private TextField txtValorNetoPago;
    
     @FXML
    private ComboBox cmbEstadoPago;
     
     
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        valueFactoryAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 7);
        spnAnio.setValueFactory(valueFactoryAnio);
        spnMes.setValueFactory(valueFactoryMes);
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
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
            System.err.println("Se produjo un error al consultar la tabla administracion en la base de datos.");
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
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
        } catch (SQLException e) {
            System.err.println("Se produjo un error al consultar la tabla locales en la base de datos.");
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }
         
    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<Clientes>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("codigoTipoCliente")
                )
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Se produjo un error al consultar la tabla clientes en la base de datos.");
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }    
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;
    }
    
    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar(){
        ArrayList<CuentasPorCobrar> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt= Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorCobrar}");
            rs = pstmt.executeQuery();
            while(rs.next()){
                lista.add(new CuentasPorCobrar(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getInt("anio"),
                        rs.getInt("mes"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getString("estadoPago"),
                        rs.getInt("codigoAdministracion"),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoLocal")
                )
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Se produjo un error al consultar la tabla cuentas por cobrar en la base de datos.");
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
        }
        listaCuentasPorCobrar = FXCollections.observableArrayList(lista);
        return listaCuentasPorCobrar;
    }
    
    public ObservableList cargarComboVox(){
        ArrayList<String> estaPago = new ArrayList<>();
        estaPago.add("Pendiente");
        estaPago.add("Cancelado");
        listaEstadoPago = FXCollections.observableArrayList(estaPago);
        return listaEstadoPago;
    }
    
    public void cargarDatos(){
        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,Integer>("id"));
        colFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,Integer>("mes"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,BigDecimal>("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,String>("estadoPago"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,Integer>("codigoAdministracion"));
        colCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,Integer>("codigoCliente"));
        colLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar,Integer>("codigoLocal"));
        cmbAdministracion.setItems(getAdministracion());
        cmbCliente.setItems(getClientes());
        cmbLocal.setItems(getLocales());
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
    private Clientes buscarClientes(int id) {
        Clientes registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarClientes(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Clientes(
                        rs.getInt("Id"), 
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("codigoTipoCliente")
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }
    
    private Locales buscarLocales(int id) {
        Locales registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarLocales(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Locales(
                        rs.getInt("Id"), 
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }
    
    public CuentasPorCobrar buscarCuentasPorCobrar(int id) {
        CuentasPorCobrar cuentasPorCobrar = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarCuentasPorCobrar(?)");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cuentasPorCobrar = new CuentasPorCobrar(
                        rs.getInt("id"),
                        rs.getString("numeroFactura"),
                        rs.getInt("anio"),
                        rs.getInt("mes"),
                        rs.getBigDecimal("valorNetoPago"),
                        rs.getString("estadoPago"),
                        rs.getInt("codigoAdministracion"),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoLocal")
                );
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
        return cuentasPorCobrar;
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura() );
            spnAnio.getValueFactory().setValue( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio() );
            spnMes.getValueFactory().setValue( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            txtValorNetoPago.setText(String.valueOf( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago() ));
            cmbEstadoPago.getSelectionModel().select(buscarCuentasPorCobrar(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().
                    getSelectedItem()).getId()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCliente.getSelectionModel().select(buscarClientes(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            cmbLocal.getSelectionModel().select(buscarLocales(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoLocal()));
        }
        
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(true);
        cmbEstadoPago.setDisable(false);
        txtValorNetoPago.setEditable(true);
        cmbAdministracion.setDisable(false);
        cmbCliente.setDisable(false);
        cmbLocal.setDisable(false);
        spnAnio.setDisable(false);
        spnMes.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(false);
        cmbEstadoPago.setDisable(true);
        txtValorNetoPago.setEditable(false);
        cmbAdministracion.setDisable(true);
        cmbCliente.setDisable(true);
        cmbLocal.setDisable(true);
        spnAnio.setDisable(true);
        spnMes.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNumeroFactura.clear();
        cmbEstadoPago.valueProperty().set(null);
        txtValorNetoPago.clear();
        cmbAdministracion.valueProperty().set(null);
        cmbCliente.valueProperty().set(null);
        cmbLocal.valueProperty().set(null);
        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);
    }
    
    public void eliminarCuentasPorCobrar() {
        if (existElementoSeleccionado()) {
            CuentasPorCobrar cuentaCobrar = (CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem();
            
            System.out.println(cuentaCobrar);
            
            PreparedStatement pstmt = null;
            
            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorCobrar(?)}");
                pstmt.setInt(1, cuentaCobrar.getId());
                
                System.out.println(pstmt);
                
                pstmt.execute();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kinal Mall");
                alert.setHeaderText(null);
                alert.setContentText("Registro eliminado exitosamente");
                alert.show();
                
            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar el registro con el id " + cuentaCobrar.getId());
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
    
    public void editarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setId(Integer.parseInt(txtId.getText()));
        cuentaCobrar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNetoPago.getText()));
        cuentaCobrar.setEstadoPago(String.valueOf(cmbEstadoPago.getSelectionModel().getSelectedItem()));
        cuentaCobrar.setCodigoAdministracion(((Administracion)cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            
            pstmt.setInt(1, cuentaCobrar.getId());
            
            pstmt.setString(2, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(3, cuentaCobrar.getAnio());
            pstmt.setInt(4, cuentaCobrar.getMes());
            pstmt.setBigDecimal(5, cuentaCobrar.getValorNetoPago());
            pstmt.setString(6, cuentaCobrar.getEstadoPago());
            pstmt.setInt(7, cuentaCobrar.getCodigoAdministracion());
            pstmt.setInt(8, cuentaCobrar.getCodigoCliente());
            pstmt.setInt(9, cuentaCobrar.getCodigoLocal());
            
            System.out.println(pstmt);
            
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
    
    public void agregarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNetoPago.getText()));
        cuentaCobrar.setEstadoPago(String.valueOf(cmbEstadoPago.getSelectionModel().getSelectedItem()));
        cuentaCobrar.setCodigoAdministracion(  ((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente( ((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal( ((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId() );
        
        System.out.println(cuentaCobrar);
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(2, cuentaCobrar.getAnio());
            pstmt.setInt(3, cuentaCobrar.getMes());
            pstmt.setBigDecimal(4, cuentaCobrar.getValorNetoPago());
            pstmt.setString(5, cuentaCobrar.getEstadoPago());
            pstmt.setInt(6, cuentaCobrar.getCodigoAdministracion());
            pstmt.setInt(7, cuentaCobrar.getCodigoCliente());
            pstmt.setInt(8, cuentaCobrar.getCodigoLocal());
            
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
        return tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null;
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

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbLocal);
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbEstadoPago);
                
                ArrayList<Spinner> listaSpinner = new ArrayList<>();
                listaSpinner.add(spnAnio);
                listaSpinner.add(spnMes);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    agregarCuentasPorCobrar();
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
                    alert.setTitle("Kinal Mall");
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
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un elemento");
                    alert.show();
                    
                }
                break;
            case ACTUALIZAR:
                // metodo actualizar
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNetoPago);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbLocal);
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbEstadoPago);
                
                ArrayList<Spinner> listaSpinner = new ArrayList<>();
                listaSpinner.add(spnAnio);
                listaSpinner.add(spnMes);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    editarCuentasPorCobrar();
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
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llene todos los campos.");
                    alert.show();
                }

                break;

            case GUARDAR://cancelar cuando cancela una insercion
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
            case ACTUALIZAR:
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
                if (existElementoSeleccionado()) {
                    /*int respuesta1 = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar?", "Kinal Mall",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    
                    if (respuesta1 == JOptionPane.YES_OPTION) {
                        //eliminarClientes();
                        //cargarDatos();
                        limpiarControles();
                        desactivarControles();
                        break;
                    } else if (respuesta1 == JOptionPane.NO_OPTION) {
                        limpiarControles();
                        desactivarControles();
                    }*/
                    
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿ Seguro que desea eliminar ?");
                    
                 
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if(respuesta.get()== ButtonType.OK){
                        eliminarCuentasPorCobrar();
                        cargarDatos();
                        limpiarControles();
                        desactivarControles();
                    }else if(respuesta.get()== ButtonType.CANCEL){
                        limpiarControles();
                        desactivarControles();
                    }
                    break;
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }
        }
    }
    
    @FXML
    void regresarCuentasPorCobrar(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaClientes();
    }

    @FXML
    void reporte(ActionEvent event) {

    }
    
}
