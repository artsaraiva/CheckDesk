/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.panes.MenuPane;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author arthu
 */
public class MainView
        extends Application
{

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;

        initComponents();
        scene.getStylesheets().add(ResourceLocator.getInstance().getStyleResource("default.css"));
        stage.setTitle("CheckDesk");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        //resize();
    }

    private void initComponents()
    {
        borderPane.setLeft(menuPane);
        menuPane.addEventHandler(MenuPane.Events.EVENT_SELECT, new EventHandler<Event>()
        {
            @Override
            public void handle(Event event)
            {
                borderPane.setCenter(menuPane.getSelectedPane());
            }
        });
    }

    private BorderPane borderPane = new BorderPane();
    private Scene scene = new Scene(borderPane);
    private MenuPane menuPane = new MenuPane();

}
