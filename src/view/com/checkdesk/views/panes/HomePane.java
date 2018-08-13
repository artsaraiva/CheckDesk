/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.model.data.Survey;
import com.checkdesk.model.data.User;
import com.checkdesk.views.parts.HomeTable;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author arthu
 */
public class HomePane
        extends HBox
{

    public HomePane()
    {
        initComponents();
    }

    private void initComponents()
    {
        User user1 = new User();
        user1.setLogin("login1");
        User user2 = new User();
        user2.setLogin("login2");
        User user3 = new User();
        user3.setLogin("login3");
        User user4 = new User();
        user4.setLogin("test_user");

        list.setItems(FXCollections.observableArrayList(
                new Survey(1, null, null, user1, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(2, null, null, user2, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(3, null, null, user3, "Pesquisa de TCC", "", new Date(), 0)
        ));

        list2.setItems(FXCollections.observableArrayList(
                new Survey(4, null, null, user4, "Pesquisa de IBGE", "", new Date(), 0)
        ));

        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(list2, Priority.ALWAYS);
        getChildren().addAll(list, list2);
    }

    private HomeTable list = new HomeTable();
    private HomeTable list2 = new HomeTable();
}
