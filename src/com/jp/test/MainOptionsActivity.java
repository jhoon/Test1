package com.jp.test;

import com.jp.test.util.SessionHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainOptionsActivity extends Activity {

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		if(!SessionHelper.hasAppID(this)){
			SessionHelper.getAppIDKey(this);
		}
		
		Log.v("UUID",SessionHelper.getAppIDKey(this));
		
		setContentView(R.layout.main_list);
		
		ListView lvMainOptions = (ListView)findViewById(R.id.list_main_opt);
		lvMainOptions.setAdapter(new ArrayAdapter(this, R.layout.list_item_simple, getResources().getStringArray(R.array.main_opt_array)));
		
		// Según la posición, se envía al activity deseado
		lvMainOptions.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch(position){
				case 0:
					startActivity(MainTabActivity.class);
					break;
				case 1:
					startActivity(LocalDataActivity.class);
					break;
				}
			}
		});
	}
	
	// Método para invocar un activity genérico
	@SuppressWarnings("unchecked")
	private void startActivity(Class clase){
		Intent myIntent = new Intent(this, clase);
		startActivity(myIntent);
	}
}
