package com.mdedu.appservice.domainobject;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "T_CHAPTER")
@JsonInclude(Include.NON_NULL)
public class Chapter {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Chapter parent;
	
	@ManyToOne
	private Book book;
	 
	@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER,mappedBy="parent", orphanRemoval=true)
	private List<Chapter> children;
	
	private String title;
	
	private String seq;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Chapter getParent() {
		return parent;
	}

	public void setParent(Chapter parent) {
		this.parent = parent;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public List<Chapter> getChildren() {
		return children;
	}

	public void setChildren(List<Chapter> children) {
		this.children = children;
	}
	
	
}
