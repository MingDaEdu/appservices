package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.Course;
import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.repository.CourseRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class CourseEditor extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1776347459122543301L;
	private CourseRepository courseRepo;
	private ChangeHandler h;

	private Iterable<Subject> subjects;

	private Iterable<Version> versions;

	private Iterable<Grade> grades;
	private Course course;

	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	private NativeSelect grade = new NativeSelect("年級：");

	private NativeSelect subject = new NativeSelect("科目：");

	private NativeSelect version = new NativeSelect("版本：");

	@Autowired
	public CourseEditor(CourseRepository courseRepo) {
		this.courseRepo = courseRepo;
		init();
	}

	private void init() {

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			courseRepo.save(course);
		});
		 delete.addClickListener(e -> {
		      ConfirmWindow deleteConfirm = new ConfirmWindow("删除课程",
		          "删除 " + course.getDisplayName() + "?", "删除", "取消");
		      deleteConfirm.setWidth("400px");
		      deleteConfirm.setHeight("200px");
		      deleteConfirm.setDecision(new Decision()
		      {
		        public void yes(Button.ClickEvent event)
		        {
		        	courseRepo.delete(course);
		        	h.onChange();
		        }
		      });
		    });
		    cancel.addClickListener(e -> edit(course));
		this.addComponents(grade, subject, version, actions);
		this.setSpacing(true);
	}

	public Iterable<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Iterable<Subject> subjects) {
		this.subjects = subjects;
	}

	public Iterable<Version> getVersions() {
		return versions;
	}

	public void setVersions(Iterable<Version> versions) {
		this.versions = versions;
	}

	public Iterable<Grade> getGrades() {
		return grades;
	}

	public void setGrades(Iterable<Grade> grades) {
		this.grades = grades;
	}

	public void edit(Course c) {

		final boolean persisted = c.getId() != null;
		if (persisted) {
			course = courseRepo.findOne(c.getId());
		} else {
			course = c;
		}
		BeanItemContainer<Grade> container = new BeanItemContainer<Grade>(Grade.class, (Collection<Grade>) grades);
		grade.setContainerDataSource(container);
		grade.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		grade.setItemCaptionPropertyId("name");
		grade.setNullSelectionAllowed(false);

		BeanItemContainer<Subject> subjectContainer = new BeanItemContainer<Subject>(Subject.class,
				(Collection<Subject>) subjects);
		subject.setContainerDataSource(subjectContainer);
		subject.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		subject.setItemCaptionPropertyId("name");
		subject.setNullSelectionAllowed(false);

		BeanItemContainer<Version> versionContainer = new BeanItemContainer<Version>(Version.class,
				(Collection<Version>) versions);
		version.setContainerDataSource(versionContainer);
		version.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		version.setItemCaptionPropertyId("name");
		version.setNullSelectionAllowed(false);
		BeanFieldGroup.bindFieldsUnbuffered(course, this);

		setVisible(true);
	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		this.h = h;
	}
}
