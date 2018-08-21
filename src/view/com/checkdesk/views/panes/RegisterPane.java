/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.views.parts.BrowseButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

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
        selectedPane.refreshContent();
    }
    
    private void initComponents()
    {
        browsePane.setButtons(userButton, surveyButton, permissionButton);
        selectedPane = browsePane;
        
        getChildren().add(browsePane);
    }

    private BrowsePane browsePane = new BrowsePane();
    private BrowseButton userButton = new BrowseButton(null, "Usuario", "login1.png");
    private BrowseButton surveyButton = new BrowseButton(null, "Pesquisa", "login1.png");
    private BrowseButton permissionButton = new BrowseButton(null, "Permissões", "login1.png");
}
