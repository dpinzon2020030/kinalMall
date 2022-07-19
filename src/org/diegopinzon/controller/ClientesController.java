/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

import org.diegopinzon.bean.Administracion;
import org.diegopinzon.bean.Clientes;
import org.diegopinzon.bean.Locales;
import org.diegopinzon.bean.TipoCliente;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;

/*
* @date 9 jun. 2021
* @time 8:09:42
* @author Diego Eduardo Pinzon Rangel
*/

public class ClientesController implements Initializable {

   private Principal escenarioPrincipal;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colCodigoLocal;
    @FXML
    private TableColumn colCodigoAdministracion;
    @FXML
    private TableColumn colCodigoTipoCliente;
    @FXML
    private ComboBox cmbTipoCliente;
    @FXML
    private ImageView ivNuevo;
    @FXML
    private ImageView ivModificar;

  
    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    private int validacion = 0;
    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Clientes> listaClientes;
    private ObservableList<TipoCliente> listaTipoCliente;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;    
    
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
   
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente}");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));                
            }
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;
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
    
    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();
        
        try{
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
            
        }catch(Exception e){
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }
    
    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try{
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes}");
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()){
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
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;
        
    }
    private TipoCliente buscarTipoCLiente(int id) {
        TipoCliente registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarTipoCliente(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new TipoCliente(rs.getInt("Id"), rs.getString("descripcion"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

     public void activarControles() {
        txtId.setEditable(false);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmail.setEditable(true);
        cmbTipoCliente.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmail.setEditable(false);
        cmbTipoCliente.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtEmail.clear();
        cmbTipoCliente.valueProperty().set(null);
    }

    public boolean existElementoSeleccionado() {
        return tblClientes.getSelectionModel().getSelectedItem() != null;
    }

    public void agregarClientes() {

        Clientes cliente = new Clientes();
        cliente.setNombres(txtNombres.getText());
        cliente.setApellidos(txtApellidos.getText());
        cliente.setDireccion(txtDireccion.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setEmail(txtEmail.getText());
        cliente.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?,?,?,?,?,?)}");
            pstmt.setString(1, cliente.getNombres());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getDireccion());
            pstmt.setString(5, cliente.getEmail());
            pstmt.setInt(6, cliente.getCodigoTipoCliente());
            pstmt.execute();
            cargarDatos();
            limpiarControles();
            desactivarControles();
            listaClientes.add(cliente);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un tipo de cliente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarClientes() {
        Clientes cliente = new Clientes();
        cliente.setId(Integer.parseInt(txtId.getText()));
        cliente.setNombres(txtNombres.getText());
        cliente.setApellidos(txtApellidos.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setDireccion(txtDireccion.getText());
        cliente.setEmail(txtEmail.getText());
        cliente.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarClientes(?,?,?,?,?,?,?)}");
            pstmt.setInt(1, cliente.getId());
            pstmt.setString(2, cliente.getNombres());
            pstmt.setString(3, cliente.getApellidos());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getDireccion());
            pstmt.setString(6, cliente.getEmail());
            pstmt.setInt(7, cliente.getCodigoTipoCliente());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarClientes() {
        if (existElementoSeleccionado()) {
            Clientes cliente = ((Clientes) tblClientes.getSelectionModel().getSelectedItem());
            try {
                PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");
                pstmt.setInt(1, cliente.getId());
                pstmt.execute();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    public boolean validarTelefono(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{8}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();
    }

    public boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    public void seleccionarElemento() {
        if (existElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getId()));
            txtNombres.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombres()));
            txtApellidos.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidos()));
            txtDireccion.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccion()));
            txtTelefono.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefono()));
            txtEmail.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getEmail()));
            cmbTipoCliente.getSelectionModel().select(buscarTipoCLiente(((Clientes) tblClientes.getSelectionModel()
                    .getSelectedItem()).getCodigoTipoCliente()));
        }
    }

     public void cargarDatos() {
        tblClientes.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoTipoCliente"));
        cmbTipoCliente.setItems(getTipoCliente());
        desactivarControles();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: // nuevo
                activarControles();
                limpiarControles();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/guardar.png"));
                ivModificar.setImage(new Image("/org/diegopinzon/resource/images/borrar.png"));
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombres);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtEmail);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if ((validarTelefono(txtTelefono.getText()))) {
                        if (validarEmail(txtEmail.getText())) {
                            agregarClientes();
                            cargarDatos();
                            limpiarControles();
                            operacion = Operaciones.NINGUNO;
                            btnNuevo.setText("Nuevo");
                            btnModificar.setText("Modificar");
                            btnEliminar.setDisable(false);
                            btnReporte.setDisable(false);
                            ivNuevo.setImage(new Image("/org/diegopinzon/resource/images/agregar.png"));
                            ivModificar.setImage(new Image("/org/diegopinzon/resource/images/escribir.png"));
                        } else {
                            //JOptionPane.showMessageDialog(null, "Correo electronico no es valido.");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Kinal Mall");
                            alert.setHeaderText(null);
                            alert.setContentText("Correo electronico no es valido.");
                            alert.show();
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Numero de telefono nos es valido", "Kinal Mall", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos.", "Kinal Mall", JOptionPane.WARNING_MESSAGE);
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
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;

                } else {
                    JOptionPane.showMessageDialog(null, "Antes de continuar seleccione un elemento");
                }
                break;
            case ACTUALIZAR:
                // metodo actualizar
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNombres);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtEmail);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    editarClientes();
                    limpiarControles();
                    cargarDatos();
                    btnModificar.setText("Modificar");
                    btnEliminar.setText("Eliminar");
                    btnNuevo.setDisable(false);
                    btnReporte.setDisable(false);
                    operacion = Operaciones.NINGUNO;
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos");
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
                btnEliminar.setText("Eliminar");
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (existElementoSeleccionado()) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar?", "Kinal Mall",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        eliminarClientes();
                        cargarDatos();
                        limpiarControles();
                        desactivarControles();
                        break;
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        desactivarControles();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Antes de continuar seleccione un registro");
                }
        }
    }

      @FXML
    private void reporte(ActionEvent event) {

    }

    @FXML
    private void regresarClientes(MouseEvent event) {
       try {
           escenarioPrincipal.menuPrincipal();
       } catch (Exception ex) {
           Logger.getLogger(ClientesController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

    @FXML
    private void mostrarEscenarioTipoCliente(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaTipoCliente();

    }


}
