package com.mdedu.appservice.controller;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mdedu.appservice.domainobject.Chapter;
import com.mdedu.appservice.domainobject.Video;

@JsonInclude(Include.NON_NULL)
public class ResChapter extends Chapter {

	
	private Collection<Video> videos;

	public Collection<Video> getVideos() {
		return videos;
	}

	public void setVideos(Collection<Video> videos) {
		this.videos = videos;
	}
	
	
	
}
