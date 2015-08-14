package com.sevadu;

import com.sevadu.conector.Conexion;
import com.sevadu.helpers.EmailHelper;
import com.sevadu.reportes.ReporteBase;

public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	        ReporteBase rp = new ReporteBase();	        
	        byte[] attachmentData = rp.getReporte();

	        EmailHelper.sendCommentEmail("lrosales@dusa.com.ve", "CONTROL DE DATOS INTEGRADOS AL SEVADU",attachmentData);
	        EmailHelper.sendCommentEmail("martigasd@dusa.com.ve", "CONTROL DE DATOS INTEGRADOS AL SEVADU",attachmentData);
	        EmailHelper.sendCommentEmail("ljaimes@dusa.com.ve", "CONTROL DE DATOS INTEGRADOS AL SEVADU",attachmentData);
		
	}

}
