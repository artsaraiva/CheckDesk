/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.views.panes.HeaderPane;
import com.checkdesk.views.panes.HomePane;
import com.checkdesk.views.panes.MenuPane;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author arthu
 */
public class MainView
        extends Application
{
    private Stage stage;

    @Override
    public void start( Stage stage ) throws Exception
    {
        this.stage = stage;

        initComponents();
        scene.getStylesheets().add( ResourceLocator.getInstance().getStyleResource( "default.css" ) );
        stage.setTitle( "CheckDesk" );
        stage.setScene( scene );
        stage.setMaximized( true );
        stage.show();

        //resize();
    }

    private void initComponents()
    {
        borderPane.setTop( headerPane );
        borderPane.setLeft( menuPane );
        borderPane.setCenter( homePane );
        
        menuPane.prefWidthProperty().bind(headerPane.getUserPaneWidth());
        
        menuPane.addEventHandler( MenuPane.Events.EVENT_SELECT, new EventHandler<Event>()
        {
            @Override
            public void handle( Event event )
            {
                Node selected = menuPane.getSelectedPane();
                borderPane.setCenter( selected );
            }

        } );
        
        stage.setOnCloseRequest((WindowEvent t) ->
        {
            try
            {
                EntityService.getInstance().close();
            }
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        });
    }

    private BorderPane borderPane = new BorderPane();
    private Scene scene = new Scene( borderPane );
    private MenuPane menuPane = new MenuPane();
    private HeaderPane headerPane = new HeaderPane();
    private HomePane homePane = new HomePane();
}
