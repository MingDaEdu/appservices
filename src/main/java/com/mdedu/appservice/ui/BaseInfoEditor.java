package com.mdedu.appservice.ui;

import org.springframework.data.repository.CrudRepository;

import com.mdedu.appservice.domainobject.BaseInfo;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BaseInfoEditor<T extends BaseInfo> extends VerticalLayout
{
  /**
   * 
   */
  private static final long serialVersionUID = -7516449930320804371L;

  private final CrudRepository<T,Long> repo;

  /**
   * The currently edited customer
   */
  private T basebean;


  /* Fields to edit properties in Customer entity */
  TextField name = new TextField("Name");


  /* Action buttons */
  Button save = new Button("Save", FontAwesome.SAVE);
  Button cancel = new Button("Cancel");
  Button delete = new Button("Delete", FontAwesome.TRASH_O);
  CssLayout actions = new CssLayout(save, cancel, delete);
  
  private ChangeHandler h;

  public BaseInfoEditor(CrudRepository<T,Long> repo)
  {

	this.repo=repo;
    addComponents(name, actions);

    // Configure and style components
    setSpacing(true);
    actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    // wire action buttons to save, delete and reset
    save.addClickListener(e -> {
     
    	repo.save(basebean);
    });
    delete.addClickListener(e -> {
      ConfirmWindow deleteConfirm = new ConfirmWindow("Delete base info",
          "Delete the configure " + basebean.getName() + "?", "Delete", "Cancle");
      deleteConfirm.setWidth("400px");
      deleteConfirm.setHeight("200px");
      deleteConfirm.setDecision(new Decision()
      {
        public void yes(Button.ClickEvent event)
        {
        	repo.delete(basebean);
          h.onChange();
        }
      });
    });
    cancel.addClickListener(e -> editSetPoint(basebean));
    setVisible(false);
  }

  public final void editSetPoint(T c)
  {
    final boolean persisted = c.getId() != null;
    if (persisted)
    {
      // Find fresh entity for editing
      basebean = repo.findOne(c.getId());
    }
    else
    {
      basebean = c;
    }
    cancel.setVisible(persisted);

    // Bind customer properties to similarly named fields
    // Could also use annotation or "manual binding" or programmatically
    // moving values from fields to entities before saving
    BeanFieldGroup.bindFieldsUnbuffered(basebean, this);

    if (basebean.getName() == null)
    {
      basebean.setName("");
    }
    setVisible(true);

    // A hack to ensure the whole form is visible
    save.focus();
    // Select all text in firstName field automatically
    name.selectAll();
  }

  public void setChangeHandler(ChangeHandler h)
  {
    // ChangeHandler is notified when either save or delete
    // is clicked
    save.addClickListener(e -> h.onChange());
    this.h = h;
  }

}
