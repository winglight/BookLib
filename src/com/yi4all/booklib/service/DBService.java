package com.yi4all.booklib.service;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.yi4all.booklib.db.BookDBOpenHelper;
import com.yi4all.booklib.db.BookModel;
import com.yi4all.booklib.db.BookStatus;
import com.yi4all.booklib.db.LogModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ChenYu on 13-8-8.
 */
public class DBService {

    private static final String LOG_TAG = "DBService";

    private BookDBOpenHelper helper;

    private DBService(Context context) {
        this.helper = BookDBOpenHelper.getHelper(context);
    }

    public static DBService getInstance(Context context) {
        return new DBService(context);
    }

    public void close() {
        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }

    public List<BookModel> getBooks(){
        List<BookModel> res = new ArrayList<BookModel>();

        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            res = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }
    
    public List<BookModel> findBooksByIsbn(String isbn){
    	List<BookModel> res = new ArrayList<BookModel>();

        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            res = dao.queryForEq(BookModel.ISBN, isbn);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }
    
    public List<BookModel> findLentBooks(){
    	List<BookModel> res = new ArrayList<BookModel>();

        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            res = dao.queryForEq(BookModel.STATUS, BookStatus.OUT);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }
    
    public String[] findCategories(){
    	String[] res = null;

        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            List<BookModel> list = dao.queryBuilder()
            	    .distinct().selectColumns(BookModel.CATEGORY).query();
            res = new String[list.size()];
            int i = 0;
            for(BookModel bm:list){
            	res[i] = bm.getCategory();
            	i++;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }
    
    public String[] findPersons(){
    	String[] res = null;

        try {
            Dao<LogModel, Integer> dao = helper.getLogDAO();

            List<LogModel> list = dao.queryBuilder()
            	    .distinct().selectColumns(LogModel.PERSON_NAME).query();
            res = new String[list.size()];
            int i = 0;
            for(LogModel bm:list){
            	res[i] = bm.getPersonName();
            	i++;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }
    
    //TODO:
    public String[] findLentPersons(){
    	String[] res = null;

        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

//            QueryBuilder<BookModel, Integer> queryBuilder = dao.queryBuilder();
//			Where<BookModel, Integer> where = queryBuilder.where();
//			where.eq(BookModel.BOOK, bm);
//				where.and();
//				where.eq(BookModel.LENT_AT, bm.getLentAt());

//			List<BookModel> list = dao.query(queryBuilder.prepare());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }

    public boolean createBook(BookModel sm){
        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            sm.setCreatedAt(new Date());
            int res = dao.create(sm);

            return  res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean updateBook(BookModel sm){
        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            int res = dao.update(sm);

            return  res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean deleteBook(BookModel sm){
        try {
            Dao<BookModel, Integer> dao = helper.getBookDAO();

            int res = dao.delete(sm);

            return  res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }
    
    public void lendBook(BookModel bm){
        try {
        	Dao<LogModel, Integer> dao = helper.getLogDAO();

            LogModel lm = new LogModel();
            lm.setBook(bm);
            lm.setLentAt(bm.getLentAt());
            lm.setPersonName(bm.getLender());
            
            dao.create(lm);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void returnBook(BookModel bm){
        try {
        	Dao<LogModel, Integer> dao = helper.getLogDAO();

        	QueryBuilder<LogModel, Integer> queryBuilder = dao.queryBuilder();
			Where<LogModel, Integer> where = queryBuilder.where();
			where.eq(LogModel.BOOK, bm);
				where.and();
				where.eq(LogModel.LENT_AT, bm.getLentAt());

			LogModel lm = dao.queryForFirst(queryBuilder.prepare());
			if(lm != null){
				lm.setReturnAt(new Date());
				dao.update(lm);
			}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
