/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.system;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.diegopinzon.controller.AdministracionController;
import org.diegopinzon.controller.MenuPrincipalController;
import org.diegopinzon.controller.AutorController;
import org.diegopinzon.controller.ClientesController;
import org.diegopinzon.controller.CuentasPorCobrarController;
import org.diegopinzon.controller.DepartamentosController;
import org.diegopinzon.controller.HorariosController;
import org.diegopinzon.controller.LocalesController;
import org.diegopinzon.controller.ProveedoresController;
import org.diegopinzon.controller.TipoClienteController;
import org.diegopinzon.controller.CuentasPorPagarController;
import org.diegopinzon.controller.EmpleadosController;
/*
* @date 24 may. 2021
* @time 12:33:20
* @author Diego Eduardo Pinzon Rangel
*/
public class Principal extends Application {

    private final String PAQUETE_VIEW = "/org/diegopinzon/view/";
    private final String PAQUETE_IMAGES = "/org/diegopinzon/resource/images/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        
        menuPrincipal();
        
    }
     
    public void menuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = null;
            menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 1200, 677);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch(Exception ex){
            System.err.println("\nSe produjo un error al cargar la vista del menu principal");
            ex.printStackTrace();
        }
    }
    public void mostrarEscenarioAutor(){        
        try{
            AutorController menuAutor = null;
            menuAutor = (AutorController) cambiarEscena("AutorView.fxml",1200, 677);
            menuAutor.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.err.println("\nSe produjo un error al cagar la vista del autor.");
            System.out.println(ex.getMessage());
        }
    }
    public void mostrarEscenaAdministracion(){
        try{
            AdministracionController adminController = null;
            adminController = (AdministracionController) cambiarEscena("AdministracionView.fxml",1200, 677);
            adminController.setEscenarioPrincipal(this);
        }catch(Exception ex){
            System.err.println("\nSe produjo un error al cargar la vista de administracion");
            ex.printStackTrace();
        }
    }
    public void mostrarEscenaClientes(){
        try {
            ClientesController clientesController = null;
            clientesController =(ClientesController) cambiarEscena("ClientesView.fxml",1200, 677);
            clientesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de clientes.");
            e.printStackTrace();
        }
    }
    public void mostrarEscenaLocales(){
        try {
            LocalesController localesController = null;
            localesController = (LocalesController)cambiarEscena("LocalesView.fxml",1200, 677);
            localesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de locales.");
            e.printStackTrace();
        }
    }
    public void mostrarEscenaTipoCliente(){
        try {
            TipoClienteController tipoClienteController = null;
            tipoClienteController =(TipoClienteController) cambiarEscena("TipoClienteView.fxml",1200, 677);
            tipoClienteController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de tipo cliente.");
            e.printStackTrace();
        }
        
    }
    
    public void mostrarEscenaDepartamentos(){
        try {
            DepartamentosController departamentosController = null;
            departamentosController = (DepartamentosController) cambiarEscena("DepartamentosView.fxml",1200, 677);
            departamentosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de departamentos.");
            e.printStackTrace();
        }
    }
    public void mostrarEscenaHorarios(){
        try {
            HorariosController horariosController=null;
            horariosController = (HorariosController) cambiarEscena("HorariosView.fxml",1200, 677);
            horariosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de horarios.");
            e.printStackTrace();
        }
    }
    public void mostrarEscenaProveedores(){
        try {
            ProveedoresController proveedoresController= null;
            proveedoresController = (ProveedoresController) cambiarEscena("ProveedoresView.fxml",1200, 677);
            proveedoresController.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de proveedores.");
            e.printStackTrace();
        }
    }
    public void mostrarEscenaCuentasPorCobrar(){
        try {
            CuentasPorCobrarController cuentasPorCobrarController = null;
            cuentasPorCobrarController = (CuentasPorCobrarController) cambiarEscena("CuentasPorCobrarView.fxml", 1200, 677);
            cuentasPorCobrarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista cuentas por cobrar.");
            e.printStackTrace();
        }
    }
    public void mostrarEscenaCuentasPorPagar(){
        try {
            CuentasPorPagarController cuentasPorPagarController = null;
            cuentasPorPagarController = (CuentasPorPagarController) cambiarEscena("CuentasPorPagarView.fxml", 1200, 677);
            cuentasPorPagarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista cuentas por pagar.");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaEmpleados(){
        try {
            EmpleadosController empleadosController = null;
            empleadosController = (EmpleadosController) cambiarEscena("EmpleadosView.fxml", 1200, 677);
            empleadosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de empleados.");
            e.printStackTrace();
        }
    }
    
    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox ){
        boolean respuesta=true;

        for(ComboBox comboBox: listaComboBox){
            if(comboBox.getSelectionModel().getSelectedItem() == null){
                return false;
            }
            
        }        
        for(TextField campoTexto: listadoCampos){
            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        
        return respuesta;
    }
    
    public Initializable cambiarEscena(String viewFxml, int ancho, int alto) throws IOException {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();   
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VIEW + viewFxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VIEW + viewFxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.setTitle("Kinal Mall");
        escenarioPrincipal.sizeToScene();
        escenarioPrincipal.show();
        
        resultado = (Initializable) cargadorFXML.getController(); 
        return resultado;
    }
}