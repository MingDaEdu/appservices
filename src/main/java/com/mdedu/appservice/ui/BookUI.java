package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.repository.BookRepository;
import com.mdedu.appservice.repository.CourseRepository;
import com.mdedu.appservice.repository.GradeRepository;
import com.mdedu.appservice.repository.PublisherRepository;
import com.mdedu.appservice.repository.SubjectRepository;
import com.mdedu.appservice.repository.VersionRepository;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.HeaderRow;

@SpringView(name = BookUI.NAME)
public class BookUI extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2310453521108263918L;
	public static final String NAME = "book";
	private static final String COLUMN_ID_VERSION_NAME = "version.name";

	private static final String COLUMN_ID_SUBJECT_NAME = "subject.name";

	private static final String COLUMN_ID_GRADE_NAME = "grade.name";

	private SubjectRepository subjectRepo;
	private GradeRepository gradeRepo;
	private VersionRepository versionRepo;

	
	private PublisherRepository publisherRepo;

	private BookRepository bookRepo;

	private Label label = new Label("教材管理");
	private Grid grid;
	private Button newBookBtn;
	private BookEditor editor;

	private NativeSelect grade = new NativeSelect();

	private NativeSelect subject = new NativeSelect();

	private NativeSelect version = new NativeSelect();

	private  HeaderRow filteringHeader;

	@Autowired
	public BookUI(SubjectRepository subjectRepo, GradeRepository gradeRepo, VersionRepository versionRepo,
			CourseRepository courseRepo,PublisherRepository publisherRepo, BookRepository bookRepo,BookEditor editor) {
		this.subjectRepo = subjectRepo;
		this.gradeRepo = gradeRepo;
		this.versionRepo = versionRepo;
		this.bookRepo = bookRepo;
		this.publisherRepo= publisherRepo;
		this.editor = editor;
		init();
	}

	private void init() {
		grid = new Grid();

		newBookBtn = new Button("新增教材", FontAwesome.PLUS);
		
		grid.setWidth("450px");
		grid.setHeight("600px");
		grid.setColumns("name", COLUMN_ID_SUBJECT_NAME,COLUMN_ID_GRADE_NAME, COLUMN_ID_VERSION_NAME,"publisher.name", "uniqueId");
		grid.getColumn("name").setHeaderCaption("教材名称");
		grid.getColumn("publisher.name").setHeaderCaption("出版社");
		grid.getColumn(COLUMN_ID_SUBJECT_NAME).setHeaderCaption("课程");
		grid.getColumn(COLUMN_ID_GRADE_NAME).setHeaderCaption("年级");
		grid.getColumn(COLUMN_ID_VERSION_NAME).setHeaderCaption("版本");
		grid.getColumn("uniqueId").setHeaderCaption("唯一编号");

		HorizontalLayout bookView = new HorizontalLayout();

		bookView.addComponents(grid, editor);
	
		editor.setGrades(gradeRepo.findAll());
		editor.setSubjects(subjectRepo.findAll());
		editor.setVersions(versionRepo.findAll());
		editor.setPublishers(publisherRepo.findAll());
		editor.setVisible(false);
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.edit((Book) grid.getSelectedRow());
			}
		});
		
	

		// Instantiate and edit new Customer the new button is clicked
		newBookBtn.addClickListener(e -> editor.edit(new Book("","")));
		bookView.setSpacing(true);
		setColumnFiltering(true);
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listBooks();
		});
		listBooks();

		this.addComponents(label, newBookBtn, bookView);
		this.setSpacing(true);
		this.setMargin(true);
		this.setWidthUndefined();
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	private void setColumnFiltering(boolean filtered) {
		BeanItemContainer<Grade> container = new BeanItemContainer<Grade>(Grade.class, (Collection<Grade>) gradeRepo.findAll());
		grade.setContainerDataSource(container);
		grade.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		grade.setItemCaptionPropertyId("name");
		grade.setNullSelectionAllowed(true);
		
		BeanItemContainer<Subject> subjectContainer = new BeanItemContainer<Subject>(Subject.class, (Collection<Subject>) subjectRepo.findAll());
		subject.setContainerDataSource(subjectContainer);
		subject.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		subject.setItemCaptionPropertyId("name");
		subject.setNullSelectionAllowed(true);
		
		BeanItemContainer<Version> versionContainer = new BeanItemContainer<Version>(Version.class, (Collection<Version>) versionRepo.findAll());
		version.setContainerDataSource(versionContainer);
		version.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		version.setItemCaptionPropertyId("name");
		version.setNullSelectionAllowed(true);
		ValueChangeListener l=new FilterValueChangeListener();
		grade.addValueChangeListener(l);
		subject.addValueChangeListener(l);
		version.addValueChangeListener(l);
		
		if (filtered && filteringHeader == null) {
            filteringHeader = grid.appendHeaderRow();	 
            filteringHeader.getCell(COLUMN_ID_GRADE_NAME).setComponent(grade);
            filteringHeader.getCell(COLUMN_ID_GRADE_NAME).setStyleName("filter-header");
            filteringHeader.getCell(COLUMN_ID_SUBJECT_NAME).setComponent(subject);
            filteringHeader.getCell(COLUMN_ID_SUBJECT_NAME).setStyleName("filter-header");
            filteringHeader.getCell(COLUMN_ID_VERSION_NAME).setComponent(version);
            filteringHeader.getCell(COLUMN_ID_VERSION_NAME).setStyleName("filter-header");
        } else if (!filtered && filteringHeader != null) {
            grid.removeHeaderRow(filteringHeader);
            filteringHeader = null;
        }
	}

	private void listBooks() {

		BeanItemContainer<Book> c = null;
		if(grade.getValue()==null && subject.getValue()==null && version.getValue()==null){
			c = new BeanItemContainer<Book>(Book.class, (Collection<Book>) bookRepo.findAll());
		}else{
			Book example=new Book();
			example.setGrade((Grade)grade.getValue());
			example.setSubject((Subject)subject.getValue());
			example.setVersion((Version)version.getValue());
			c = new BeanItemContainer<Book>(Book.class, (Collection<Book>) bookRepo.findAll(Example.of(example)));
		}
		c.addNestedContainerBean("publisher");
		c.addNestedContainerBean("grade");
		c.addNestedContainerBean("subject");
		c.addNestedContainerBean("version");
		grid.setContainerDataSource(c);

	}

	class FilterValueChangeListener implements ValueChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -938452467727268749L;

		@Override
		public void valueChange(ValueChangeEvent event) {

			listBooks();
		}

	}

}
