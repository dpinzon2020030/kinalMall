/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.diegopinzon.system.Principal;

/*
* @date 3 jun. 2021
* @time 2:30:34
* @author Diego Eduardo Pinzon Rangel
*/
public class AutorController implements Initializable{
    
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
    public void regresar(MouseEvent event) throws Exception{
        escenarioPrincipal.menuPrincipal();
    } 

}
