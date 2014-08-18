package com.sevadu.reportes;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;

import com.sevadu.conector.Conexion;

public class ReporteBase {

	protected Connection conexion;

	public byte[] getReporte() {
		byte[] fichero = null;

		conexion = null;
		try {
			
			

			ClassLoader cl = this.getClass().getClassLoader();
			InputStream fis = null;
			Map parameters = new HashMap();

			System.out.println(getCurrentDateMonth_Year().concat("-01"));
			System.out.println(getCurrentDate());

			parameters.put("fecha_desde",
					getCurrentDateMonth_Year().concat("-01"));
			parameters.put("fecha_hasta", getCurrentDate());
			parameters.put("aliado", "TODOS");
			parameters.put("naliado", "TODOS");
			parameters.put("zona", "");
			parameters.put("cliente", "");
			parameters.put("vendedor", "");
			
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			c.add(Calendar.MONTH, getCurrentMonth());
			c.add(Calendar.YEAR, getCurrentYear());
			 
			
			procesarControlSubida(new GregorianCalendar(getCurrentYear(), getCurrentMonth(), 1).getTime(),new Date());
			

			fis = (cl
					.getResourceAsStream("com/sevadu/reportes/R55420026.jasper"));

			/*
			 * String driver = "org.postgresql.Driver"; String url =
			 * "jdbc:postgresql://localhost/mantenimiento"; String user =
			 * "postgres"; String password = "123456";
			 */

			try {

				try {
					// conexion = java.sql.DriverManager.getConnection(url,
					// user, password);
					conexion = Conexion.getConexion();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			JasperPrint jasperPrint = null;

			try {

				if (fichero == null) {
					fichero = JasperRunManager.runReportToPdf(fis, parameters,
							conexion);
				}

			} catch (JRException ex) {
				ex.printStackTrace();
			}

			if (conexion != null) {
				conexion.close();
			}

		} catch (SQLException e) {
			System.out.println("Error de conexión: " + e.getMessage());
			System.exit(4);
		} catch (Exception e) {
			System.out.println("Error de Reporte: " + e.toString());
			System.exit(4);
		}

		return fichero;

	}

	public static String getCurrentDate() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static String convertirFechaMysql(Date fecha) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		String strDate = sdfDate.format(fecha);
		return strDate;
	}

	public static String getCurrentDateMonth_Year() {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int month = cal.get(Calendar.MONTH);
		month++;
		int year = cal.get(Calendar.YEAR);

		return String.valueOf(year).concat("-")
				.concat(String.format("%02d", month));
	}

	public static int getCurrentMonth() {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int month = cal.get(Calendar.MONTH);

		return month;
	}

	public static int getCurrentYear() {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int year = cal.get(Calendar.YEAR);

		return year;
	}

	public int procesarControlSubida(Date fechaDesde, Date fechaHasta)
			throws Exception {
		try {
			int correlativo = 0;
			String query = "";
			String codigoAliado = "";
			String nombreAliado = "";
			String ventas = "NO";
			String plan_ventas = "NO";
			String existencias = "NO";

			Connection cnn = Conexion.getConexion();
			Statement st = cnn.createStatement();
			Statement st1 = cnn.createStatement();

			query = "DELETE FROM tmp_control_upload ";
			st1.executeUpdate(query);

			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.set(Calendar.DATE, 1);
			Date firstDayOfMonth = cal.getTime();

			query = "select codigo_aliado,nombre from maestro_aliado  order by nombre";
			ResultSet rs = st.executeQuery(query);

			Date fechaAuxiliar = new Date();

			while (rs.next()) {

				codigoAliado = rs.getString("codigo_aliado");
				nombreAliado = rs.getString("nombre");
				fechaAuxiliar = fechaDesde;

				while (fechaAuxiliar.compareTo(fechaHasta) < 0) {
					ventas = "NO";
					ResultSet rsVentas = st1
							.executeQuery("SELECT codigo_aliado FROM ventas WHERE codigo_aliado = '"
									+ codigoAliado
									+ "' AND fecha_auditoria = '"
									+ convertirFechaMysql(fechaAuxiliar) + "'");
					if (rsVentas.next()) {
						ventas = "SI";
					}
					
					rsVentas.close();

					plan_ventas = "NO";
					ResultSet rsPlanVentas = st1
							.executeQuery(" SELECT codigo_aliado FROM plan_ventas WHERE codigo_aliado = '"
									+ codigoAliado
									+ "' AND fecha_auditoria = '"
									+ convertirFechaMysql(fechaAuxiliar) + "' ");
					if (rsPlanVentas.next()) {
						plan_ventas = "SI";
					}
					
					rsPlanVentas.close();

					existencias = "NO";
					ResultSet rsExistencia = st1
							.executeQuery(" SELECT codigo_aliado FROM existencia WHERE codigo_aliado = '"
									+ codigoAliado
									+ "' AND fecha_auditoria = '"
									+ convertirFechaMysql(fechaAuxiliar) + "' ");
					if (rsExistencia.next()) {
						existencias = "SI";
					}
					
					rsExistencia.close();

					Random rand = new Random();
					int randomNum = rand.nextInt((10000000 - 1) + 1) + 1;

					st1.executeUpdate("INSERT INTO tmp_control_upload VALUES ('"
							+ randomNum + "','" + codigoAliado + "','"
							+ nombreAliado + "','" + ventas + "','"
							+ plan_ventas + "','" + existencias + "','"
							+ convertirFechaMysql(fechaAuxiliar) + "')");

					Calendar c = Calendar.getInstance();
					c.setTime(fechaAuxiliar);
					c.add(Calendar.DATE, 1);
					fechaAuxiliar = c.getTime();

				}
				
				System.out.println(codigoAliado);

			}
			rs.close();
			st.close();
			st1.close();
			cnn.close();
			return correlativo;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

}
