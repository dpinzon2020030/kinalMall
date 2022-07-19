/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.bean;

import java.math.BigDecimal;

/*
* @date 14 jul. 2021
* @time 20:28:50
* @author Diego Eduardo Pinzon Rangel
*/
public class Proveedores {

    private int id;
    private String nit;
    private String servicioPrestado;
    private String telefono;
    private String direccion;
    private BigDecimal saldoFavor;
    private BigDecimal saldoContra;

    public Proveedores() {
    }
    
    public Proveedores(int aInt, String string, String string0, String string1, String string2, BigDecimal bigDecimal, BigDecimal bigDecimal0) {
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getServicioPrestado() {
        return servicioPrestado;
    }

    public void setServicioPrestado(String servicioPrestado) {
        this.servicioPrestado = servicioPrestado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getSaldoFavor() {
        return saldoFavor;
    }

    public void setSaldoFavor(BigDecimal saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public BigDecimal getSaldoContra() {
        return saldoContra;
    }

    public void setSaldoContra(BigDecimal saldoContra) {
        this.saldoContra = saldoContra;
    }

    @Override
    public String toString() {
        return id + " | " + servicioPrestado;
    }
    
}
