package com.jp.test;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.jp.test.db.DatabaseHelper;
import com.jp.test.db.Movies;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LocalInsertActivity extends OrmLiteBaseActivity<DatabaseHelper>  {

	private EditText txtInsTitle;
	private EditText txtInsDirector;
	private Button btnSave;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert);
		
		txtInsTitle = (EditText)findViewById(R.id.txtInsTitle);
		txtInsDirector = (EditText)findViewById(R.id.txtInsDirector);
		btnSave = (Button)findViewById(R.id.btnSave);
		
		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try{
					Movies movie = saveToObj();
					Dao<Movies, Integer> dao = getHelper().getMoviesDao();
					boolean alreadyCreated=false;
					if(movie.getId() != null) {
						Movies dbCount = dao.queryForId(movie.getId());
						if (dbCount != null) {
							dao.update(movie);
							alreadyCreated = true;
						}
					}
					if (alreadyCreated) {
						finish();
					} else {
						dao.create(movie);
						setResult(RESULT_OK);
						finish();
						//LocalDataActivity.callMe(LocalInsertActivity.this, movie.getId());
					}
				} catch (SQLException x){
					throw new RuntimeException();
				}
			}
		});
	}

	private Movies saveToObj(){
		Movies m = new Movies();		
		m.setTitle(txtInsTitle.getText().toString());
		m.setDirector(txtInsDirector.getText().toString());
		return m;
	}
}
