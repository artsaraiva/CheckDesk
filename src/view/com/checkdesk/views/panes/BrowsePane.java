/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.views.parts.BrowseButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author arthu
 */
public class BrowsePane
        extends DefaultPane
{
    private List<BrowseButton> buttons = new ArrayList<>();
    
    public BrowsePane()
    {
        initComponents();
    }

    public void setButtons(BrowseButton... buttons)
    {
        setButtons(Arrays.asList(buttons));
    }

    public void setButtons(List<BrowseButton> buttons)
    {
        this.buttons = buttons;
        refreshContent();
    }
    
    @Override
    protected void resize()
    {
        hbox.setPrefSize(getWidth(), getHeight());
        int size = buttons.size();
        
        for (BrowseButton bt : buttons)
        {
            double width = (getWidth() / size) - 20;
            double height = getHeight();
            bt.setPrefSize(Math.min(width, height), Math.min(width, height));
        }
    }

    @Override
    public void refreshContent()
    {
        hbox.getChildren().setAll(buttons);
    }
    
    private void initComponents()
    {
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        
        hbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF0000"),
                                                        CornerRadii.EMPTY,
                                                        Insets.EMPTY)));
        
        getChildren().add(hbox);
    }
    
    private HBox hbox = new HBox();
}
