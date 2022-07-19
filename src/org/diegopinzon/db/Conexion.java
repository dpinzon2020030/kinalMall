/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diegopinzon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.DatabaseMetaData;
        
/*        
* @date 26 may. 2021
* @time 9:32:39
* @author Diego Eduardo Pinzon Rangel
*/
public class Conexion {
    
    private Connection conexion;
    private final String URL;
    private final String SERVER;
    private final String PUERTO;
    private final String DB;
    private final String USER;
    private final String PASS;
    
    private static Conexion instancia;

    private Conexion() {
        
        SERVER = "127.0.0.1";
        PUERTO = "3306";
        DB = "IN5BM_KinalMall";
        USER = "root";
        PASS = "admin";
        
        URL = "jdbc:mysql://" + SERVER +  ":" + PUERTO + "/" + DB + "?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false";
        
        try {
            
            // Registrar el Driver JDBC
            
            // para registrar el driver
            // con metodos mas antiduos se usa .newInstance
            // opcion 1
    
            // opcion 2
            // ses puede hacer de la forma que hacia antes o la mas reciente
            // mas viable
            // Class.forName("com.mysql.jdbc.Driver");
            
            // opcion 3
            // conector mas reciente
            // Class.forName("com.mysql.cj.jdbc.Driver"
            
            // opcion 4
            // a partir de jdk 6, los drivers JDBC 4
            // ya se registran automaticamente

            
            // Opción 1:
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            //Opción 2:
            //Class.forName("com.mysql.jdbc.Driver");
            
            //Opción 3:
            //Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Opción 4
            // A partir del JDK 6, los driver JDBC 4
            // ya se registran automáticamente.
            
            // La conexion a la DB se hace con el método getConnection();
            //conexion = DriverManager.getConnection(URL);
            conexion = DriverManager.getConnection(URL, USER, PASS);
            
            System.out.println("Conexión exitosa!");

            checkForWarning(conexion.getWarnings());

            DatabaseMetaData dma = conexion.getMetaData();
            System.out.println("\nConnected to " + dma.getURL());
            System.out.println("Driver " + dma.getDriverName());
            System.out.println("Version " + dma.getDriverVersion());
            System.out.println("");
            
        } catch (ClassNotFoundException e) {
            System.out.println("No se encuentra ninguna definición para la clase");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("No se puede crear una instancia del objeto");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("No se tiene los permisos para acceder al paquete");
        } catch (SQLException e) {
            System.out.println("Se produjo un error de tipo SQLException");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Vendor: " + e.getErrorCode());
            System.out.println("");            
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    private static boolean checkForWarning(SQLWarning warn) throws SQLException {
        boolean rc = false;
        if (warn != null) {
            System.out.println("\n *** Warning ***\n");
            rc = true;
            while (warn != null) {
                System.out.println("SQLState: " + warn.getSQLState());
                System.out.println("Message: " + warn.getMessage());
                System.out.println("Vendor: " + warn.getErrorCode());
                System.out.println("");
                warn = warn.getNextWarning();
            }
        }
        return rc;
    }
    
    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    
    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

}
