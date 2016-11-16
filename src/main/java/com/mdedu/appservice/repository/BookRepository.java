package com.mdedu.appservice.repository;

import java.util.Collection;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Book;

@Repository
public interface BookRepository extends CrudRepository<Book,Long>{
	Collection<Book> findAll(Example<Book> example);
}
