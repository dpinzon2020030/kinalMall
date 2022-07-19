/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.bean;

/*
* @date 18 jun. 2021
* @time 21:48:04
* @author Diego Eduardo Pinzon Rangel
*/
public class Departamentos {
    private int id; 
    private String nombre;

    public Departamentos() {
    }

    public Departamentos(int id, String nombre) {
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
        return  id + " | " + nombre;
    }
}
