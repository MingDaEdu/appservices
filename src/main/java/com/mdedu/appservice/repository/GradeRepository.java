package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Grade;

@Repository
public interface GradeRepository extends CrudRepository<Grade,Long>{

}
