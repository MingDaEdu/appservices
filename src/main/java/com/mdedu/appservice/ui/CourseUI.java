package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import com.mdedu.appservice.domainobject.Course;
import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.repository.CourseRepository;
import com.mdedu.appservice.repository.GradeRepository;
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
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = CourseUI.NAME)
public class CourseUI extends VerticalLayout implements View {

	private static final String COLUMN_ID_VERSION_NAME = "version.name";

	private static final String COLUMN_ID_SUBJECT_NAME = "subject.name";

	private static final String COLUMN_ID_GRADE_NAME = "grade.name";

	/**
	 * 
	 */
	private static final long serialVersionUID = 5067676574666227924L;

	public static final String NAME = "course";

	private SubjectRepository subjectRepo;
	private GradeRepository gradeRepo;
	private VersionRepository versionRepo;

	private CourseRepository courseRepo;

	private Label label = new Label("课程管理");
	private Grid grid;
	private Button newCourseBtn;
	private CourseEditor editor;

	private Iterable<Subject> subjects;

	private Iterable<Version> versions;

	private Iterable<Grade> grades;

	private NativeSelect grade = new NativeSelect();
	
	private NativeSelect subject = new NativeSelect();
	
	private NativeSelect version = new NativeSelect();
	
	private  HeaderRow filteringHeader;
	
	@Autowired
	public CourseUI(SubjectRepository subjectRepo, GradeRepository gradeRepo, VersionRepository versionRepo,
			CourseRepository courseRepo, CourseEditor editor) {
		this.subjectRepo = subjectRepo;
		this.gradeRepo = gradeRepo;
		this.versionRepo = versionRepo;
		this.courseRepo = courseRepo;
		this.editor = editor;
		init();
	}

	private void init() {
		grid = new Grid();

		newCourseBtn = new Button("新增课程", FontAwesome.PLUS);

		grid.setColumns(COLUMN_ID_GRADE_NAME, COLUMN_ID_SUBJECT_NAME, COLUMN_ID_VERSION_NAME);
		grid.getColumn(COLUMN_ID_GRADE_NAME).setHeaderCaption("年级");
		grid.getColumn(COLUMN_ID_SUBJECT_NAME).setHeaderCaption("科目");
		grid.getColumn(COLUMN_ID_VERSION_NAME).setHeaderCaption("版本");
		
		versions=versionRepo.findAll();
		grades=gradeRepo.findAll();
		subjects=subjectRepo.findAll();
		
		HorizontalLayout couresView = new HorizontalLayout();

		BeanItemContainer<Grade> container = new BeanItemContainer<Grade>(Grade.class, (Collection<Grade>) grades);
		grade.setContainerDataSource(container);
		grade.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		grade.setItemCaptionPropertyId("name");
		grade.setNullSelectionAllowed(true);
		
		BeanItemContainer<Subject> subjectContainer = new BeanItemContainer<Subject>(Subject.class, (Collection<Subject>) subjects);
		subject.setContainerDataSource(subjectContainer);
		subject.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		subject.setItemCaptionPropertyId("name");
		subject.setNullSelectionAllowed(true);
		
		BeanItemContainer<Version> versionContainer = new BeanItemContainer<Version>(Version.class, (Collection<Version>) versions);
		version.setContainerDataSource(versionContainer);
		version.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		version.setItemCaptionPropertyId("name");
		version.setNullSelectionAllowed(true);
		ValueChangeListener l=new FilterValueChangeListener();
		grade.addValueChangeListener(l);
		subject.addValueChangeListener(l);
		version.addValueChangeListener(l);
		couresView.addComponents(grid, editor);
	
		editor.setVisible(false);
		editor.setGrades(grades);
		editor.setSubjects(subjects);
		editor.setVersions(versions);
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.edit((Course) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		newCourseBtn.addClickListener(e -> editor.edit(new Course()));
		couresView.setSpacing(true);
		setColumnFiltering(true);
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCourses();
		});
		listCourses();
		this.addComponents(label, newCourseBtn, couresView);
		this.setSpacing(true);
		this.setMargin(true);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	 private void setColumnFiltering(boolean filtered) {
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
	
	private void listCourses() {	
		BeanItemContainer<Course> c = null;
		if(grade.getValue()==null && subject.getValue()==null && version.getValue()==null){
			c = new BeanItemContainer<Course>(Course.class, (Collection<Course>) courseRepo.findAll());
		}else{
			Course course=new Course();
			course.setGrade((Grade)grade.getValue());
			course.setSubject((Subject)subject.getValue());
			course.setVersion((Version)version.getValue());
			c = new BeanItemContainer<Course>(Course.class, (Collection<Course>) courseRepo.findAll(Example.of(course)));
		}
		c.addNestedContainerBean("grade");
		c.addNestedContainerBean("subject");
		c.addNestedContainerBean("version");

		grid.setContainerDataSource(c);

	}
	
	class FilterValueChangeListener implements ValueChangeListener{

		/**
		 * 
		 */
		private static final long serialVersionUID = -938452467727268749L;

		@Override
		public void valueChange(ValueChangeEvent event) {
	
			listCourses();
		}
		
	}
}
