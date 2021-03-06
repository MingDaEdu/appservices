package com.mdedu.appservice.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.Publisher;
import com.mdedu.appservice.repository.PublisherRepository;
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
public class PublisherEditor extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7516449930320804371L;

	private final PublisherRepository repo;

	/**
	 * The currently edited customer
	 */
	private Publisher bean;

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("名称");

	TextField description = new TextField("描述");
	// TextField ispn = new TextField("描述");
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	private ChangeHandler h;

	@Autowired
	public PublisherEditor(PublisherRepository repo) {

		this.repo = repo;
		addComponents(name, description, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {

			repo.save(bean);
		});
		delete.addClickListener(e -> {
			ConfirmWindow deleteConfirm = new ConfirmWindow("删除出版社", "确定要删除出版社： " + bean.getName() + "?", "删除", "取消");
			deleteConfirm.setWidth("400px");
			deleteConfirm.setHeight("200px");
			deleteConfirm.setDecision(new Decision() {
				public void yes(Button.ClickEvent event) {
					repo.delete(bean);
					h.onChange();
				}
			});
		});
		cancel.addClickListener(e -> editSetPoint(bean));
		setVisible(false);
	}

	public final void editSetPoint(Publisher c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			bean = repo.findOne(c.getId());
		} else {
			bean = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(bean, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		this.h = h;
	}

}
