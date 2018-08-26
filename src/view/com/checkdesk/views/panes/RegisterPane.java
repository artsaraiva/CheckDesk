/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.parts.BrowseButton;
import com.checkdesk.views.parts.NavigationItem;
import javafx.event.Event;

/**
 *
 * @author MNicaretta
 */
public class RegisterPane
        extends DefaultPane
{
    public RegisterPane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        browsePane.setPrefSize(getWidth(), getHeight());
        browsePane.resize();
    }

    @Override
    public void refreshContent()
    {
        browsePane.refreshContent();
    }

    @Override
    public NavigationItem createNavigationItem(NavigationItem currentItem)
    {
        return new NavigationItem(browsePane.getSelectedButton().getPane(), browsePane.getSelectedButton().getTitle(), currentItem);
    }
    
    private void initComponents()
    {
        getStylesheets().add(ResourceLocator.getInstance().getStyleResource("registerview.css"));
        browsePane.setButtons(userButton, surveyButton, permissionButton, logsButton);
        
        getChildren().add(browsePane);
        
        browsePane.addEventHandler(BrowsePane.Events.ON_SELECT, (Event event) ->
        {
            fireEvent(new Event(Events.ON_CHANGE));
        });
    }

    private BrowsePane browsePane = new BrowsePane();
    private BrowseButton userButton = new BrowseButton(null, "Usuario", "login1.png");
    private BrowseButton surveyButton = new BrowseButton(null, "Formulário", "login1.png");
    private BrowseButton permissionButton = new BrowseButton(new PermissionPane(), "Permissões", "login1.png");
    private BrowseButton logsButton = new BrowseButton(new LogPane(), "Auditoria", "login1.png");
}
