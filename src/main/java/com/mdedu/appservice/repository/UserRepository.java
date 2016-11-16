package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long>{

	public User findByUsernameAndPassword(@Param("username")String username,@Param("password")String password);
}
