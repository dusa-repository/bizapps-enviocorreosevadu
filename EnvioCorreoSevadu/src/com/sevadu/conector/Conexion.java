package com.sevadu.conector;

import java.sql.SQLException;

public class Conexion {

    public static  java.sql.Connection getConexion() {
    	

    String driver = "com.mysql.jdbc.Driver";

   String url = "jdbc:mysql://www.sevadu.com/sevaduco_dusa_estadistica_ventas";
   String user = "sevaduco_root";
   String password = "123vox*";

   java.sql.Connection cnn=null;
   if (cnn==null)
   {
       try {
         Class.forName(driver);
         try {
        cnn    = java.sql.DriverManager.getConnection(url, user, password);

         } catch (SQLException ex) {
           ex.printStackTrace();
         }
       } catch (ClassNotFoundException ex) {
         ex.printStackTrace();
       }
   }

   return cnn;

  }

}

