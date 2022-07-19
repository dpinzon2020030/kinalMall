/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import org.diegopinzon.system.Principal;

/*
* @date 24 may. 2021
* @time 13:04:29
* @author Diego Eduardo Pinzon Rangel
*/
public class MenuPrincipalController implements Initializable {
    
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    void mostrarVistaAutor(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenarioAutor();
    }

    @FXML
    private void mostrarVistaAdministracion(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaAdministracion();
    }

    @FXML
    private void mostrarVistaClientes(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaClientes();
    }

    @FXML
    void mostrarVistaProveedores(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaProveedores();
    }
    
     @FXML
    void mostrarVistaEmpleados(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaEmpleados();
    }
    
    @FXML
    void mostrarVistaHorarios(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaHorarios();
    }
    
}