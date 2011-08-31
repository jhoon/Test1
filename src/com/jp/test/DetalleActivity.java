package com.jp.test;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleActivity extends Activity {
	private static final String url = "http://restmocker.bitzeppelin.com/api/datatest/peliculas/$.json";
    private String mRowId;
    
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle);
        setTitle(R.string.detail_title);
        
        String strPelicula = "";
        String strImgUrl = "";
        Bundle extras = getIntent().getExtras();
        mRowId = "";
        if (extras != null) {
            mRowId = extras.getString(ListadoActivity.KEY_ROWID);
            strPelicula = extras.getString(ListadoActivity.KEY_TITLE);
            strImgUrl = extras.getString(ListadoActivity.KEY_ICON);
        }
        
        // Se llenan todos los campos del detalle
        try{
        	new DetalleTask().execute(strImgUrl);
        	JSONObject jsonPelicula = ListadoActivity.getJSONArrayFromURL(DetalleActivity.url.replace("$", mRowId)).getJSONObject(0);
			TextView content;
			content = (TextView)findViewById(R.id.txtTitle);
			content.setText(strPelicula);
			content = (TextView)findViewById(R.id.txtDirector);
			content.setText(jsonPelicula.getString("director"));
			content = (TextView)findViewById(R.id.txtGenre);
			content.setText(jsonPelicula.getString("genero"));
			content = (TextView)findViewById(R.id.txtRating);
			content.setText(jsonPelicula.getString("calificacion"));
			content = (TextView)findViewById(R.id.txtCast);
			content.setText(jsonPelicula.getString("actores"));
			content = (TextView)findViewById(R.id.txtPremiereDate);
			content.setText(jsonPelicula.getString("fecha-estreno"));
			content = (TextView)findViewById(R.id.txtRunningTime);
			content.setText(jsonPelicula.getString("duracion"));
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT);
		}
	}
	
	/**
	 * se usa el DetalleTask para la imagen que debe ser cargada en el detalle
	 */
	private class DetalleTask extends AsyncTask<String, Void, Bitmap>{
		ImageView iv;
		
		protected void onPreExecute(){
        	iv = (ImageView)findViewById(R.id.imgPelicula);
		}
		
		protected Bitmap doInBackground(String... params) {
			Bitmap image = null;
			try {
				image = ListadoActivity.getBitmapFromUrl(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return image;
		}
		
		protected void onPostExecute(Bitmap image){
			if (null != image)
		        iv.setImageBitmap(image);
		    else
				Toast.makeText(getApplicationContext(), "Error de imagen!", Toast.LENGTH_SHORT);
		}
	}
}
