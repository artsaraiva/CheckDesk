/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
import com.checkdesk.views.parts.BrowseButton;
import com.checkdesk.views.parts.NavigationItem;
import com.checkdesk.views.pickers.ItemPicker;
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
        BrowseButton button = browsePane.getSelectedButton();
        
        if (button != null && button.getPane() != null)
        {
            try
            {
                DefaultPane pane = (DefaultPane) button.getPane().newInstance();

                return new NavigationItem(pane, browsePane.getSelectedButton().getTitle(), currentItem);
            }
            
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
        
        return null;
    }
    
    private void initComponents()
    {
        browsePane.setButtons(userButton, formButton, permissionButton, logsButton);
        
        getChildren().add(browsePane);
        
        browsePane.addEventHandler(BrowsePane.Events.ON_SELECT, (Event event) ->
        {
            fireEvent(new Event(Events.ON_CHANGE));
        });
    }

    private BrowsePane browsePane = new BrowsePane();
    private BrowseButton userButton = new BrowseButton(UserPane.class, "Usuario", "login1.png");
    private BrowseButton formButton = new BrowseButton(SurveyPane.class, "Pesquisas", "login1.png");
    private BrowseButton permissionButton = new BrowseButton(PermissionPane.class, "Permissões", "login1.png");
    private BrowseButton logsButton = new BrowseButton(LogPane.class, "Auditoria", "login1.png");
    
    private class UserPane
            extends DefaultPane
    {
        @Override
        protected void resize()
        {
        }

        @Override
        public void refreshContent()
        {
            picker.setItems(UserUtilities.getUsers());
            picker.open("Selecione um Usuário");
            
            if (picker.getSelected() != null)
            {
                UserUtilities.editUser(picker.getSelected());
            }
        }
        
        private ItemPicker<User> picker = new ItemPicker<>();
    }
    
    private class SurveyPane
            extends DefaultPane
    {
        @Override
        protected void resize()
        {
        }

        @Override
        public void refreshContent()
        {
            SurveyUtilities.addSurvey();
        }
    }
}
