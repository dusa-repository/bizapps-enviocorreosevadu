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

	        //EmailHelper.sendCommentEmail("frivero84@gmail.com", "EL USUARIO: HA ENVIADO UNA SOLICITUD DE EVENTOS ESPECIALES CON ID: ",attachmentData);
		
	}

}
