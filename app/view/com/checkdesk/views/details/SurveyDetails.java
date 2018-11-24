/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details;

import com.checkdesk.control.util.CategoryUtilities;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.GroupUtilities;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.data.Survey;
import com.checkdesk.views.details.util.DetailsCaption;
import com.checkdesk.views.details.util.DetailsTable;
import com.checkdesk.views.details.util.Table;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class SurveyDetails
        extends DetailsPane<Survey>
{

    public SurveyDetails()
    {
        initComponents();
    }

    @Override
    public void setSource(int sourceId)
    {
        setSource(SurveyUtilities.getValue(sourceId));
    }

    @Override
    protected void resize()
    {
        vbox.setPrefSize(getWidth(), getHeight());

        for (Node node : vbox.getChildren())
        {
            if (node instanceof Region)
            {
                ((Region) node).setMaxWidth(getWidth());
            }
        }
    }

    @Override
    public void refreshContent()
    {
        vbox.getChildren().clear();

        if (source != null)
        {
            Form form = FormUtilities.getForm(source.getFormId());
            
            vbox.getChildren().addAll(
                    new DetailsCaption(source.toString()),
                    new DetailsTable(75).addItem("Data de criação", source.getCreatedDate())
                                        .addItem("Tipo", SurveyUtilities.getType(source.getType()))
                                        .addItem("Autor", UserUtilities.getUser(source.getOwnerId()))
                                        .addItem("Categoria", CategoryUtilities.getCategory(source.getCategoryId()))
                                        .addItem("Participantes", GroupUtilities.getGroup(source.getParticipantsId(), true))
                                        .addItemHtml("Informações", source.getInfo())
                                        .addChart("Formulário", FormUtilities.getQuestionChart(form)));
        }
    }

    private void initComponents()
    {
        getChildren().add(vbox);
    }

    private VBox vbox = new VBox();
}
