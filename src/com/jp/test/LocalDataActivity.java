package com.jp.test;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.jp.test.db.DatabaseHelper;
import com.jp.test.db.Movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LocalDataActivity extends Activity {
	
//	public static void callMe(Context c, Integer clickCounterId) {
//		Intent intent = new Intent(c, Movies.class);
//		intent.putExtra(CLICK_COUNTER_ID, clickCounterId);
//		c.startActivity(intent);
//	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_list);
		try{refreshList();}catch(Exception e){}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_local, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.mniRefreshLocal:
	    	try {
	    		refreshList();
	    	}catch (Exception e){}
	        return true;
	    case R.id.mniNew:
	        goToNew();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean onPrepareOptionsMenu (Menu menu){
		return true;
	}
	
	private void goToNew(){
		Intent intent = new Intent(getApplicationContext(),LocalInsertActivity.class);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1){
			if(resultCode == RESULT_OK){
				try{ refreshList(); } catch (Exception e){}
			}
		}
	}
	
	private void refreshList() throws SQLException{
//		Dao<Movies,Integer> dao = getHelper().getMoviesDao();
		Dao<Movies,Integer> dao = (new DatabaseHelper(this)).getMoviesDao();
		QueryBuilder<Movies, Integer> builder = dao.queryBuilder();
		builder.orderBy("id", true);
		List<Movies> movies =dao.query(builder.prepare());
		Log.v(this.getClass().toString(), movies.size()+"");
		ListView lv = (ListView)findViewById(R.id.list_movies_local);
		lv.setAdapter(new MoviesAdapter(this, R.layout.list_item_simple, movies));
	}
	
	private class MoviesAdapter extends ArrayAdapter<Movies> {
		public MoviesAdapter(Context context, int textViewResourceId, List<Movies> items) {
			super(context, textViewResourceId, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item_simple, null);
			}
			Movies movie = getItem(position);
			fillText(v, R.id.txtSimpleListTitle, movie.getTitle());
			return v;
		}

		private void fillText(View v, int id, String text) {
			TextView textView = (TextView) v.findViewById(id);
			textView.setText(text == null ? "" : text);
		}
	}
}
