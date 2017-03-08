package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Chapter;
import com.mdedu.appservice.domainobject.Grade;
import com.mdedu.appservice.domainobject.Publisher;
import com.mdedu.appservice.domainobject.Subject;
import com.mdedu.appservice.domainobject.Version;
import com.mdedu.appservice.domainobject.Video;
import com.mdedu.appservice.repository.BookRepository;
import com.mdedu.appservice.repository.ChapterRespository;
import com.mdedu.appservice.repository.GradeRepository;
import com.mdedu.appservice.repository.SubjectRepository;
import com.mdedu.appservice.repository.VersionRepository;
import com.mdedu.appservice.repository.VideoRepository;
import com.mdedu.appservice.ui.util.HierarchicalBeanItemContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = VideoUI.NAME)
public class VideoUI extends HorizontalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8851897509657337860L;

	public static final String NAME = "videoui";

	private SubjectRepository subjectRepo;
	private GradeRepository gradeRepo;
	private VersionRepository versionRepo;

	private BookRepository bookRepo;

	private ChapterRespository chapterRepo;
	
	private VideoRepository videoRepo;

	private NativeSelect grade = new NativeSelect("年級：");

	private NativeSelect subject = new NativeSelect("科目:");

	private NativeSelect version = new NativeSelect("版本:");

	private VerticalLayout navTree = new VerticalLayout();
	private VerticalLayout mainPanel = new VerticalLayout();
	private FormLayout filterForm = new FormLayout();
	private VerticalLayout books = new VerticalLayout();

	private Label chLabel = new Label("");

	private Grid videoGrid = new Grid();
	private HorizontalLayout videoView = new HorizontalLayout();
	private VideoEditor editor;
	
	private Button newBtn;
	
	private Chapter chapter;

	@Autowired
	public VideoUI(SubjectRepository subjectRepo, GradeRepository gradeRepo, VersionRepository versionRepo,
			BookRepository bookRepo, ChapterRespository chapterRepo,VideoRepository videoRepo,
			VideoEditor editor) {
		this.subjectRepo = subjectRepo;
		this.gradeRepo = gradeRepo;
		this.versionRepo = versionRepo;
		this.bookRepo = bookRepo;
		this.chapterRepo = chapterRepo;
		this.videoRepo=videoRepo;
		this.editor=editor;
		init();
	}

	private void init() {
		setColumnFiltering(true);
		this.setSizeFull();
		this.setHeight("100%");
		filterForm.addComponents(grade, subject, version);
		filterForm.setMargin(false);
		filterForm.setCaption("书目过滤：");
		grade.addStyleName("inline-label");
		navTree.addComponents(filterForm, books);
		navTree.setWidth("200px");
		navTree.setHeight("100%");
		books.setSizeFull();
		newBtn = new Button("新增视频");
		videoGrid.setColumns("name","playedCount","seq","relatedSeq");
		videoView.addComponents(videoGrid,editor);
		mainPanel.addComponents(chLabel,newBtn, videoView);
		this.addComponents(navTree, mainPanel);
		mainPanel.setSizeFull();
		this.setExpandRatio(navTree, 1);
		this.setExpandRatio(mainPanel, 3);
		editor.setVisible(false);
		newBtn.addClickListener(e -> editor.edit(new Video()));
		videoGrid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.edit((Video) videoGrid.getSelectedRow());
			}
		});
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			reloadVideoView();
		});
		
		this.setSpacing(true);
		this.setMargin(true);

	}

	private void setColumnFiltering(boolean filtered) {
		BeanItemContainer<Grade> container = new BeanItemContainer<Grade>(Grade.class,
				(Collection<Grade>) gradeRepo.findAll());
		grade.setContainerDataSource(container);
		grade.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		grade.setItemCaptionPropertyId("name");
		grade.setNullSelectionAllowed(true);

		BeanItemContainer<Subject> subjectContainer = new BeanItemContainer<Subject>(Subject.class,
				(Collection<Subject>) subjectRepo.findAll());
		subject.setContainerDataSource(subjectContainer);
		subject.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		subject.setItemCaptionPropertyId("name");
		subject.setNullSelectionAllowed(true);

		BeanItemContainer<Version> versionContainer = new BeanItemContainer<Version>(Version.class,
				(Collection<Version>) versionRepo.findAll());
		version.setContainerDataSource(versionContainer);
		version.setItemCaptionMode(NativeSelect.ItemCaptionMode.PROPERTY);
		version.setItemCaptionPropertyId("name");
		version.setNullSelectionAllowed(true);
		ValueChangeListener l = new FilterValueChangeListener();
		grade.addValueChangeListener(l);
		subject.addValueChangeListener(l);
		version.addValueChangeListener(l);

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

	private void listBooks() {
		if (grade.getValue() == null && subject.getValue() == null && version.getValue() == null) {
			books.removeAllComponents();
		} else {
			Book example = new Book();
			example.setGrade((Grade) grade.getValue());
			example.setSubject((Subject) subject.getValue());
			example.setVersion((Version) version.getValue());
			books.removeAllComponents();
			bookRepo.findAll(Example.of(example)).forEach((book) -> {
				Tree chapterTree = new Tree(book.getFullName());
				HierarchicalBeanItemContainer<Chapter> c = new HierarchicalBeanItemContainer<Chapter>(Chapter.class,
						"parent");
				chapterTree.setContainerDataSource(c);
				chapterTree.setItemCaptionPropertyId("title");
				c.addAll(chapterRepo.findByBook(book));
				books.addComponent(chapterTree);
				c.rootItemIds().forEach((e) -> {
					chapterTree.expandItemsRecursively(e);
				});
				chapterTree.addValueChangeListener((e)->{
					if(e.getProperty().getValue()!=null){
						Chapter ch=(Chapter)e.getProperty().getValue();
						chapter=ch;
						reloadVideoView();
						
					}else{
						videoView.setVisible(false);
					}
				});
			});
		}

	}

	public void reloadVideoView(){
		BeanItemContainer<Video> v =new BeanItemContainer(Video.class,
				(Collection<Video>)videoRepo.findByChapter(chapter));
				videoGrid.setContainerDataSource(v);
				videoView.setVisible(true);
				chLabel.setCaption(chapter.getTitle()+" 视频列表");
				editor.setChapter(chapter);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}
