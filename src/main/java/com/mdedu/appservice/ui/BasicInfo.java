package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.repository.GradeRepository;
import com.mdedu.appservice.repository.SubjectRepository;
import com.mdedu.appservice.repository.VersionRepository;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = BasicInfo.NAME)
public class BasicInfo extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7383839992016023287L;
	public static final String NAME = "basicinfo";
	private Label subjectLabel = new Label("科目设置");

	private Grid subjectGrid;

	private SubjectRepository subjectRepo;
	private GradeRepository gradeRepo;
	private VersionRepository versionRepo;

	private BaseInfoEditor<Subject> subjectEditor;

	private Button addNewSubjectBtn;

	private Label gradeLabel = new Label("年级设置");

	private Grid gradeGrid;

	private BaseInfoEditor<Grade> gradeEditor;

	private Button addNewGradeBtn;

	private Label versionLabel = new Label("版本设置");

	private Grid versionGrid;

	private BaseInfoEditor<Version> versionEditor;

	private Button addNewVersionBtn;

	@Autowired
	public BasicInfo(SubjectRepository subjectRepo, GradeRepository gradeRepo, VersionRepository versionRepo) {
		this.subjectRepo = subjectRepo;
		this.gradeRepo = gradeRepo;
		this.versionRepo = versionRepo;
		init();
	}

	private void init() {
		HorizontalLayout subjects = initSubject();

		HorizontalLayout grades = initGrade();
		HorizontalLayout versions = initVersion();

		this.addComponents(gradeLabel, addNewGradeBtn, grades, subjectLabel, addNewSubjectBtn, subjects, versionLabel,
				addNewVersionBtn, versions);
		this.setSpacing(true);
		this.setMargin(true);
	}

	private HorizontalLayout initVersion() {
		versionGrid = new Grid();
		HorizontalLayout versions = new HorizontalLayout();

		addNewVersionBtn = new Button("新增版本");
		versionGrid.setColumns("name");

		versionEditor = new BaseInfoEditor<Version>(versionRepo);
		versions.addComponents(versionGrid, versionEditor);
		// Connect selected Customer to editor or hide if none is selected
		versionGrid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				versionEditor.setVisible(false);
			} else {
				versionEditor.editSetPoint((Version) versionGrid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewVersionBtn.addClickListener(e -> versionEditor.editSetPoint(new Version()));

		// Listen changes made by the editor, refresh data from backend
		versionEditor.setChangeHandler(() -> {
			versionEditor.setVisible(false);
			listVersion();
		});
		listVersion();
		return versions;
	}

	private HorizontalLayout initSubject() {
		subjectGrid = new Grid();

		HorizontalLayout subjects = new HorizontalLayout();

		addNewSubjectBtn = new Button("新增科目");
		subjectGrid.setColumns("name");

		subjectEditor = new BaseInfoEditor<Subject>(subjectRepo);
		subjects.addComponents(subjectGrid, subjectEditor);
		// Connect selected Customer to editor or hide if none is selected
		subjectGrid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				subjectEditor.setVisible(false);
			} else {
				subjectEditor.editSetPoint((Subject) subjectGrid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewSubjectBtn.addClickListener(e -> subjectEditor.editSetPoint(new Subject()));

		// Listen changes made by the editor, refresh data from backend
		subjectEditor.setChangeHandler(() -> {
			subjectEditor.setVisible(false);
			listSubject();
		});
		listSubject();
		return subjects;
	}

	private HorizontalLayout initGrade() {
		gradeGrid = new Grid();

		addNewGradeBtn = new Button("新增年级");

		gradeGrid.setColumns("name");
		HorizontalLayout grades = new HorizontalLayout();

		gradeEditor = new BaseInfoEditor<Grade>(gradeRepo);
		grades.addComponents(gradeGrid, gradeEditor);
		gradeGrid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				gradeEditor.setVisible(false);
			} else {
				gradeEditor.editSetPoint((Grade) gradeGrid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewGradeBtn.addClickListener(e -> gradeEditor.editSetPoint(new Grade()));

		// Listen changes made by the editor, refresh data from backend
		gradeEditor.setChangeHandler(() -> {
			gradeEditor.setVisible(false);
			listGrade();
		});
		listGrade();
		return grades;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		init();
	}

	private void listVersion() {
		BeanItemContainer<Version> c = new BeanItemContainer<Version>(Version.class,
				(Collection<Version>) versionRepo.findAll());

		versionGrid.setContainerDataSource(c);

	}

	private void listGrade() {
		BeanItemContainer<Grade> c = null;
		c = new BeanItemContainer<Grade>(Grade.class, (Collection<Grade>) gradeRepo.findAll());

		gradeGrid.setContainerDataSource(c);

	}

	private void listSubject() {
		BeanItemContainer<Subject> c = null;
		c = new BeanItemContainer<Subject>(Subject.class, (Collection<Subject>) subjectRepo.findAll());

		subjectGrid.setContainerDataSource(c);

	}

}
