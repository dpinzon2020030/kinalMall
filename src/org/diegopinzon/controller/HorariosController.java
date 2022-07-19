/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;
        
import com.jfoenix.controls.JFXTimePicker;
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
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
        
import org.diegopinzon.bean.Horarios;
import org.diegopinzon.db.Conexion;
import org.diegopinzon.system.Principal;

/*
* @date 7 jul. 2021
* @time 9:01:01
* @author Diego Eduardo Pinzon Rangel
*/
public class HorariosController implements Initializable {

    private Principal escenarioPrincipal;
    private boolean respuesta;
    private final String PAQUETE_IMAGES = "/org/diegopinzon/resource/images/";

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<Horarios> listaHorarios;

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
    private TableView tblHorarios;

    @FXML
    private TextField txtId;

    @FXML
    private JFXTimePicker tpHoraEntrada;

    @FXML
    private JFXTimePicker tpHoraSalida;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colHoraEntrada;

    @FXML
    private TableColumn colHoraSalida;

    @FXML
    private TableColumn colLunes;

    @FXML
    private TableColumn colMartes;

    @FXML
    private TableColumn colMiercoles;

    @FXML
    private TableColumn colJueves;

    @FXML
    private TableColumn colViernes;

    @FXML
    private CheckBox cbLunes;

    @FXML
    private CheckBox cbMartes;

    @FXML
    private CheckBox cbMiercoles;

    @FXML
    private CheckBox cbJueves;

    @FXML
    private CheckBox cbViernes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tpHoraEntrada.set24HourView(true);
        tpHoraSalida.set24HourView(true);

