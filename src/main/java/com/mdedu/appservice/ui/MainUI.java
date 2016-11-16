package com.mdedu.appservice.ui;

import com.mdedu.appservice.domainobject.User;
import com.mdedu.appservice.utils.SpringContextHelper;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

@SpringView(name=MainUI.NAME)
public class MainUI extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -549933193542196079L;

	public static final String NAME="main";
	
	private Navigator navigator;

	private User user;
	
	private SpringContextHelper contextHelper;
	private void init() {
		
		contextHelper= new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		Header header=new Header(navigator);
		
		HorizontalLayout body = new HorizontalLayout();
		
		this.addComponents(header,body);
		Panel main=new Panel();
		
		Tree menu = new Tree("My Tree");
		

		
		menu.addItem("配置管理");

		menu.addItem("基本信息");
		menu.addItem("用户管理");
		menu.addItem("出版社管理");
		menu.setChildrenAllowed("基本信息", false);
		menu.setChildrenAllowed("用户管理", false);
		menu.setChildrenAllowed("出版社管理", false);
		menu.setParent("用户管理", "配置管理");
		menu.setParent("基本信息", "配置管理");

		menu.setParent("出版社管理", "配置管理");
		menu.addItem("课程管理");
		menu.addItem("教材管理");
		menu.addItem("视频管理");
		menu.setChildrenAllowed("课程管理", false);
		menu.setChildrenAllowed("教材管理", false);
		menu.setChildrenAllowed("视频管理", false);
		menu.addItemClickListener(new ItemClickListener(){

			
			private static final long serialVersionUID = 919978318267705959L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Item item=event.getItem();
				if(item.hashCode()=="基本信息".hashCode()){
					main.setContent(contextHelper.getBean(BasicInfo.class));
				}else if(item.hashCode()=="课程管理".hashCode()){
					main.setContent(contextHelper.getBean(CourseUI.class));
				}else if(item.hashCode()=="教材管理".hashCode()){
					main.setContent(contextHelper.getBean(BookUI.class));
				}else if(item.hashCode()=="出版社管理".hashCode()){
					main.setContent(contextHelper.getBean(PublisherUI.class));
				}else if (item.hashCode()=="视频管理".hashCode()){
					main.setContent(contextHelper.getBean(VideoUI.class));
				}
				else {
					main.setContent(null);
				}
			}});
		body.addComponents(menu,main);
		
		main.setSizeFull();
		body.setExpandRatio(main, 1);
		body.setSizeFull();
		MarginInfo marginInfo =new MarginInfo(10);
		this.setMargin(marginInfo );
	}

	@Override
	public void enter(ViewChangeEvent event) {
		navigator = event.getNavigator();
		user=(User)VaadinSession.getCurrent().getAttribute("user");
		if(user==null){
			navigator.navigateTo("");
		}
		else
		{
			init();
		}
	}

}
