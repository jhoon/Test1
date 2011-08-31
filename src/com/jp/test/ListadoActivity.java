package com.jp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListadoActivity extends Activity {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_ICON = "icon";
	public static final String KEY_TITLE = "titulo";
	public static final String KEY_BITMAP = "bitmap";
	private static final String url = "http://restmocker.bitzeppelin.com/api/datatest/peliculas.json";
	
	private PopulateGUITask myTask;
	
	String[] movieList;
	String[] movieId;
	String[] movieIcon;
	Bitmap[] bImages;
	JSONArray movies;
	LinearLayout layLoading;

	/**
	 * BroadcastReceiver para monitorear cambios a la conectividad de red
	 */
	private BroadcastReceiver brConnReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			makeToast("this is a change of connectivity!");
			Log.v("Listado", "it's alive!");
			boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			if (noConnectivity){
				Log.v("Listado", "it's alive and without connection!!");
			} else {
				Log.v("Listado", "it's alive and is connected!!!");
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerReceiver(brConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		myTask = new PopulateGUITask();
		myTask.execute();
	}

	/**
	 * convierte el contenido proveido por el InputStream a texto entendible
	 */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/** 
	 * devuelve un JSONArray (usa convertStreamToString) 
	 */
	protected static JSONArray getJSONArrayFromURL(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		JSONArray arResp = null;
		try {
			response = httpclient.execute(httpget);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					arResp = new JSONArray(convertStreamToString(instream));
					instream.close();
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return arResp;
	}
	
	/**
	 * devuelve un bitmap para usar en la aplicación
	 */
	public static Bitmap getBitmapFromUrl(String url) throws Exception{
		URL ulrn = new URL(url);
	    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
	    InputStream is = con.getInputStream();
		return BitmapFactory.decodeStream(is);
	}

	
	/**
	 * método para mostrar un pequeño mensaje
	 */
	public void makeToast(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG);
	}
	
	/**
	 * AsyncTask usado para llenar la interfaz de forma asincrónica
	 */
	private class PopulateGUITask extends AsyncTask<Void, Void, Void> {
		/**
		 * se preparan los elementos de la UI para indicar que está cargando.
		 */
		protected void onPreExecute(){
			((TextView)findViewById(R.id.txtErrMsg)).setText(R.string.loading);
			((ProgressBar)findViewById(R.id.prgMain)).setVisibility(View.VISIBLE);
			layLoading = (LinearLayout)findViewById(R.id.layLoading);
			layLoading.setVisibility(View.VISIBLE);
		}
		
		/**
		 * operación a realizar en otro hilo
		 */
		protected Void doInBackground(Void... unused){
			movies = getJSONArrayFromURL(ListadoActivity.url);
			return null;
		}
		
		/**
		 * en caso de que la operación se cancele, se llama a este método,
		 * se ejecuta en el mismo hilo de la UI
		 * -- aquí solo se muestra un mensaje de error
		 */
		protected void onCancelled(){
			layLoading.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.txtErrMsg)).setText(R.string.main_stopped);
			((ProgressBar)findViewById(R.id.prgMain)).setVisibility(View.GONE);
		}
		
		/**
		 * una vez terminado doInBackground, se ejecuta este método
		 * se ejecuta en el mismo hilo de la UI
		 * -- se oculta la interfaz de carga y se muestran los resultados obtenidos
		 */
		protected void onPostExecute(Void unused) {
			if(movies!=null){
				layLoading.setVisibility(View.GONE);
				findViewById(R.id.list_movies).setVisibility(View.VISIBLE);
				try {
					movieList = new String[movies.length()];
					movieId = new String[movies.length()];
					movieIcon = new String[movies.length()];
					bImages = new Bitmap[movies.length()];
					for (int i = 0; i < movies.length(); i++) {
						JSONObject movie;
						movie = movies.getJSONObject(i);
						movieId[i] = movie.getString("id");
						movieList[i] = movie.getString("nombre");
						movieIcon[i] = movie.getString("icono");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.v("Listado","esta cosa se ha caido mal.");
				}
				
				ListView lv = (ListView)findViewById(R.id.list_movies);
				lv.setAdapter(new ListadoAdapter(ListadoActivity.this));
				
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent myIntent = new Intent(ListadoActivity.this,DetalleActivity.class);
						myIntent.putExtra(ListadoActivity.KEY_ROWID, movieId[position]);
						myIntent.putExtra(ListadoActivity.KEY_TITLE,movieList[position]);
						myIntent.putExtra(ListadoActivity.KEY_ICON, movieIcon[position]);
						startActivity(myIntent);
					}
				});
			} else {
				makeToast(getString(R.string.main_error));
			}
		}
	}
	
	private class GetImagesTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused){
			
		}
	}
	
	
	/* métodos correspondientes al menú */
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.mniRefresh:
	        refreshList();
	        return true;
	    case R.id.mniStop:
	        stopLoading();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean onPrepareOptionsMenu (Menu menu){
		if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(true);
        } else {
        	menu.getItem(0).setEnabled(true);
			menu.getItem(1).setEnabled(false);
        }
		return true;
	}
	/* ********* */
	
	private void refreshList(){
		myTask = new PopulateGUITask();
		myTask.execute();
	}
	
	private void stopLoading(){
		if (myTask != null && myTask.getStatus() == AsyncTask.Status.RUNNING) {
			myTask.cancel(true);
			myTask = null;
        }
	}
}