        StringConverter<LocalTime> defaultConverter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.US);
        tpHoraEntrada.setConverter(defaultConverter);

        StringConverter<LocalTime> defaultConverter2 = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK);
        tpHoraEntrada.setConverter(defaultConverter2);
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
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

    public void activarControles() {
        txtId.setEditable(false);
        tpHoraEntrada.setDisable(false);
        tpHoraSalida.setDisable(false);
        cbLunes.setDisable(false);
        cbMartes.setDisable(false);
        cbMiercoles.setDisable(false);
        cbJueves.setDisable(false);
        cbViernes.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        tpHoraEntrada.setDisable(true);
        tpHoraSalida.setDisable(true);
        cbLunes.setDisable(true);
        cbMartes.setDisable(true);
        cbMiercoles.setDisable(true);
        cbJueves.setDisable(true);
        cbViernes.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        tpHoraEntrada.getEditor().clear();
        tpHoraSalida.getEditor().clear();
        cbLunes.setSelected(false);
        cbMartes.setSelected(false);
        cbMiercoles.setSelected(false);
        cbJueves.setSelected(false);
        cbViernes.setSelected(false);
    }

    public boolean checkBoxLunes() {
        if ((cbLunes.isSelected() == false)) {
            respuesta = false;

        } else if ((cbLunes.isSelected() == true)) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean checkBoxMartes() {
        if ((cbMartes.isSelected() == false)) {
            respuesta = false;

        } else if ((cbMartes.isSelected() == true)) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean checkBoxMiercoles() {
        if ((cbMiercoles.isSelected() == false)) {
            respuesta = false;

        } else if ((cbMiercoles.isSelected() == true)) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean checkBoxJueves() {
        if ((cbJueves.isSelected() == false)) {
            respuesta = false;

        } else if ((cbJueves.isSelected() == true)) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean checkBoxViernes() {
        if ((cbViernes.isSelected() == false)) {
            respuesta = false;

        } else if ((cbViernes.isSelected() == true)) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean verificacion() {
        if (((tpHoraSalida.getValue() == null)) || ((tpHoraEntrada.getValue() == null))) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validarFecha(String date) {
        String patron = "[0-9]{4}[-/.](0[0-9]|1[012])[-/.](0[0-9]|1[0-9]|2[0-9]|3[01])";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public boolean validarTiempo(String time) {
        String patron = "([01][0-9]|[2][0123]):([0-5][0-9]):([0-5][0-9])";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    public void cargarDatos() {
        tblHorarios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
        desactivarControles();
    }

    public void seleccionarElemento() {
        try {
            txtId.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId()));
            tpHoraEntrada.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpHoraSalida.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida().toLocalTime());
            cbLunes.setSelected(Boolean.parseBoolean(String.valueOf(((Horarios) tblHorarios.getSelectionModel()
                    .getSelectedItem()).getLunes())));
            cbMartes.setSelected(Boolean.parseBoolean(String.valueOf(((Horarios) tblHorarios.getSelectionModel()
                    .getSelectedItem()).getMartes())));
            cbMiercoles.setSelected(Boolean.parseBoolean(String.valueOf(((Horarios) tblHorarios.getSelectionModel()
                    .getSelectedItem()).getMiercoles())));
            cbJueves.setSelected(Boolean.parseBoolean(String.valueOf(((Horarios) tblHorarios.getSelectionModel()
                    .getSelectedItem()).getJueves())));
            cbViernes.setSelected(Boolean.parseBoolean(String.valueOf(((Horarios) tblHorarios.getSelectionModel()
                    .getSelectedItem()).getViernes())));
        } catch (Exception e) {

        }
    }

    public void agregarHorarios() {
        Horarios registro = new Horarios();
        registro.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
        registro.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
        registro.setLunes(Boolean.parseBoolean(String.valueOf(checkBoxLunes())));
        registro.setMartes(Boolean.parseBoolean(String.valueOf(checkBoxMartes())));
        registro.setMiercoles(Boolean.parseBoolean(String.valueOf(checkBoxMiercoles())));
        registro.setJueves(Boolean.parseBoolean(String.valueOf(checkBoxJueves())));
        registro.setViernes(Boolean.parseBoolean(String.valueOf(checkBoxViernes())));

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            stmt.setTime(1, registro.getHorarioEntrada());
            stmt.setTime(2, registro.getHorarioSalida());
            stmt.setBoolean(3, registro.getLunes());
            stmt.setBoolean(4, registro.getMartes());
            stmt.setBoolean(5, registro.getMiercoles());
            stmt.setBoolean(6, registro.getJueves());
            stmt.setBoolean(7, registro.getViernes());
            stmt.execute();
            cargarDatos();
            limpiarControles();
            desactivarControles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarHorarios() {
        Horarios registro = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
        registro.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
        registro.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
        registro.setLunes(Boolean.parseBoolean(String.valueOf(checkBoxLunes())));
        registro.setMartes(Boolean.parseBoolean(String.valueOf(checkBoxMartes())));
        registro.setMiercoles(Boolean.parseBoolean(String.valueOf(checkBoxMiercoles())));
        registro.setJueves(Boolean.parseBoolean(String.valueOf(checkBoxJueves())));
        registro.setViernes(Boolean.parseBoolean(String.valueOf(checkBoxViernes())));

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setTime(2, registro.getHorarioEntrada());
            stmt.setTime(3, registro.getHorarioSalida());
            stmt.setBoolean(4, registro.getLunes());
            stmt.setBoolean(5, registro.getMartes());
            stmt.setBoolean(6, registro.getMiercoles());
            stmt.setBoolean(7, registro.getJueves());
            stmt.setBoolean(8, registro.getViernes());
            stmt.execute();
            cargarDatos();
            limpiarControles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarHorarios() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarHorarios(?)}");
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
            System.err.println("\nSe produjo un error al intentar eliminar el registro en Horarios");
            e.printStackTrace();
        } catch (Exception e) {
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
                ivNuevo.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                ivModificar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                break;
            case GUARDAR:
                if (verificacion()) {
                    agregarHorarios();
                    operacion = Operaciones.NINGUNO;
                    btnNuevo.setText("Nuevo");
                    btnModificar.setText("Modificar");
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                    ivNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));
                    ivModificar.setImage(new Image(PAQUETE_IMAGES + "escribir.png"));
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
                    ivModificar.setImage(new Image(PAQUETE_IMAGES + "listo.png"));
                    btnEliminar.setText("Cancelar");
                    ivEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
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
                    editarHorarios();
                    cargarDatos();
                    limpiarControles();
                    btnModificar.setText("Modificar");
                    ivModificar.setImage(new Image(PAQUETE_IMAGES +  "escribir.png"));
                    btnEliminar.setText("Eliminar");
                    ivEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
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

                break;
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnModificar.setText("Modificar");
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                ivNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));
                ivModificar.setImage(new Image(PAQUETE_IMAGES + "escribir.png"));
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
                ivModificar.setImage(new Image(PAQUETE_IMAGES + "escribir.png"));
                btnEliminar.setText("Eliminar");
                ivEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
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
                        eliminarHorarios();
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
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "compra.png"));
                    alert.show();
                }
                break;
        }
    }

    @FXML
    void reporte(ActionEvent event) {

    }

    @FXML
    void regresarHorarios(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaEmpleados();
    }

}

