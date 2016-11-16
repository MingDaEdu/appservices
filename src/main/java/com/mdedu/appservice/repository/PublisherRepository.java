package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Publisher;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher,Long>{

}
