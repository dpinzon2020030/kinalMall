/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.bean;

/*
* @date 14 jul. 2021
* @time 20:45:23
* @author Diego Eduardo Pinzon Rangel
*/
public class Cargos {

    private int id;
    private String nombre;

    

    public Cargos() {
    }
    
    public Cargos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Cargos:" + "id=" + id + ", nombre=" + nombre;
    }
}
