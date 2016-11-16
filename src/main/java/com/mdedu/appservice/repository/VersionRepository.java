package com.mdedu.appservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdedu.appservice.domainobject.Version;

@Repository
public interface VersionRepository extends CrudRepository<Version,Long>{

}
