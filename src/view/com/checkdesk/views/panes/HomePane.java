/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.model.data.Survey;
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
        list.setItems(FXCollections.observableArrayList(
                new Survey(0, null, null, null, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(0, null, null, null, "Pesquisa Corporativa", "", new Date(), 0),
                new Survey(0, null, null, null, "Pesquisa de TCC", "", new Date(), 0)
        ));

        list2.setItems(FXCollections.observableArrayList(
                new Survey(0, null, null, null, "Pesquisa de teste", "", new Date(), 0)
        ));

        HBox.setHgrow(list, Priority.ALWAYS);
        HBox.setHgrow(list2, Priority.ALWAYS);
        getChildren().addAll(list, list2);
    }

    private HomeTable list = new HomeTable();
    private HomeTable list2 = new HomeTable();
}
