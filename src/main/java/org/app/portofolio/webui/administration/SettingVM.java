package org.app.portofolio.webui.administration;

import java.util.Collection;
import java.util.List;

import org.app.portofolio.webui.hr.common.utilities.ComponentConditionUtil;
import org.module.hr.model.MstSetting;
import org.module.hr.service.AdministrationService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

public class SettingVM {
	
	/*------------ Zul -----------*/
	@Wire("#buttonSaveSystemSetting")
	private Button buttonSaveSystemSetting;
	@Wire("#buttonEditSystemSetting")
	private Button buttonEditSystemSetting;
	@Wire("#gridRowsSettings")
	private Rows gridRowsSettings;
	
	/*--------- Services ---------*/
	@WireVariable
	private AdministrationService administrationService;
	
	/*--------- Beans ---------*/
	private List<MstSetting> listSetting;
	private Boolean editingMode;
	
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component, @ExecutionArgParam("object") Object object) {
		Selectors.wireComponents(component, this, false);
		
		refreshAfterSave();
	}
	
	@Command
	@NotifyChange("editingMode")
	public void doEdit() {
		ComponentConditionUtil.invisibleButton(buttonEditSystemSetting);
		ComponentConditionUtil.visibleButton(buttonSaveSystemSetting);
		this.editingMode = Boolean.TRUE;
	}
	
	@Command
	public void doSave() throws Exception {
		administrationService.saveAll(listSetting);
		Messagebox.show("Success !");
		refreshAfterSave();
	}
	
	@NotifyChange({"editingMode", "listSetting"})
	public void refreshAfterSave(){
		this.listSetting = administrationService.getAllSettings();
		
		this.editingMode = Boolean.FALSE;
		ComponentConditionUtil.invisibleButton(buttonSaveSystemSetting);
		ComponentConditionUtil.visibleButton(buttonEditSystemSetting);
		
	}

	
	/* ----------- GETTER - SETTER -----------*/
	public List<MstSetting> getListSetting() {
		return listSetting;
	}
	
	public void setListSetting(List<MstSetting> listSetting) {
		this.listSetting = listSetting;
	}
	
	public void setEditingMode(boolean editingMode) {
		this.editingMode = editingMode;
	}
	
	public Boolean getEditingMode() {
		return editingMode;
	}
	
}
