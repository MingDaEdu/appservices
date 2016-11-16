package com.mdedu.appservice.domainobject;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_BOOK")
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String uniqueId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Grade grade;

	@ManyToOne(fetch=FetchType.EAGER)
	private Subject subject;

	@ManyToOne(fetch=FetchType.EAGER)
	private Version version;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Publisher publisher;
	

	public Book(){
		
	}
	public Book(String name,String uniqueId){
		this.name=name;
		this.uniqueId=uniqueId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public String getFullName(){
		return name+"-"+(grade==null?"":grade.getName())+(subject==null?"":subject.getName())+(version==null?"":version.getName());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	
}
