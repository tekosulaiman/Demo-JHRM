package org.app.portofolio.webui.security.right.model;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import org.module.sysadmin.model.SecRight;
import org.module.sysadmin.model.dto.SecType;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecRightListModelItemRenderer implements ListitemRenderer<SecRight>{

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecurityService securityService = (SecurityService) SpringUtil.getBean("securityService");		
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final SecRight secRight, int index) throws Exception {
		Listcell listcell;
		
		/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Wire component
		 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
		final Button buttonSave = new Button();
    		buttonSave.setImage("/images/icons/btn_save.gif");
		
		final Button buttonEdit = new Button();
			buttonEdit.setImage("/images/icons/btn_edit.gif");
			
		final Button buttonDelete = new Button();
        	buttonDelete.setImage("/images/icons/btn_delete.gif");
        
        final Button buttonCancel = new Button();
        	buttonCancel.setImage("/images/icons/btn_cancel.gif");
        	
        final Label labelRightName = new Label();			        
        final Label labelRightType = new Label();
        
        final Textbox textboxRightName = new Textbox();
        final Combobox comboBoxType = new Combobox();
        	
        listcell = new Listcell();
        	buttonEdit.setParent(listcell); 
        	buttonSave.setParent(listcell); 
        	buttonCancel.setParent(listcell); 
        	buttonDelete.setParent(listcell); 
		listcell.setParent(item);
		
		listcell = new Listcell(String.valueOf(secRight.getRigId()!= null ? secRight.getRigId(): ""));
		listcell.setParent(item);
        
		listcell = new Listcell();
			textboxRightName.setParent(listcell); 
			labelRightName.setParent(listcell); 
		listcell.setParent(item);

		listcell = new Listcell();
		comboBoxType.setParent(listcell); 
			labelRightType.setParent(listcell);
		listcell.setParent(item);

		if(secRight.getRigId() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
			
			comboBoxType.setModel(new ListModelList<SecType>(securityService.getAllTypes()));
        	comboBoxType.setItemRenderer(new SecTypeListModelItemRenderer());

		}else{
        	buttonSave.setVisible(false);
        	buttonCancel.setVisible(false);
        	buttonDelete.setVisible(false);
        	
        	SecType type = securityService.getTypeById(secRight.getRigType().intValue());
        	
        	labelRightName.setValue(secRight.getRigName());
        	labelRightType.setValue(type.getStpTypname());
        	
        	textboxRightName.setVisible(false);
        	comboBoxType.setVisible(false);
		}
		
		/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Function CRUD Event
		 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
		buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(secRight.getRigId() == null){
					secRight.setRigName(textboxRightName.getValue());
					secRight.setRigType(comboBoxType.getSelectedIndex());

					securityService.save(secRight);
					
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				}else{
					secRight.setRigName(textboxRightName.getValue());
					secRight.setRigType(comboBoxType.getSelectedIndex());

					securityService.update(secRight);
					
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				}
			}
		});
		
		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {		
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);
				
				textboxRightName.setVisible(true);
				comboBoxType.setVisible(true);
				
				labelRightName.setVisible(false);
				labelRightType.setVisible(false);
				
				textboxRightName.setValue(secRight.getRigName());
				
				SecType type = securityService.getTypeById(secRight.getRigType().intValue());
				
				comboBoxType.setModel(new ListModelList<SecType>(securityService.getAllTypes()));
	        	comboBoxType.setItemRenderer(new SecTypeListModelItemRenderer());
				comboBoxType.setValue(type.getStpTypname());
			}					
		});
		
		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				System.out.println("ID >>>"+secRight.getRigId());
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
				    public void onEvent(Event event) throws Exception {    	
				 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {

				 			securityService.delete(secRight);
				 			
				 			BindUtils.postGlobalCommand(null, null, "refreshAfterSave", null);
				 		}else{
				 			return;
				 		}
				 	}
				});
			}
		});
		
		buttonCancel.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
			}
		});
	}
}