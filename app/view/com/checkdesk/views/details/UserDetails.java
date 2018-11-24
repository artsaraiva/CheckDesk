/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.details;

import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
import com.checkdesk.views.details.util.DetailsCaption;
import com.checkdesk.views.details.util.DetailsTable;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author arthu
 */
public class UserDetails
        extends DetailsPane<User>
{

    public UserDetails()
    {
        initComponents();
    }

    @Override
    public void setSource(int sourceId)
    {
        setSource(UserUtilities.getUser(sourceId));
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
                    new DetailsTable(75).addItem("Login", source.getLogin())
                                        .addItem("E-mail", source.getEmail())
                                        .addItem("Telefone", UserUtilities.maskPhone(source.getPhone()))
                                        .addItem("Tipo", UserUtilities.getType(source.getType())));
        }
    }

    private void initComponents()
    {
        getChildren().add(vbox);

    }

    private VBox vbox = new VBox();
}
