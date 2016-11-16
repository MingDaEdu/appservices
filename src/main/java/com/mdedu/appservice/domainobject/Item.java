package com.mdedu.appservice.domainobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
	@SerializedName("g")
	private String grade;
	@SerializedName("ch")
	private String charpter;
	@Expose
	@SerializedName("seq")
	private String sequence;
	@Expose
	@SerializedName("t")
	private String title;
	@Expose
	@SerializedName("bid")
	private String book;
	@SerializedName("url")
	private String url;
	@Expose
	@SerializedName("rSeq")
	private String relatedSeq;
	@Expose
	@SerializedName("pcount")
	private Integer playedTimes;
	
	@Expose
	@SerializedName("sort")	
	private Integer sortID;

	public Item(String grade, String charpter, String sequence, String title,
			String bookid) {
		this.grade = grade;
		this.sequence = sequence;
		this.charpter = charpter;
		this.title = title;
		this.book = bookid;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the charpter
	 */
	public String getCharpter() {
		return charpter;
	}

	/**
	 * @param charpter
	 *            the charpter to set
	 */
	public void setCharpter(String charpter) {
		this.charpter = charpter;
	}

	/**
	 * @return the book
	 */
	public String getBook() {
		return book;
	}

	/**
	 * @param book
	 *            the book to set
	 */
	public void setBook(String book) {
		this.book = book;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the playedTimes
	 */
	public Integer getPlayedTimes() {
		return playedTimes;
	}

	/**
	 * @param playedTimes
	 *            the playedTimes to set
	 */
	public void setPlayedTimes(Integer playedTimes) {
		this.playedTimes = playedTimes;
	}
	
	

	/**
	 * @return the sortID
	 */
	public Integer getSortID() {
		return sortID;
	}

	/**
	 * @param sortID the sortID to set
	 */
	public void setSortID(Integer sortID) {
		this.sortID = sortID;
	}

	public void play() {
		if (this.playedTimes == null) {
			this.playedTimes = 1;
		} else {
			this.playedTimes++;
		}
	}
	
	
	public String getRelatedSeq() {
		return relatedSeq;
	}

	public void setRelatedSeq(String relatedSeq) {
		this.relatedSeq = relatedSeq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "Item {" + "年级" + grade
				+ (charpter == null ? "" : (", 章节:" + charpter))
				+ (sequence == null ? "" : (", 顺序:" + sequence))
				+ (title == null ? "" : (", 标题:" + title)) + "}";
	}

}