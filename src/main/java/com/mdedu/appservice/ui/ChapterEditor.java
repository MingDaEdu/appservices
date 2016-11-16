package com.mdedu.appservice.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdedu.appservice.domainobject.Book;
import com.mdedu.appservice.domainobject.Chapter;
import com.mdedu.appservice.domainobject.Item;
import com.mdedu.appservice.domainobject.Video;
import com.mdedu.appservice.repository.ChapterRespository;
import com.mdedu.appservice.repository.VideoRepository;
import com.mdedu.appservice.ui.util.HierarchicalBeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;

public class ChapterEditor extends VerticalLayout implements Handler, Receiver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5532326239701308568L;

	private Tree chapterTree;

	private ChapterRespository chapterRepo;
	
	private VideoRepository videoRepo;

	HierarchicalBeanItemContainer<Chapter> c = new HierarchicalBeanItemContainer<Chapter>(Chapter.class, "parent");;

	private Button addRoot = new Button("添加章节");

	private Button importRoot = new Button("导入章节");
	
	private TextField title = new TextField("标题：");
	private TextField seq = new TextField("编号：");
	private Button saveBtn = new Button("保存");

	private VerticalLayout form;
	
	private VerticalLayout importForm;
	private Upload upload ;
	private Book book;

	Chapter ch;

	public ChapterEditor(ChapterRespository chapterRepo,VideoRepository videoRepo) {
		this.chapterRepo = chapterRepo;
		this.videoRepo=videoRepo;
	}

	public void init(Book book) {
		chapterTree = new Tree(book.getName() + " 的章节");
		ch=null;
		this.book=book;
		form = new VerticalLayout();
		importForm = new VerticalLayout();
		importForm.setVisible(false);
		upload = new Upload("Upload it here", this);
		upload.setButtonCaption("Upload Now");
		upload.addSucceededListener((e)->{
			Notification.show("Upload success, processing file now...");
			
			
			Reader reader = null;
			try {
				ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
				reader = new InputStreamReader(bais,"UTF-8");
				Gson gs = new Gson();
				List<Item> items = gs.fromJson(reader, new TypeToken<List<Item>>() {
				}.getType());
				Map<String,Chapter> chapters=new HashMap<String,Chapter>();
				Map<String,Video> videos=new HashMap<String,Video>();
				items.forEach((element)->{
					if(element.getSequence()!=null){
						if(element.getUrl()!=null&&element.getUrl().equals("true")||element.getSequence().split("\\.").length>3){
							//new video;
							//element.getSequence().subSequence(0, element.getSequence().lastIndexOf("."));
							Video video= new Video();
							video.setName(element.getTitle());
							video.setSeq(element.getSequence());
							video.setRelatedSeq(element.getRelatedSeq());
							videos.put(element.getSequence(), video);						
						}else{
							Chapter ch= new Chapter();
							ch.setChildren(new ArrayList<Chapter>());
							ch.setTitle(element.getTitle());
							ch.setSeq(element.getSequence());
							ch.setBook(book);
							chapters.put(element.getSequence(),ch);
								//video.setChapter(chapter);
						}
					}					
				});
				Chapter ch;
				Chapter pCh;
				List<Chapter> rootChapters=new ArrayList<Chapter>();
				for(String key:chapters.keySet()){
					ch=chapters.get(key);
					int ind=key.lastIndexOf(".");
					if(ind==-1){
						rootChapters.add(ch);
					}else{
						pCh=chapters.get(key.substring(0, ind));
						ch.setParent(pCh);
						pCh.getChildren().add(ch);
					}
				}
				saveChapter(rootChapters);
				
				for(String key:videos.keySet()){
					int ind=key.lastIndexOf(".");
					pCh=chapters.get(key.substring(0, ind));
					Video v=videos.get(key);
					if(pCh==null){
						ind=v.getRelatedSeq().lastIndexOf(".");
						pCh= chapters.get(v.getRelatedSeq().substring(0,ind));
					}
					v.setChapter(pCh);
					videoRepo.save(v);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				if(reader!=null){
					try {
						reader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		upload.setWidth(200, Unit.PIXELS);
		importForm.addComponent(upload);
		chapterTree.setContainerDataSource(c);

		loadData(this.book);

		chapterTree.setItemCaptionPropertyId("title");
		chapterTree.addActionHandler(this);
		chapterTree.setWidth(200, Unit.PIXELS);
		chapterTree.addValueChangeListener((e) -> {

			ch = (Chapter) e.getProperty().getValue();
			if (ch != null) {
				title.setValue(ch.getTitle());
				seq.setValue(ch.getSeq());
				form.setVisible(true);
				importForm.setVisible(false);
			} else {
				form.setVisible(false);
			}
		});
		addRoot.addClickListener((event) -> {
			ch = new Chapter();
			title.setValue("");
			seq.setValue("");
			form.setVisible(true);
			importForm.setVisible(false);
		});

		importRoot.addClickListener((event) -> {
			form.setVisible(false);
			importForm.setVisible(true);
		});
		saveBtn.addClickListener((event) -> {
			if(ch.getId()!=null){
				ch=chapterRepo.findOne(ch.getId());
			}
			ch.setBook(this.book);
			ch.setTitle(title.getValue());
			ch.setSeq(seq.getValue());
			chapterRepo.save(ch);
			form.setVisible(false);

			loadData(this.book);

		});
		this.removeAllComponents();
		HorizontalLayout chapterView = new HorizontalLayout();

		form.addComponents(title, seq, saveBtn);
		form.setVisible(false);
		chapterView.setSpacing(true);
		chapterView.addComponents(chapterTree, form,importForm);
//		importRoot
		HorizontalLayout btns = new HorizontalLayout();
		btns.addComponents(addRoot,importRoot);
		btns.setSpacing(true);
		this.addComponents(btns, chapterView);
		this.setVisible(true);
	}

	private void saveChapter(List<Chapter> chapters) {
		for(Chapter root:chapters){
			chapterRepo.save(root);
			saveChapter(root.getChildren());
		}
	}

	private void loadData(Book book) {
		c.removeAllItems();
		c.addAll(chapterRepo.findByBook(book));
	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		Action[] result = new Action[2];

		result[0] = new Action("刪除");
		result[1] = new Action("添加子章节");
		return result;
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		if (action.getCaption().equals("刪除")) {
			c.removeItem(target);
			chapterRepo.delete((Chapter) target);
		} else if (action.getCaption().equals("编辑")) {
			ch = (Chapter) target;
			title.setValue(ch.getTitle());
			seq.setValue(ch.getSeq());
			form.setVisible(true);
			importForm.setVisible(false);
		} else {
			ch = new Chapter();
			ch.setParent((Chapter) target);
			title.setValue("");
			seq.setValue("");
			importForm.setVisible(false);
			form.setVisible(true);
		}
	}

	ByteArrayOutputStream baos;
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		baos =new ByteArrayOutputStream();
		return baos;
	}
}
