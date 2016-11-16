package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission,Long>{

}
