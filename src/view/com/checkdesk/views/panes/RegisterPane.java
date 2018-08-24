/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.views.parts.BrowseButton;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author MNicaretta
 */
public class RegisterPane
        extends DefaultPane
{
    private DefaultPane selectedPane = null;
    
    public RegisterPane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        selectedPane.setPrefSize(getWidth(), getHeight());
        selectedPane.resize();
    }

    @Override
    public void refreshContent()
    {
        selectPane(browsePane);
    }
    
    private void selectPane(DefaultPane pane)
    {
        selectedPane = pane;
        
        if (selectedPane == null)
        {
            selectedPane = browsePane;
        }
        
        selectedPane.refreshContent();
        getChildren().setAll(selectedPane);
        resize();
    }
    
    private void initComponents()
    {
        browsePane.setButtons(userButton, surveyButton, permissionButton, logsButton);
        
        getChildren().add(browsePane);
        
        browsePane.addEventHandler(BrowsePane.Events.ON_SELECT, (Event event) ->
        {
            selectPane(browsePane.getSelectedPane());
        });
    }

    private BrowsePane browsePane = new BrowsePane();
    private BrowseButton userButton = new BrowseButton(null, "Usuario", "login1.png");
    private BrowseButton surveyButton = new BrowseButton(null, "Formulário", "login1.png");
    private BrowseButton permissionButton = new BrowseButton(new PermissionPane(), "Permissões", "login1.png");
    private BrowseButton logsButton = new BrowseButton(null, "Logs", "login1.png");
}
