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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.diegopinzon.bean.Administracion;
import org.diegopinzon.bean.Cargos;
import org.diegopinzon.bean.Departamentos;
import org.diegopinzon.bean.Empleados;
import org.diegopinzon.bean.Horarios;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;
/*
* @date 16 jul. 2021
* @time 18:45:49
* @author Diego Eduardo Pinzon Rangel
*/
public class EmpleadosController implements Initializable {

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
    private ImageView ivEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Departamentos> cmbDepartamento;

    @FXML
    private ComboBox<Cargos> cmbCargo;

    @FXML
    private TextField txtSueldo;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefono;

    @FXML
    private ComboBox<Administracion> cmbAdministracion;

    @FXML
    private ComboBox<Horarios> cmbHorarios;

    @FXML
    private JFXDatePicker dtpFechaContratacion;

    @FXML
    private TableView tblEmpleados;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNombre;

    @FXML
    private TableColumn colApellido;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colContratacion;

    @FXML
    private TableColumn colSueldo;

    @FXML
    private TableColumn colDepartamentos;

    @FXML
    private TableColumn colCargos;

    @FXML
    private TableColumn colHorarios;

    @FXML
    private TableColumn colAdministracion;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<Cargos> listaCargos;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Horarios> listaHorarios;
    private ObservableList<Departamentos> listaDepartamentos;
    private ObservableList<Empleados> listaEmpleados;

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

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarEmpleados}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Empleados registro = new Empleados();
                registro.setId(rs.getInt("id"));
                registro.setNombres(rs.getString("nombres"));
                registro.setApellidos(rs.getString("apellidos"));
                registro.setEmail(rs.getString("email"));
                registro.setTelefono(rs.getString("telefono"));
                registro.setFechaContratacion(rs.getDate("fechaContratacion"));
                registro.setSueldo(rs.getBigDecimal("sueldo"));
                registro.setCodigoDepartamento(rs.getInt("codigoDepartamento"));
                registro.setCodigoCargo(rs.getInt("codigoCargo"));
                registro.setCodigoHorario(rs.getInt("codigoHorario"));
                registro.setCodigoAdministracion(rs.getInt("codigoAdministracion"));
                lista.add(registro);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaEmpleados = FXCollections.observableArrayList(lista);
        return listaEmpleados;
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

    public ObservableList<Cargos> getCargos() {
        ArrayList<Cargos> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cargos registro = new Cargos();
                registro.setId(rs.getInt("id"));
                registro.setNombre(rs.getString("nombre"));
                lista.add(registro);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaCargos = FXCollections.observableArrayList(lista);
        return listaCargos;
    }

    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")
                )
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("\n se produjo un error al intentar consultar la lista horarios.");
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
        listaHorarios = FXCollections.observableArrayList(lista);
        return listaHorarios;
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

    private Administracion buscarAdministracion(int id) {
        Administracion registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Administracion(
                        rs.getInt("id"),
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

    private Departamentos buscarDepartamentos(int id) {
        Departamentos registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarDepartamentos(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Departamentos(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

    private Horarios buscarHorarios(int id) {
        Horarios registro = null;
        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarHorarios(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

    private Cargos buscarCargos(int id) {
        Cargos registro = null;
        PreparedStatement pstmt = null;
        try {

            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarCargos(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new Cargos(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registro;
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombres"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Empleados, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefono"));
        colContratacion.setCellValueFactory(new PropertyValueFactory<Empleados, Date>("fechaContratacion"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, BigDecimal>("sueldo"));
        colDepartamentos.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoDepartamento"));
        colCargos.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargo"));
        colHorarios.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoHorario"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoAdministracion"));
        cmbDepartamento.setItems(getDepartamentos());
        cmbCargo.setItems(getCargos());
        cmbHorarios.setItems(getHorarios());
        cmbAdministracion.setItems(getAdministracion());
        deshabilitarCampos();
    }

    public boolean validarNombres(String nombres) {
        String patron = "^\\D*$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(patron);
        return matcher.matches();
    }

    public void habilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtEmail.setEditable(true);
        txtTelefono.setEditable(true);
        dtpFechaContratacion.setDisable(false);
        txtSueldo.setEditable(true);
        cmbDepartamento.setDisable(false);
        cmbCargo.setDisable(false);
        cmbHorarios.setDisable(false);
        cmbAdministracion.setDisable(false);
    }

    public void limpiar() {
        txtId.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtEmail.clear();
        txtTelefono.clear();
        dtpFechaContratacion.getEditor().clear();
        txtSueldo.clear();
        cmbDepartamento.valueProperty().set(null);
        cmbCargo.valueProperty().set(null);
        cmbHorarios.valueProperty().set(null);
        cmbAdministracion.valueProperty().set(null);
    }

    public void deshabilitarCampos() {
        txtId.setEditable(false);
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtEmail.setEditable(false);
        txtTelefono.setEditable(false);
        dtpFechaContratacion.setDisable(true);
        txtSueldo.setEditable(false);
        cmbDepartamento.setDisable(true);
        cmbCargo.setDisable(true);
        cmbHorarios.setDisable(true);
        cmbAdministracion.setDisable(true);
    }

    public void seleccionarElemento() {
        try {
            txtId.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombres()));
            txtApellidos.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidos()));
            txtEmail.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmail()));
            txtTelefono.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefono()));
            dtpFechaContratacion.setValue(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getFechaContratacion().toLocalDate());
            txtSueldo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
            cmbDepartamento.getSelectionModel().select(buscarDepartamentos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            cmbCargo.getSelectionModel().select(buscarCargos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            cmbHorarios.getSelectionModel().select(buscarHorarios(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
        } catch (Exception e) {

        }
    }

    public void agregarEmpleados() {
        Empleados registro = new Empleados();
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setEmail(txtEmail.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setFechaContratacion(Date.valueOf(dtpFechaContratacion.getValue()));
        registro.setSueldo(new BigDecimal(txtSueldo.getText()));
        registro.setCodigoDepartamento(((Departamentos) cmbDepartamento.getSelectionModel().getSelectedItem()).getId());
        registro.setCodigoCargo(((Cargos) cmbCargo.getSelectionModel().getSelectedItem()).getId());
        registro.setCodigoHorario(((Horarios) cmbHorarios.getSelectionModel().getSelectedItem()).getId());
        registro.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarEmpleados(?,?,?,?,?,?,?,?,?,?)}");
            stmt.setString(1, registro.getNombres());
            stmt.setString(2, registro.getApellidos());
            stmt.setString(3, registro.getEmail());
            stmt.setString(4, registro.getTelefono());
            stmt.setDate(5, registro.getFechaContratacion());
            stmt.setBigDecimal(6, registro.getSueldo());
            stmt.setInt(7, registro.getCodigoDepartamento());
            stmt.setInt(8, registro.getCodigoCargo());
            stmt.setInt(9, registro.getCodigoHorario());
            stmt.setInt(10, registro.getCodigoAdministracion());
            stmt.execute();
            cargarDatos();
            limpiar();
            deshabilitarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarEmpleados() {
        Empleados registro = (Empleados) tblEmpleados.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setEmail(txtEmail.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setFechaContratacion(Date.valueOf(dtpFechaContratacion.getValue()));
        registro.setSueldo(new BigDecimal(txtSueldo.getText()));
        registro.setCodigoDepartamento(((Departamentos) cmbDepartamento.getSelectionModel().getSelectedItem()).getId());
        registro.setCodigoCargo(((Cargos) cmbCargo.getSelectionModel().getSelectedItem()).getId());
        registro.setCodigoHorario(((Horarios) cmbHorarios.getSelectionModel().getSelectedItem()).getId());
        registro.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarEmpleados(?,?,?,?,?,?,?,?,?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNombres());
            stmt.setString(3, registro.getApellidos());
            stmt.setString(4, registro.getEmail());
            stmt.setString(5, registro.getTelefono());
            stmt.setDate(6, registro.getFechaContratacion());
            stmt.setBigDecimal(7, registro.getSueldo());
            stmt.setInt(8, registro.getCodigoDepartamento());
            stmt.setInt(9, registro.getCodigoCargo());
            stmt.setInt(10, registro.getCodigoHorario());
            stmt.setInt(11, registro.getCodigoAdministracion());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarEmpleados() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarEmpleados(?)}");
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
            System.err.println("\nSe produjo un error al intentar eliminar el registro en empleados");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existElementoSeleccionado() {
        return tblEmpleados.getSelectionModel().getSelectedItem() != null;
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
    private void nuevo(ActionEvent event) {
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
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtEmail);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtEmail);
                listaCampos.add(txtSueldo);

                ArrayList<JFXDatePicker> listaDatePicker = new ArrayList<>();
                listaDatePicker.add(dtpFechaContratacion);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCargo);
                listaComboBox.add(cmbHorarios);
                listaComboBox.add(cmbDepartamento);

                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (validarNombres(txtNombre.getText())) {
                        if (validarNombres(txtApellidos.getText())) {
                            if ((validarTelefono(txtTelefono.getText()))) {
                                if (validarEmail(txtEmail.getText())) {
                                    agregarEmpleados();
                                    cargarDatos();
                                    limpiar();
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
                                    alert.setContentText("El correo electronico no es valido.");
                                    alert.show();
                                }

                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Kinal Mall");
                                alert.setHeaderText(null);
                                alert.setContentText("Numero de telefono nos es valido");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Kinal Mall");
                            alert.setHeaderText(null);
                            alert.setContentText("No se permite ingresar numeros en el apellido");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Kinal Mall");
                        alert.setHeaderText(null);
                        alert.setContentText("No se permite ingresar numeros en el nombre");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
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
                    habilitarCampos();
                    btnModificar.setText("Actualizar");
                    ivModificar.setImage(new Image("/org/diegopinzon/resource/images/listo.png"));
                    btnEliminar.setText("Cancelar");
                    ivEliminar.setImage(new Image("/org/diegopinzon/resource/images/cancelar.png"));
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un elemento");
                    alert.show();
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtEmail);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtEmail);
                listaCampos.add(txtSueldo);

                ArrayList<JFXDatePicker> listaDatePicker = new ArrayList<>();
                listaDatePicker.add(dtpFechaContratacion);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCargo);
                listaComboBox.add(cmbHorarios);
                listaComboBox.add(cmbDepartamento);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    editarEmpleados();
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
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llena todos los campos");
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
        switch (operacion) {
            case ACTUALIZAR:
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
            case NINGUNO:
                if (existElementoSeleccionado()) {
                    Alert alertPregunta = new Alert(Alert.AlertType.CONFIRMATION);
                    alertPregunta.setTitle("Kinal Mall");
                    alertPregunta.setHeaderText(null);
                    alertPregunta.setContentText("¿Esta seguro que desea eliminar?");

                    Optional<ButtonType> respuesta = alertPregunta.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        eliminarEmpleados();
                        cargarDatos();
                        limpiar();
                        deshabilitarCampos();
                    } else if (respuesta.get() == ButtonType.CANCEL) {
                        limpiar();
                        deshabilitarCampos();
                    }
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal Mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }
        }
    }
    @FXML
    public void regresarAdministracion(MouseEvent event) {
        escenarioPrincipal.menuPrincipal();
    }

    @FXML
    private void regresarEmpleados(MouseEvent event) {
        escenarioPrincipal.menuPrincipal();
    }
    
    @FXML
    void mostrarVistaHorarios(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaHorarios();
    }
}