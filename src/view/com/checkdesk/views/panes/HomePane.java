/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.data.User;
import com.checkdesk.views.parts.HomeTable;
import java.util.Arrays;
import java.util.Date;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author arthu
 */
public class HomePane
        extends DefaultPane
{
    public HomePane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        pendingList.setPrefWidth(getWidth() / 2);
        createdList.setPrefWidth(getWidth() / 2);
        pendingList.setPrefHeight(getHeight());
        createdList.setPrefHeight(getHeight());
    }

    @Override
    public void refreshContent()
    {
        User user1 = new User();
        user1.setLogin("login1");
        User user2 = new User();
        user2.setLogin("login2");
        User user3 = new User();
        user3.setLogin("login3");
        User user4 = new User();
        user4.setLogin("test_user");

        pendingList.setSurveys(Arrays.asList(
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0),
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0)
        ));

        createdList.setSurveys(Arrays.asList(
                new Survey(4, null, null, user4, "Pesquisa de IBGE", "", new Date(), 0)
        ));
    }

    private void initComponents()
    {
        getStylesheets().add(ResourceLocator.getInstance().getStyleResource("homeview.css"));
        pendingList.bindSelection(createdList);
        createdList.bindSelection(pendingList);

        HBox.setHgrow(pendingList, Priority.ALWAYS);
        HBox.setHgrow(createdList, Priority.ALWAYS);
        hbox.getChildren().addAll(pendingList, createdList);
        getChildren().add(hbox);
    }

    private HBox hbox = new HBox();
    private HomeTable pendingList = new HomeTable("Pesquisas pendentes");
    private HomeTable createdList = new HomeTable("Pesquisas criadas");
}
