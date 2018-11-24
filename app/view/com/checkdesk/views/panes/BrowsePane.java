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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author arthu
 */
public class BrowsePane
        extends DefaultPane
{

    public static class Events
    {

        public static final EventType ON_SELECT = new EventType("onBrowseSelect");
    }

    private List<BrowseButton> buttons = new ArrayList<>();
    private BrowseButton selectedButton;

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
        double width = (getWidth() / buttons.size()) - 20;
        double height = getHeight();

        double size = Math.min(width, height);

        for (BrowseButton bt : buttons)
        {
            bt.setPrefSize(size, size);
            bt.resize(size, size);
        }
    }

    @Override
    public void refreshContent()
    {
        for (BrowseButton bt : buttons)
        {
            bt.setOnMouseClicked(onClick);
        }

        hbox.getChildren().setAll(buttons);
    }

    public BrowseButton getSelectedButton()
    {
        return selectedButton;
    }

    private void initComponents()
    {
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(false);

        getChildren().add(hbox);
    }

    private HBox hbox = new HBox();
    private EventHandler<MouseEvent> onClick = (MouseEvent event) ->
    {
        selectedButton = (BrowseButton) event.getSource();

        fireEvent(new Event(Events.ON_SELECT));
    };
}
