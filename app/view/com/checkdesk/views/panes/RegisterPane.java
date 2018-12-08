/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
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
        for (BrowseButton button : buttons)
        {
            boolean p = false;
            if (!button.getRole().isEmpty())
            {
                p = !PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), button.getRole());
                button.getStyleClass().add("navigation-pane-button");
            }
            button.setDisable(p);
        }

        browsePane.setButtons(buttons);

        getChildren().add(browsePane);

        browsePane.addEventHandler(BrowsePane.Events.ON_SELECT, (Event event) ->
        {
            fireEvent(new Event(Events.ON_CHANGE));
        });
    }

    private BrowsePane browsePane = new BrowsePane();
    private BrowseButton[] buttons = new BrowseButton[]
    {
        new BrowseButton(new UserPane(), "Usuários", "bi_users.png", "view.users"),
        new BrowseButton(new FormPane(), "Formulários", "bi_forms.png", "view.forms"),
        new BrowseButton(new PermissionPane(), "Permissões", "bi_permissions.png", "view.permissions"),
        new BrowseButton(new LogPane(), "Auditorias", "bi_logs.png", "view.logs"),
        new BrowseButton(new ReleasePane(), "Releases", "bi_logs.png", "view.logs")
    };
}
