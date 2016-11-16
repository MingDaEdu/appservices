package com.mdedu.appservice.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Chapter;

@Repository
public interface ChapterRespository extends CrudRepository<Chapter,Long>{

	Collection<Chapter> findByBook(Book book);
	
	Collection<Chapter> findByBookAndParent(Book book,Chapter parent);

}
