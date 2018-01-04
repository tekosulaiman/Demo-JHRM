package org.app.portofolio.webui;

//import org.module.rest.control.SecUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Legend;
import org.zkoss.chart.PlotLine;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.app.portofolio.webui.chart.LineBasicData;

public class DashBoardVM {
	
	@Wire("#chartTest")
	private Charts chartTest;
	
	//Test REstTemplate
	/*@Autowired
	private SecUserController secUserController;*/
	
	@AfterCompose
	public void initComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("event") Event event) throws Exception {
		
		Selectors.wireComponents(component, this, false);
		
		//System.out.println("secUserController >>>"+secUserController);
		
		System.out.println("Chart >>>"+chartTest);
		
		chartTest.setModel(LineBasicData.getCategoryModel());
        
		chartTest.getTitle().setX(-20);
    
		chartTest.getSubtitle().setX(-20);
    
		chartTest.getYAxis().setTitle("Temperature (�C)");
        PlotLine plotLine = new PlotLine();
        plotLine.setValue(0);
        plotLine.setWidth(1);
        plotLine.setColor("#808080");
        chartTest.getYAxis().addPlotLine(plotLine);

        chartTest.getTooltip().setValueSuffix("�C");

        Legend legend = chartTest.getLegend();
        legend.setLayout("vertical");
        legend.setAlign("right");
        legend.setVerticalAlign("middle");
        legend.setBorderWidth(0);
	}
}
