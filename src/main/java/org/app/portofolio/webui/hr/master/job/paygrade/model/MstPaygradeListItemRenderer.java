package org.app.portofolio.webui.hr.master.job.paygrade.model;

import org.app.portofolio.webui.hr.common.components.InlineListItemRenderer;
import org.module.hr.model.MstPaygrade;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class MstPaygradeListItemRenderer extends InlineListItemRenderer<MstPaygrade>{
	
	@Override
	public void renderListItem(Listitem item, MstPaygrade value, int index) throws Exception {	
		Textbox textboxPayGrade = new Textbox();
		textboxPayGrade.setValue(value.getPaygradeName());
		textboxPayGrade.setVisible(false);
		
		Textbox textboxCurrency = new Textbox();
		textboxCurrency.setValue("");
		textboxCurrency.setVisible(false);
		
		Label labelPayGrade = new Label();
		labelPayGrade.setValue(value.getPaygradeName());
		
		Label labelCurrency = new Label();
		labelCurrency.setValue("");
		
		setEditingComponents(textboxPayGrade, textboxCurrency);
		setLabelingComponents(labelPayGrade, labelCurrency);
		
		if (value.getIdPaygrade() == null) {
			textboxPayGrade.setVisible(true);
			textboxCurrency.setVisible(true);
			onAdding(true);
		}
		
		super.renderListItem(item, value, index);
		
		Listcell listcell = new Listcell();
			textboxCurrency.setParent(listcell);
			labelCurrency.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
			textboxCurrency.setParent(listcell);	
			labelCurrency.setParent(listcell);
		listcell.setParent(item);
		
		item.setValue(value);
	}
}