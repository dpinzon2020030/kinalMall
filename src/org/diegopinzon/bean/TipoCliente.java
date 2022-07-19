/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.bean;

/*
* @date 9 jun. 2021
* @time 9:27:27
* @author Diego Eduardo Pinzon Rangel
*/
public class TipoCliente {
    private int id;
    private String descripcion;

    public TipoCliente() {
    }

    public TipoCliente(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return   id + " | " + descripcion ;
    }

}
