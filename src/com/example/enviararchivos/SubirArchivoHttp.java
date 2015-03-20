package com.example.enviararchivos;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;


public class SubirArchivoHttp extends AsyncTask<String, Void, String>{
	 
    private static String URL_SERVIDOR = "http://192.168.1.65:8080/ServidorArchivos/SubirArchivos"; 
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

	@Override
	protected String doInBackground(String... arg0) {
				
		String nombre_usuario = arg0[0];
		
        try {
        	//Agregando usuario
        	String url_completa = URL_SERVIDOR+"?usuario="+nombre_usuario;
        	
        	//Creando cliente http
        	HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url_completa);
            
            //Creando entidad multiparte
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            
            //Agregando archivos
            for (int i=1; i<arg0.length; i++){
            	File file = new File(arg0[i]);
                FileBody fileBody = new FileBody(file);
                builder.addPart("uploadFile"+i, fileBody);
            } 
            
            //Construyendo entidad http con la entidad multiparte
            HttpEntity entity = builder.build();
            httppost.setEntity(entity);
            
            //Obteniendo respuesta del servidor
            HttpResponse response = httpclient.execute(httppost);
            httpclient.getConnectionManager().shutdown();
            if (response.getStatusLine().getStatusCode() == 200){//Status = OK
            	return ("Archivos enviados correctamente");
            }
            else{
            	return ("Error al subir archivos: servidor");
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return ("Error al subir archivos: excepcion");
        }
	}
	
}
