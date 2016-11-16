package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{

}
