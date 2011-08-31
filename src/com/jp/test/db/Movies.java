package com.jp.test.db;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies")
public class Movies implements Serializable {

	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = -472012866966151510L;

	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField(canBeNull = false)
	private String title;
	
	@DatabaseField(canBeNull = false)
	private String director;
	
	@DatabaseField
	private String sinopsis;
	
	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getSinopsis() {
		return sinopsis;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	public Movies(){}
}
