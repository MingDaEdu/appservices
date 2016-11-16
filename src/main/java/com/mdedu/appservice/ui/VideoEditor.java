package com.mdedu.appservice.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.Video;
import com.mdedu.appservice.repository.VideoRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class VideoEditor extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1776347459122543301L;
	private VideoRepository videoRepo;
	private ChangeHandler h;

	private Video video;

	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	private TextField name = new TextField("名称:");

	private TextField seq = new TextField("唯一编号:");
	
	private TextField relatedSeq = new TextField("关联编号:");
	
	private TextField description = new TextField("详细：");
	

	@Autowired
	public VideoEditor(VideoRepository videoRepo) {
		this.videoRepo = videoRepo;
		init();
	}

	private void init() {

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			videoRepo.save(video);
		});
		 delete.addClickListener(e -> {
		      ConfirmWindow deleteConfirm = new ConfirmWindow("删除视频",
		          "删除 " + video.getName() + "?", "删除", "取消");
		      deleteConfirm.setWidth("400px");
		      deleteConfirm.setHeight("200px");
		      deleteConfirm.setDecision(new Decision()
		      {
		        public void yes(Button.ClickEvent event)
		        {
		        	videoRepo.delete(video);
		        	h.onChange();
		        }
		      });
		    });
		    cancel.addClickListener(e -> edit(video));
		this.addComponents(name, description, seq,relatedSeq,actions);
		this.setSpacing(true);
	}


	public void edit(Video c) {

		final boolean persisted = c.getId() != null;
		if (persisted) {
			video = videoRepo.findOne(c.getId());
		} else {
			video = c;
		}
				BeanFieldGroup.bindFieldsUnbuffered(video, this);

		setVisible(true);
	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		this.h = h;
	}
}
