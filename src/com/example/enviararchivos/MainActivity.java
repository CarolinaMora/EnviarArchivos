package com.example.enviararchivos;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static int CODE_IMAGEN1 = 0;
	private static int CODE_IMAGEN2 = 1;
	private static int CODE_IMAGEN3 = 2;

	private String path_imagen1 = null;
	private String path_imagen2 = null;
	private String path_imagen3 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void seleccionarImagen1 (View view){
		//Intent a galeria de imagenes para seleccionar una
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(intent, CODE_IMAGEN1);
	}
	
	public void seleccionarImagen2 (View view){
		//Intent a galeria de imagenes para seleccionar una
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(intent, CODE_IMAGEN2);
	}
	
	public void seleccionarImagen3 (View view){
		//Intent a galeria de imagenes para seleccionar una
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(intent, CODE_IMAGEN3);
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK) {
			
			//Se obtiene la uri de la imagen seleccionada
			Uri selectedImage = data.getData();
			String ruta = getPath(selectedImage);//selectedImage.getPath();
			
			//Se coloca en el Textview que corresponda
			switch (requestCode)
			{
			case 0:
				TextView tv_imagen1 = (TextView)findViewById(R.id.tv_imagen1);
				tv_imagen1.setText(ruta);
				path_imagen1 = ruta;
				break;
			case 1:
				TextView tv_imagen2 = (TextView)findViewById(R.id.tv_imagen2);
				tv_imagen2.setText(ruta);
				path_imagen2 = ruta;
				break;
			case 2:
				TextView tv_imagen3 = (TextView)findViewById(R.id.tv_imagen3);
				tv_imagen3.setText(ruta);
				path_imagen3 = ruta;
				break;
			
			}
		}
		
	}
	
	public String getPath(Uri uri) {
		
		//Valisar que uri no este vacia
        if( uri == null ) {
            return null;
        }
        
        //Obtener path completo de la imagen seleccionada
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
	}
	
	public void enviarImagenes (View view){
		
		//Validamos si todos los archivos han sido seleccionados 
		if(path_imagen1!=null && path_imagen2!=null && path_imagen3!=null){
			
			//Ejecutando tarea asincrona que envia los archivos al servidor
			SubirArchivoHttp subirImagenes = new SubirArchivoHttp();
			subirImagenes.execute("usuario", path_imagen1, path_imagen2, path_imagen3);
			
			//Obteniendo resultado de la tarea
			String resultado = null;
			try {
				resultado = subirImagenes.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "Resultado: "+resultado, Toast.LENGTH_SHORT).show();
		}
		
		//Si no pedir al usuario que los seleccione
		else{
			Toast.makeText(getApplicationContext(), "Seleccione los tres archivos", Toast.LENGTH_SHORT).show();
		}
		
	}
}
