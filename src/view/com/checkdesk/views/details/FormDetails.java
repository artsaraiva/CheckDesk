/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details;

import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Question;
import com.checkdesk.views.details.util.DetailsCaption;
import com.checkdesk.views.details.util.DetailsTable;
import com.checkdesk.views.details.util.Table;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class FormDetails
        extends DetailsPane<Form>
{

    public FormDetails()
    {
        initComponents();
    }

    @Override
    public void setSource(int sourceId)
    {
        setSource(FormUtilities.getForm(sourceId));
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
            vbox.getChildren().addAll(
                    new DetailsCaption(source.toString()),
                    new DetailsTable(75).addItemHtml("Informações", source.getInfo()));

            Table table = new Table("Pergunta", "Tipo", "Opções");

            for (Question question : FormUtilities.getQuestions(source))
            {
                table.addRow(question.getName(),
                        FormUtilities.getQuestionType(question.getType()),
                        question.getOptionId());
            }

            vbox.getChildren().add(table);
        }
    }

    private void initComponents()
    {
        getChildren().add(vbox);
    }

    private VBox vbox = new VBox();
}
