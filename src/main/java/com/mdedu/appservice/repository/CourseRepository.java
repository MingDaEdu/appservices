package com.mdedu.appservice.repository;

import java.util.Collection;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course,Long> {

	Collection<Course> findAll(Example<Course> example);

}
