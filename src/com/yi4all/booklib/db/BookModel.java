package com.yi4all.booklib.db;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "book")
public class BookModel implements Serializable{

	public  static final String COVER_URL = "COVER_URL";
	public  static final String NAME = "NAME";
	public  static final String ISBN = "ISBN";
	public  static final String CODE = "CODE";
	public  static final String AUTHOR = "AUTHOR";
	public  static final String PUBLISH_ON = "PUBLISH_ON";
	public  static final String CATEGORY = "CATEGORY";
	public  static final String DELETE_FLAG = "DELETE_FLAG";
	public  static final String CREATED_AT = "CREATED_AT";
	public  static final String LENT_AT = "LENT_AT";
	public  static final String META_INFO = "META_INFO";
	public  static final String STATUS = "STATUS";
	public  static final String LENDER = "LENDER";
	
	@DatabaseField(generatedId = true)
	private long id;
	@DatabaseField(index = true, columnName = NAME)
	private String name;
	@DatabaseField(columnName = ISBN)
	private String isbn;
	@DatabaseField(columnName = CODE)
	private String code;
	@DatabaseField(columnName = AUTHOR)
	private String author;
	@DatabaseField(columnName = PUBLISH_ON)
	private String publishOn;
	@DatabaseField(columnName = META_INFO)
	private String metaInfo;
	@DatabaseField(columnName = CATEGORY)
	private String category="Category";
	@DatabaseField( columnName = COVER_URL)
	private String coverUrl;
	@DatabaseField(columnName = DELETE_FLAG)
	private boolean deleteFlag; // 0 - exists images ; 1 - no images
	@DatabaseField(columnName = CREATED_AT)
	private Date createdAt; 
	@DatabaseField(columnName = LENT_AT)
	private Date lentAt;
	@DatabaseField( columnName = LENDER)
	private String lender;
	@DatabaseField(columnName = STATUS)
	private BookStatus status=BookStatus.IN;
	
	public BookModel(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return coverUrl;
	}
	public void setUrl(String url) {
		this.coverUrl = url;
	}
	public boolean isDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Date getLentAt() {
		return lentAt;
	}

	public void setLentAt(Date lentAt) {
		this.lentAt = lentAt;
	}

	public String getLender() {
		return lender;
	}

	public void setLender(String lender) {
		this.lender = lender;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublishOn() {
		return publishOn;
	}

	public void setPublishOn(String publishOn) {
		this.publishOn = publishOn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
}
