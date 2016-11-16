package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Subject;

@Repository
public interface SubjectRepository extends CrudRepository<Subject,Long>{

}
