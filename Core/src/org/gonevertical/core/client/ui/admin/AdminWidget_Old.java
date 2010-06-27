package org.gonevertical.core.client.ui.admin;

import org.gonevertical.core.client.ClientPersistence;
import org.gonevertical.core.client.global.QueryString;
import org.gonevertical.core.client.global.QueryStringData;
import org.gonevertical.core.client.ui.login.CreateUserAccount;
import org.gonevertical.core.client.ui.login.CreateUserAccount;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminWidget_Old extends Composite implements ValueChangeHandler<String>, ChangeHandler {

  private ClientPersistence cp = null;

  private int changeEvent = 0;
  
  private VerticalPanel pWidget = new VerticalPanel();

  private VerticalPanel pMenu = new VerticalPanel();
  
  // stick the stuff in here
  private VerticalPanel pContent = new VerticalPanel();
  
  private AdminTabs wTabs = null;
  
  // widgets
  private CreateUserAccount wCreate = null;
  
  /**
   * init Widget
   * 
   * @param cp
   */
  public AdminWidget_Old(ClientPersistence cp) {
    this.cp = cp;
  
    wTabs = new AdminTabs(cp);
    
    //pWidget.add(pMenu);
    pWidget.add(pContent);
    pWidget.add(wTabs);
    
    initWidget(pWidget);
    
    // set defaults
    this.setVisible(false);
    
    initHistory();
    
    cp.addChangeHandler(this);
    
    pWidget.setWidth("100%");
    pContent.setWidth("100%");
    
    pMenu.addStyleName("core-Account-Menu");
    
    drawMenu();
    

    // register bread crumbs that are used in this widget
    cp.setBreadCrumbCategory("Account", "account");
    
    cp.setBreadCrumb("Create", "core_create");
    cp.setBreadCrumb("Admin", "core_home");
    cp.setBreadCrumb("Things", "core_things");
    cp.setBreadCrumb("Thing Stuff Types", "core_stufftype");
    cp.setBreadCrumb("Thing Types", "core_types");
    
    
    //pWidget.addStyleName("test1");
    
  }
  
	public boolean getMatchHistoryToken() {
	  boolean b = false;
	  String historyToken = History.getToken();
	  if (historyToken.matches("^core_.*?$") == true) {
	  	b = true;
	  }
	  return b;
  }
  
  private void initHistory() {
    History.addValueChangeHandler(this);
  }
  
  private void drawMenu() {
    Hyperlink hprofile = new Hyperlink("My Profile", "core_profile");
    
    HorizontalPanel hp = new HorizontalPanel();
    hp.add(hprofile);
   
    pMenu.clear();
    pMenu.add(hp);
  }
  
  /**
   * draw the widgets according to the url 
   * 
   * account_Create - create account
   * account_Profile - display profile
   * account_Wizard - wizard creation of account
   * @param event 
   * 
   */
  public void draw() {
    
    QueryStringData qsd = QueryString.getQueryStringData();
    
    String historyToken = qsd.getHistoryToken();
    if (historyToken.matches("core_.*") != true) {
      this.setVisible(false);
      return;
    }
    
    this.setVisible(true);
    
    if (qsd.getHistoryToken().equals("core_create")) { // create account
      pContent.setVisible(true);
      wTabs.setVisible(false);
      
      drawCreateAccount();
      
    } else {
      pContent.setVisible(false);
      wTabs.setVisible(true);
      wTabs.draw();
    }

  }
  
  private void drawCreateAccount() {
    pContent.clear();
    wCreate = new CreateUserAccount(cp);
    pContent.add(wCreate);
    pContent.setCellHorizontalAlignment(wCreate, HorizontalPanel.ALIGN_CENTER);
  }
  

  public int getChangeEvent() {
    return changeEvent;
  }
  
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  public void onValueChange(ValueChangeEvent<String> event) {
    draw();
  }

  public void onChange(ChangeEvent event) {
    
    Widget sender = (Widget) event.getSource();
    if (sender == cp) {
      draw();
    }
  }



}