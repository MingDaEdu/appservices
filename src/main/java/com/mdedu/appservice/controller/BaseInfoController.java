package com.mdedu.appservice.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.repository.GradeRepository;
import com.mdedu.appservice.repository.SubjectRepository;
import com.mdedu.appservice.repository.VersionRepository;

@RestController
@RequestMapping("/baseinfo")
public class BaseInfoController {
	
	@Autowired
	private GradeRepository gradeRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private VersionRepository versionRepo;
	
	
	@RequestMapping(path="/grades", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public Collection<Grade> getGrades(){
		return (Collection<Grade>) gradeRepo.findAll();
	}
	@RequestMapping(path="/subjects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public Collection<Subject> getSubject(){
		return (Collection<Subject>) subjectRepo.findAll();
	}
	
	@RequestMapping(path="/versions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public Collection<Version> getVersion(){
		return (Collection<Version>) versionRepo.findAll();
	}
}
