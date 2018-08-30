/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
import com.checkdesk.views.parts.BrowseButton;
import com.checkdesk.views.parts.ItemSelector;
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
        return new NavigationItem(browsePane.getSelectedButton().getPane(), browsePane.getSelectedButton().getTitle(), currentItem);
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
    private BrowseButton userButton = new BrowseButton(new UserPane(), "Usuario", "login1.png");
    private BrowseButton formButton = new BrowseButton(new SurveyPane(), "Pesquisas", "login1.png");
    private BrowseButton permissionButton = new BrowseButton(new PermissionPane(), "Permissões", "login1.png");
    private BrowseButton logsButton = new BrowseButton(new LogPane(), "Auditoria", "login1.png");
    
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
