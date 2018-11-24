/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.Item;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

/**
 *
 * @author MNicaretta
 */
public class AnalysisController
{
    public static final int PIE_CHART = 0;
    
    public static final Chart createChart(int type, String title, List<Item> items)
    {
        Chart chart = null;
        
        switch (type)
        {
            case PIE_CHART:
                chart = createPieChart(title, items);
                break;
        }
        
        return chart;
    }
    
    private static final PieChart createPieChart(String title, List<Item> items)
    {
        PieChart chart = new PieChart();
        
        chart.setTitle(title);
        chart.setLabelLineLength(10);
        chart.setLegendSide(Side.BOTTOM);
        
        int max = 0;
        
        for (Item item : items)
        {
            max += item.getValue();
            PieChart.Data data = new PieChart.Data(item.getLabel(), item.getValue());
            chart.getData().add(data);
            
            Tooltip.install(data.getNode(), new Tooltip(String.format("%.0f", data.getPieValue())));
        }
        
        return chart;
    }
}
