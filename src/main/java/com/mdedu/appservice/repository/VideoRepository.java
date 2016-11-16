package com.mdedu.appservice.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Chapter;
import com.mdedu.appservice.domainobject.Video;

@Repository
public interface VideoRepository extends CrudRepository<Video,Long>{

	Collection<Video> findByChapter(Chapter value);

}
