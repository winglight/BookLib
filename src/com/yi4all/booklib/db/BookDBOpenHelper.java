package com.yi4all.booklib.db;

import java.sql.SQLException;
import java.util.Date;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookDBOpenHelper extends OrmLiteSqliteOpenHelper {
	
	public  static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_NAME = "book";
	
	private static BookDBOpenHelper helper = null;
	
	private Dao<LogModel, Integer> logDao;
	private Dao<BookModel, Integer> bookDao;

    public BookDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static synchronized BookDBOpenHelper getHelper(Context context) {
		if (helper == null) {
			helper = new BookDBOpenHelper(context);
			helper.getWritableDatabase();
		}
		return helper;
	}

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    	try {
			Log.i(BookDBOpenHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, LogModel.class);
			TableUtils.createTable(connectionSource, BookModel.class);

		} catch (SQLException e) {
			Log.e(BookDBOpenHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
        
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(BookDBOpenHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, BookModel.class, true);
			TableUtils.dropTable(connectionSource, LogModel.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(BookDBOpenHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
		
	}
	
	public Dao<LogModel, Integer> getLogDAO() throws SQLException{
    	if(logDao == null){
    		logDao = getDao(LogModel.class);
    	}
    	return logDao;
    }
    
    public Dao<BookModel, Integer> getBookDAO() throws SQLException{
    	if(bookDao == null){
    		bookDao = getDao(BookModel.class);
    	}
    	return bookDao;
    }
    
    @Override
	public void close() {
		super.close();
		logDao = null;
		bookDao = null;
	}

}
