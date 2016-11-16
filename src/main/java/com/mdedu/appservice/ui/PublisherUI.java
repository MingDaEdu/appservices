package com.mdedu.appservice.ui;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.Publisher;
import com.mdedu.appservice.repository.PublisherRepository;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = PublisherUI.NAME)
public class PublisherUI extends VerticalLayout implements View {


	private static final long serialVersionUID = -6692781294666833366L;
	public static final String NAME = "publisher";
	private Label label = new Label("出版社管理");

	private Grid grid;
	private PublisherRepository repo;
	
	private PublisherEditor editor;

	private Button newBtn;


	@Autowired
	public PublisherUI(PublisherRepository repo,PublisherEditor editor) {
		this.repo = repo;
		this.editor= editor;
		init();
	}

	private void init() {
		HorizontalLayout subjects = initPublisher();


		this.addComponents( label, newBtn, subjects);
		this.setSpacing(true);
		this.setMargin(true);
	}

	

	private HorizontalLayout initPublisher() {
		grid = new Grid();

		HorizontalLayout subjects = new HorizontalLayout();

		newBtn = new Button("新增出版商");
		grid.setColumns("name","description");

		subjects.addComponents(grid, editor);
		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.editSetPoint((Publisher) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		newBtn.addClickListener(e -> editor.editSetPoint(new Publisher()));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listSubject();
		});
		listSubject();
		return subjects;
	}

	

	@Override
	public void enter(ViewChangeEvent event) {
		init();
	}

	private void listSubject() {
		BeanItemContainer<Publisher> c = null;
		c = new BeanItemContainer<Publisher>(Publisher.class, (Collection<Publisher>) repo.findAll());

		grid.setContainerDataSource(c);

	}

}
