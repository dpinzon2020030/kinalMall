/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegopinzon.report;

import java.io.InputStream;
import java.util.Locale;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.util.Map;

import org.diegopinzon.db.Conexion;

/**
 *
 * @author Diego Pinzon
 */
public class GenerarReporte {

    private GenerarReporte() {
        Locale locale = new Locale("es", "GT");
        locale.setDefault(locale);
    }

    private static GenerarReporte instancia;
    private JasperViewer jasperViewer;

    public static GenerarReporte getInstance() {
        if (instancia == null) {
            instancia = new GenerarReporte();
        }
        return instancia;
    }

    public void mostrarReporte(String nombreReporte, Map parametros, String titulo) {
        try {
            parametros.put("LOGO_HEADER", getClass().getResourceAsStream
                                                ("/org/diegopinzon/resource/images/LogoEmpresa.png"));
            parametros.put("LOGO_FOOTER", getClass().getResourceAsStream
                                                ("/org/diegopinzon/resource/images/bolsaOficial.png"));
            InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parametros,
                    Conexion.getInstance().getConexion()
            );
            if (jasperViewer == null || !jasperViewer.isVisible()) {
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle(titulo);
                jasperViewer.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
