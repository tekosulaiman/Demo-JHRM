package org.app.portofolio.webui.hr.common.components;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public abstract class InlineListItemRenderer<E> {
	
	protected Button buttonEdit;
	protected Button buttonSave;
	protected Button buttonCancel;
	protected Button buttonDelete;
	public Renderer renderer;
	private Component[] editingComponents;
	private Component[] labelingComponents;
	
	public static final String SELECTED_TRANSACTION_VALUE = "selectedTransactionValue"; 
	
	/**
	 * 
	 */
	public InlineListItemRenderer() {
		initComponents();
	}

	/**
	 * 
	 */
	private void initComponents() {
		renderer = new Renderer();
	}
	
	/**
	 * 
	 */
	protected void buttonEditActionListener() {
		
	}
	
	/**
	 * 
	 */
	protected void buttonSaveActionListener() {
		
	}
	
	/**
	 * 
	 */
	protected void buttonDeleteActionListener () {
		
	}
	
	/**
	 * 
	 */
	protected void buttonCancelActionListener () {
		
	}

	private boolean adding = false;
	
	public void onAdding(boolean adding) {
		this.adding = adding;
	}
	
	public boolean isAdding() {
		return this.adding;
	}
	
	public void normalizeButtons(Button...buttons) {
		buttons[0].setVisible(true); // edit
		buttons[1].setVisible(false); // save
		buttons[2].setVisible(false); // cancel
		buttons[3].setVisible(false); // delete
	}
	
	public void addingButtonCondition(Button...buttons){
		buttons[0].setVisible(false); //edit
		buttons[1].setVisible(true); // save
		buttons[2].setVisible(true); // cancel
		buttons[3].setVisible(false); // delete
	}
	
	public void editingButtonCondition(Button...buttons){
		buttons[0].setVisible(false); //edit
		buttons[1].setVisible(true); // save
		buttons[2].setVisible(false); // cancel
		buttons[3].setVisible(true); // delete
	}
	
	
	
	/**
	 * @param <E>
	 * 
	 */
	public class Renderer implements ListitemRenderer<E>{
		
		@Override
		public void render(Listitem arg0, E arg1, int arg2) throws Exception {
			renderListItem(arg0, arg1, arg2);			
		}
		
	}
	
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws Exception
	 */
	public void renderListItem(Listitem item, final E e, int index) throws Exception{
		Listcell listcell = new Listcell();
		
		final Component[] editingComponents = getEditingComponents();
		final Component[] labelingComponents = getLabelingComponents();
		
    	final Button buttonSave = new Button();
    	final Button buttonEdit = new Button();
		final Button buttonDelete = new Button();
		final Button buttonCancel = new Button();
    	
		buttonSave.setImage("/images/icons/btn_save.gif");
		buttonSave.setParent(listcell);
		buttonSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				buttonSaveActionListener();
				for(Component component : editingComponents) {
					component.setVisible(false);
				}
				for(Component component : labelingComponents) {
					component.setVisible(true);
				}
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				normalizeButtons(buttonEdit, buttonSave, buttonCancel, buttonDelete);
			}
		});
		
		buttonEdit.setImage("/images/icons/btn_edit.gif");
		buttonEdit.setParent(listcell);
		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				buttonEditActionListener();
				for(Component component : editingComponents) {
					component.setVisible(true);
				}
				for(Component component : labelingComponents) {
					component.setVisible(false);
				}
				Map<String, Object> args = new HashMap<>();
				args.put(SELECTED_TRANSACTION_VALUE, e);
				BindUtils.postGlobalCommand(null, null, "setTransactionValue", args);
				editingButtonCondition(buttonEdit, buttonSave, buttonCancel, buttonDelete);
			}
		});
		
		
		buttonDelete.setImage("/images/icons/btn_delete.gif");
		buttonDelete.setParent(listcell);
		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				buttonDeleteActionListener();
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
			}
		});
		
		
		buttonCancel.setImage("/images/icons/btn_cancel.gif");
		buttonCancel.setParent(listcell);
		buttonCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				buttonCancelActionListener();
				for(Component component : editingComponents) {
					component.setVisible(false);
				}
				for(Component component : labelingComponents) {
					component.setVisible(true);
				}
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				normalizeButtons(buttonEdit, buttonSave, buttonCancel, buttonDelete);
			}
		});
    	
    	listcell.setParent(item);
    	
    	normalizeButtons(buttonEdit, buttonSave, buttonCancel, buttonDelete);
    	if (isAdding()) {
    		addingButtonCondition(buttonEdit, buttonSave, buttonCancel, buttonDelete);
    	} 
	}

	public Component[] getEditingComponents() {
		return editingComponents;
	}

	public void setEditingComponents(Component... editingComponents) {
		this.editingComponents = editingComponents;
	}

	public Component[] getLabelingComponents() {
		return labelingComponents;
	}

	public void setLabelingComponents(Component... labelingComponents) {
		this.labelingComponents = labelingComponents;
	}

	public Renderer getRenderer(){
		return renderer; 
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
}
