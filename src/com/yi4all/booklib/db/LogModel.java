package com.yi4all.booklib.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "log")
public class LogModel implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 4901273812769097177L;

	public final static String LOGTAG = "LogModel";
	
	public  static final String BOOK = "BOOK";
	public  static final String PERSON_NAME = "PERSON_NAME";
	public  static final String LENT_AT = "LENT_AT";
	public  static final String RETURN_AT = "RETURN_AT";
	
	@DatabaseField(generatedId = true)
	private long id = -1;
	@DatabaseField( columnName = LENT_AT)
	private Date lentAt;
	@DatabaseField( columnName = RETURN_AT)
	private Date returnAt;
	@DatabaseField( foreignAutoRefresh = true, foreign = true, columnName = BOOK)
	private BookModel book;
	@DatabaseField( columnName = PERSON_NAME)
	private String personName;

	public LogModel(){
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BookModel getBook() {
		return book;
	}

	public void setBook(BookModel book) {
		this.book = book;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Date getLentAt() {
		return lentAt;
	}

	public void setLentAt(Date lentAt) {
		this.lentAt = lentAt;
	}

	public Date getReturnAt() {
		return returnAt;
	}

	public void setReturnAt(Date returnAt) {
		this.returnAt = returnAt;
	}

	
}
