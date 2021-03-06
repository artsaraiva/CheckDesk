/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ConfigurationManager;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.util.ReleaseUtilities;
import com.checkdesk.views.panes.DefaultPane;
import com.checkdesk.views.panes.HeaderPane;
import com.checkdesk.views.panes.LoggedUserPane;
import com.checkdesk.views.panes.MenuPane;
import com.checkdesk.views.panes.NavigationPane;
import com.checkdesk.views.parts.MenuItem;
import com.checkdesk.views.parts.NavigationItem;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author arthu
 */
public class MainView
        extends Application
{

    private Stage stage;
    private Set<DefaultPane> panesCache = new HashSet<>();

    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;

        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
        {
            ApplicationController.logException(e);
        });

        initComponents();
        scene.getStylesheets().add(ResourceLocator.getInstance().getStyleResource("default.css"));
        stage.setTitle("CheckDesk");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        ApplicationController.getInstance().setRootWindow(stage);
    }

    private void selectMenuItem(MenuItem selected)
    {
        headerPane.setNavigationItem(new NavigationItem(selected.getPane(), selected.getName()));
    }

    private void setCenter(final DefaultPane pane)
    {
        if (pane != null)
        {
            if (!panesCache.contains(pane))
            {
                pane.addEventHandler(DefaultPane.Events.ON_CHANGE, (Event t) ->
                {
                    headerPane.setNavigationItem(pane.createNavigationItem(headerPane.getNavigationItem()));
                });

                panesCache.add(pane);
            }

            pane.refreshContent();
        }

        borderPane.setCenter(pane);
    }

    public void showRealeaseFile()
    {
        ReleaseUtilities.showRelease();
    }

    private void initComponents()
    {
        borderPane.setTop(headerPane);
        borderPane.setLeft(menuPane);

        menuPane.prefWidthProperty().bind(headerPane.getUserPaneWidth());

        borderPane.prefWidthProperty().bind(stage.widthProperty());
        borderPane.prefHeightProperty().bind(stage.heightProperty());

        headerPane.prefWidthProperty().bind(borderPane.widthProperty());

        menuPane.addEventHandler(MenuPane.Events.EVENT_SELECT, (Event event) ->
        {
            selectMenuItem(menuPane.getSelectedItem());
        });

        headerPane.addEventHandler(NavigationPane.Events.ON_SELECT, (Event event) ->
        {
            DefaultPane pane = null;
            NavigationItem item = headerPane.getNavigationItem();

            if (item != null)
            {
                pane = headerPane.getNavigationItem().getPane();

                if (pane != null)
                {
                    pane.setContext(item.getContext());
                }
            }

            setCenter(pane);
        });

        headerPane.addEventHandler(LoggedUserPane.Events.LOG_OUT, (Event event) ->
        {
            try
            {
                new LoginView().start(new Stage());
                ApplicationController.getInstance().reset();
                stage.close();
            }

            catch (Exception e)
            {
                ApplicationController.getInstance().logException(e);
            }
        });

        stage.setOnCloseRequest((WindowEvent t) ->
        {
            try
            {
                ApplicationController.getInstance().close();
            }
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        });

        menuPane.refreshContent();
        headerPane.refreshContent();
    }

    private BorderPane borderPane = new BorderPane();
    private Scene scene = new Scene(borderPane);
    private MenuPane menuPane = new MenuPane();
    private HeaderPane headerPane = new HeaderPane();
}
