/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.parts.NavigationItem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author MNicaretta
 */
public class NavigationPane
        extends ScrollPane
{
    public static class Events
    {
        public static final EventType ON_SELECT = new EventType("onNavigationSelect");
    }

    private static final String SEPARATOR_ICON = ResourceLocator.getInstance().getImageResource("ni_separator.png");

    private NavigationItem currentItem;
    private List<NavigationItem> items = new ArrayList<>();

    public NavigationPane()
    {
        this(null);
    }

    public NavigationPane(NavigationItem currentItem)
    {
        initComponents();
        setItem(currentItem);
    }

    public void setItem(NavigationItem item)
    {
        this.currentItem = item;
        updateItems();
    }

    public NavigationItem getItem()
    {
        return currentItem;
    }

    private void updateItems()
    {
        items.clear();
        hbox.getChildren().clear();

        if (currentItem != null)
        {
            items.add(currentItem);

            while (items.get(0).getPreviousItem() != null)
            {
                items.add(0, items.get(0).getPreviousItem());
            }
        }

        for (NavigationItem item : items)
        {
            ImageView imageView = new ImageView(new Image(SEPARATOR_ICON));
            imageView.setFitHeight(24);
            imageView.setFitWidth(24);

            HBox.setHgrow(item, Priority.ALWAYS);

            hbox.getChildren().addAll(item, imageView);
            item.setOnMouseClicked((MouseEvent t) ->
            {
                if (t.getButton() == MouseButton.PRIMARY)
                {
                    currentItem = item;
                    fireEvent(new Event(Events.ON_SELECT));
                }
            });
        }

        if (!hbox.getChildren().isEmpty())
        {
            hbox.getChildren().remove(hbox.getChildren().size() - 1);
        }

        fireEvent(new Event(Events.ON_SELECT));
    }

    private void resize()
    {
        hbox.setMinWidth(getWidth());
        hbox.setPrefHeight(getViewportBounds().getHeight());
    }

    private void initComponents()
    {
        hbox.getStyleClass().add("navigation-pane");

        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER_LEFT);

        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.NEVER);

        setPannable(true);
        setContent(hbox);

        widthProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
        {
            resize();
        });

        heightProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
        {
            resize();
        });

        addEventFilter(MouseEvent.ANY, (MouseEvent event) ->
        {
            if (event.getButton() != MouseButton.MIDDLE)
            {
                event.consume();
            }
        });
    }

    @Override
    public String toString()
    {
        return uuid;
    }
    
    private String uuid = UUID.randomUUID().toString();
    private HBox hbox = new HBox();
}
