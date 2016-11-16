package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Course;
import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Publisher;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.repository.BookRepository;
import com.mdedu.appservice.repository.ChapterRespository;
import com.mdedu.appservice.repository.VideoRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class BookEditor extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1776347459122543301L;
	private BookRepository bookRepo;
	private ChapterRespository chapterRepo;
	private ChangeHandler h;

	private Iterable<Course> courses;

	private Iterable<Publisher> publishers;
	private Book book;

	private Iterable<Subject> subjects;

	private Iterable<Version> versions;

	private Iterable<Grade> grades;

	Button save = new Button("保存");
	Button cancel = new Button("取消");
	Button delete = new Button("删除");
	CssLayout actions = new CssLayout(save, cancel, delete);

	private TextField name = new TextField("书名:");
	private NativeSelect publisher = new NativeSelect("出版社：");

	private TextField uniqueId = new TextField("唯一编号:");

	private NativeSelect grade = new NativeSelect("年級：");

	private NativeSelect subject = new NativeSelect("科目：");

	private NativeSelect version = new NativeSelect("版本：");
	
	private ChapterEditor chapterEditor;
	private VideoRepository videoRepo;

	@Autowired
	public BookEditor(BookRepository bookRepo,ChapterRespository chapterRepo,VideoRepository videoRepo) {
		this.bookRepo = bookRepo;
		this.chapterRepo= chapterRepo;
		this.videoRepo= videoRepo;
		init();
	}

	private void init() {

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			bookRepo.save(book);
		});
		delete.addClickListener(e -> {
			ConfirmWindow deleteConfirm = new ConfirmWindow("删除教材", "删除 " + book.getName() + "?", "删除", "取消");
			deleteConfirm.setWidth("400px");
			deleteConfirm.setHeight("200px");
			deleteConfirm.setDecision(new Decision() {
				public void yes(Button.ClickEvent event) {
					bookRepo.delete(book);
					h.onChange();
				}
			});
		});
		cancel.addClickListener(e -> edit(book));
		VerticalLayout v = new VerticalLayout();
		v.addComponents(name, publisher, grade, subject, version, uniqueId, actions);
		v.setSpacing(true);
		chapterEditor=new ChapterEditor(chapterRepo,videoRepo);
		this.addComponents(v,chapterEditor);
	}

	public void edit(Book b) {

		final boolean persisted = b.getId() != null;
		if (persisted) {
			book = bookRepo.findOne(b.getId());
		} else {
			book = b;
		}
		BeanItemContainer<Publisher> container = new BeanItemContainer<Publisher>(Publisher.class, (Collection<Publisher>) publishers);
		publisher.setContainerDataSource(container);
		publisher.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		publisher.setItemCaptionPropertyId("name");
		publisher.setNullSelectionAllowed(true);

		BeanItemContainer<Subject> subjectContainer = new BeanItemContainer<Subject>(Subject.class,
				(Collection<Subject>) subjects);
		subject.setContainerDataSource(subjectContainer);
		subject.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		subject.setItemCaptionPropertyId("name");
		subject.setNullSelectionAllowed(false);
		
		BeanItemContainer<Grade> gradeContainer = new BeanItemContainer<Grade>(Grade.class,
				(Collection<Grade>) grades);
		grade.setContainerDataSource(gradeContainer);
		grade.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		grade.setItemCaptionPropertyId("name");
		grade.setNullSelectionAllowed(false);

		BeanItemContainer<Version> versionContainer = new BeanItemContainer<Version>(Version.class,
				(Collection<Version>) versions);
		version.setContainerDataSource(versionContainer);
		version.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		version.setItemCaptionPropertyId("name");
		version.setNullSelectionAllowed(false);
		
		BeanFieldGroup.bindFieldsUnbuffered(book, this);
		if(persisted){
		
			chapterEditor.init(book);
		}else{
			chapterEditor.setVisible(false);
		}
		setVisible(true);
	}

	public Iterable<Course> getCourses() {
		return courses;
	}

	public void setCourses(Iterable<Course> courses) {
		this.courses = courses;
	}

	public Iterable<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(Iterable<Publisher> publishers) {
		this.publishers = publishers;
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

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		this.h = h;
	}
}
