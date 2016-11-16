package com.mdedu.appservice.domainobject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "T_VIDEO")
@JsonInclude(Include.NON_NULL)
public class Video {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String location;
	
	private String name;
	
	private String description;
	
	private Long playedCount;
	
	private String seq;
	
	private String relatedSeq;
	
	@ManyToOne
	private Chapter chapter;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPlayedCount() {
		return playedCount;
	}

	public void setPlayedCount(Long playedCount) {
		this.playedCount = playedCount;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getRelatedSeq() {
		return relatedSeq;
	}

	public void setRelatedSeq(String relatedSeq) {
		this.relatedSeq = relatedSeq;
	}	
	
}
