/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg100añosmastarde;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author diego
 */
public class ConexionUsuarios {
    private java.sql.Connection conexion;

    public ConexionUsuarios() {
        Properties configuracion = new Properties();
        FileInputStream arqConfiguracion;

        try {
            arqConfiguracion = new FileInputStream("baseDatos.properties");
            configuracion.load(arqConfiguracion);
            arqConfiguracion.close();

            Properties usuario = new Properties();
     

            String gestor = configuracion.getProperty("gestor");

            usuario.setProperty("user", configuracion.getProperty("usuario"));
            usuario.setProperty("password", configuracion.getProperty("clave"));
            this.conexion=java.sql.DriverManager.getConnection("jdbc:"+gestor+"://"+
                    configuracion.getProperty("servidor")+":"+
                    configuracion.getProperty("puerto")+"/"+
                    configuracion.getProperty("baseDatos"),
                    usuario);

        } catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        } catch (IOException i){
            System.out.println(i.getMessage());
        } 
        catch (java.sql.SQLException e){
            System.out.println(e.getMessage());
        } 
    }
    
    public boolean inicioSesion (String id, String password) {
        PreparedStatement stm = null;
        ResultSet rsInicioSesion;
        boolean esCorrecto = false;

        try  {
        stm = conexion.prepareStatement("select * from users where userid=? and "
                + "userpassword=?");
        stm.setString(1, id);
        stm.setString(2, password);
        rsInicioSesion = stm.executeQuery();
        if (rsInicioSesion.next()) esCorrecto = true;
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
          }
        }
        return esCorrecto;
    }
    
    public boolean registrarUsuario (String id, String password) {
        PreparedStatement stm = null;
        boolean esCorrecto = true;

        try  {
        stm = conexion.prepareStatement("insert into users (userid, userpassword) "
                + "values (?,?)");
        stm.setString(1, id);
        stm.setString(2, password);
        stm.executeUpdate();
        } catch (SQLException e){
          System.out.println(e.getMessage());
          esCorrecto = false;
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
          }
        }
        return esCorrecto;
    }
    
    public ArrayList<String> obtenerListaAmigos (String id) {
        PreparedStatement stm = null;
        ResultSet rsAmigos;
        ArrayList<String> listaAmigos = new ArrayList<String>();
        String auxiliar;
        
        try  {
        stm = conexion.prepareStatement("select * from friends where "
                + "(userid=? or userid2=?) and actuallyfriends=true");
        stm.setString(1, id);
        stm.setString(2, id);
        rsAmigos = stm.executeQuery();
        while (rsAmigos.next()) {
            auxiliar = rsAmigos.getString("userid");
            if (auxiliar.equals(id)) auxiliar = rsAmigos.getString("userid2");
            listaAmigos.add(auxiliar);
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
        return listaAmigos;
    }
    
    public ArrayList<String> obtenerListaUsuarios (String id) {
        PreparedStatement stm = null;
        ResultSet rsUsuarios;
        ArrayList<String> listaUsuarios = new ArrayList<String>();
        
        try  {
        stm = conexion.prepareStatement("select userid from users where userid!=?");
        stm.setString(1, id);
        rsUsuarios = stm.executeQuery();
        while (rsUsuarios.next()) {
            listaUsuarios.add(rsUsuarios.getString("userid"));
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
        return listaUsuarios;
    }
    
    public ArrayList<String> obtenerListaSugerencias (String id, String texto) {
        PreparedStatement stm = null;
        ResultSet rsUsuarios;
        ArrayList<String> listaUsuarios = new ArrayList<String>();
        texto = "%" + texto + "%";
        try  {
        stm = conexion.prepareStatement("select userid from users where userid!=? and userid like ?");
        stm.setString(1, id);
        stm.setString(2, texto);
        rsUsuarios = stm.executeQuery();
        while (rsUsuarios.next()) {
            listaUsuarios.add(rsUsuarios.getString("userid"));
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
        return listaUsuarios;
    }
    
    public boolean añadirSolicitud (String id, String amigo) {
        boolean añadidoCorrectamente = false;
        PreparedStatement stm = null;
        ResultSet rsSolicitudes;
        
        try  {
        stm = conexion.prepareStatement("select * from friends where (userid = ? "
                + "and userid2 = ?) or (userid = ? and userid2 = ?)");
        stm.setString(1, id);
        stm.setString(3, id);
        stm.setString(2, amigo);
        stm.setString(4, amigo);
        
        rsSolicitudes = stm.executeQuery();
        if (!rsSolicitudes.next()) {
            añadidoCorrectamente = true;
            stm = conexion.prepareStatement("insert into friends(userid, userid2,"
                    + "actuallyfriends) values (?, ?, false)");
            stm.setString(1, id);
            stm.setString(2, amigo);
            stm.executeUpdate();
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
        return añadidoCorrectamente;
    }
    
    public void aceptarAmigo (String id, String amigo) {
        PreparedStatement stm = null;
        ResultSet rsSolicitudes;
        
        try  {
        stm = conexion.prepareStatement("select * from friends where userid=? "
                + "and userid2=? "
                + "and actuallyfriends=false");
        stm.setString(1, amigo);
        stm.setString(2, id);
        
        rsSolicitudes = stm.executeQuery();
        if (rsSolicitudes.next()) {
            stm = conexion.prepareStatement("update friends set actuallyfriends=true "
                    + "where userid=? and userid2=?");
            stm.setString(1, amigo);
            stm.setString(2, id);
            stm.executeUpdate();
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
    }
    
    public ArrayList<String> obtenerListaSolicitudesRecibidas (String id) {
        PreparedStatement stm = null;
        ResultSet rsSolicitudes;
        String auxiliar;
        ArrayList<String> listaSolicitudes = new ArrayList<String>();
        
        try {
        stm = conexion.prepareStatement("select * from friends where " +
                "userid2 = ? "
                + "and actuallyfriends = false");
        stm.setString(1, id);
        
        rsSolicitudes = stm.executeQuery();
        while (rsSolicitudes.next()) {
            auxiliar = rsSolicitudes.getString("userid");
            listaSolicitudes.add(auxiliar);
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
        return listaSolicitudes;
    }
    
     public ArrayList<String> obtenerListaSolicitudesEnviadas (String id) {
        PreparedStatement stm = null;
        ResultSet rsSolicitudes;
        String auxiliar;
        ArrayList<String> listaSolicitudes = new ArrayList<String>();
        
        try {
        stm = conexion.prepareStatement("select * from friends where " +
                "userid=? "
                + "and actuallyfriends = false");
        stm.setString(1, id);
        
        rsSolicitudes = stm.executeQuery();
        while (rsSolicitudes.next()) {
            auxiliar = rsSolicitudes.getString("userid2");
            listaSolicitudes.add(auxiliar);
        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
        return listaSolicitudes;
    }
    
    public void rechazarSolicitud (String id, String amigo) {
        PreparedStatement stm = null;
        System.out.println(id + " " + amigo);
        try  {
        stm = conexion.prepareStatement("delete from friends where userid=? "
                + "and userid2=? and actuallyfriends=false");
        stm.setString(1, amigo);
        stm.setString(2, id);
        stm.executeUpdate();
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
    }
    
    public void actualizarContraseña (String id, String contraseña) {
        PreparedStatement stm = null;
        try  {
        stm = conexion.prepareStatement("update users set "
                + "userpassword=? where userid=?");
        stm.setString(1, contraseña);
        stm.setString(2, id);
        stm.executeUpdate();
        } catch (SQLException e){
          System.out.println(e.getMessage());
        }finally{
          try {
            stm.close();
          } 
          catch (SQLException e) { 
              System.out.println("Imposible cerrar cursores");
            }
        }
    }
    
}   