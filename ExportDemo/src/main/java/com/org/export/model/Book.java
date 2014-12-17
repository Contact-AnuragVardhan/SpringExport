package com.org.export.model;

import com.org.export.annotation.ExportInfo;
import com.org.export.interfaces.IExport;

public class Book implements IExport
{
	@ExportInfo(headerText="Title",order=1,width=30,wordWrap=true)
	private String title;
	@ExportInfo(headerText="Author",order=2,width=30)
	private String author;
	@ExportInfo(headerText="IsBN",order=3,width=30)
	private String isbn;
	@ExportInfo(headerText="Published Date",order=4)
	private String publishedDate;
	private float price;

	public Book(String title, String author, String isbn, String publishedDate, float price) 
	{
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.publishedDate = publishedDate;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}


}
