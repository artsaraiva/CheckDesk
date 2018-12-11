/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.Answer;
import com.checkdesk.model.data.Entity;
import com.checkdesk.model.data.Survey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author arthu
 */
public class HomeTable
        extends VBox
{
    public static final EventType SELECT = new EventType("onSelect");
    public static final EventType EDIT = new EventType("onEdit");

    private String title;

    private HomeTableItem selected;
    private Set<HomeTable> bindedTables = new HashSet<>();

    public HomeTable()
    {
        this(null);
    }

    public HomeTable(String title)
    {
        initComponents();

        this.title = title;
        updateTitle();
    }

    public void setTitle(String title)
    {
        this.title = title;
        updateTitle();
    }

    public void setAnswers(List<Answer> answers)
    {
        getVBoxChildren().clear();

        if (answers != null)
        {
            for (Answer a : answers)
            {
                getVBoxChildren().add(createItem(a));
            }
        }

        updateTitle();
        HBox.setHgrow(vbox, Priority.ALWAYS);
    }

    public void setSurveys(List<Survey> surveys)
    {
        getVBoxChildren().clear();

        if (surveys != null)
        {
            for (Survey s : surveys)
            {
                getVBoxChildren().add(createItem(s));
            }
        }

        updateTitle();
        HBox.setHgrow(vbox, Priority.ALWAYS);
    }

    public void bindSelection(HomeTable table)
    {
        bindedTables.add(table);
    }

    private HomeTableItem createItem(Answer answer)
    {
        final HomeTableItem item = new HomeTableItem(answer);
        item.getStyleClass().add("home-cell");
        
        scrollPane.prefWidthProperty().bind(widthProperty());
        scrollPane.prefHeightProperty().bind(heightProperty());
        
        scrollPane.viewportBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) ->
        {
            item.setPrefWidth(scrollPane.getViewportBounds().getWidth());
        });
        
        item.setOnMouseClicked((MouseEvent event) ->
        {
            if (event.getClickCount() == 2)
            {
                this.selected = item;
                fireEvent(new Event(EDIT));
            }
            
            else
            {
                setSelected(item);
            }
        });
        
        return item;
    }

    private HomeTableItem createItem(Survey survey)
    {
        final HomeTableItem item = new HomeTableItem(survey);
        item.getStyleClass().add("home-cell");
        
        scrollPane.prefWidthProperty().bind(widthProperty());
        scrollPane.prefHeightProperty().bind(heightProperty());
        
        scrollPane.viewportBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) ->
        {
            item.setPrefWidth(scrollPane.getViewportBounds().getWidth());
        });
        
        item.setOnMouseClicked((MouseEvent event) ->
        {
            setSelected(item);
        });
        
        return item;
    }

    protected ObservableList<Node> getVBoxChildren()
    {
        return vbox.getChildren();
    }

    public void setSelected(HomeTableItem selected)
    {
        this.selected = selected;

        List<Node> nodes = new ArrayList<>(getVBoxChildren());

        for (HomeTable table : bindedTables)
        {
            nodes.addAll(table.getVBoxChildren());
        }

        for (Node node : nodes)
        {
            node.getStyleClass().remove("home-cell-selected");
        }

        selected.getStyleClass().add("home-cell-selected");

        fireEvent(new Event(SELECT));
    }
    
    public Entity getSelectedSource()
    {
        Entity result = null;
        
        if (selected != null)
        {
            result = selected.getSource();
        }
        
        return result;
    }

    private void updateTitle()
    {
        getVBoxChildren().remove(titleLabel);

        if (title != null && !title.isEmpty())
        {
            titleLabel.setText(title);
        }
    }

    private void initComponents()
    {
        getStyleClass().add("home-table");
        scrollPane.getStyleClass().add("home-table-scroll");
        titleLabel.getStyleClass().add("home-table-title");
        
        titleLabel.prefWidthProperty().bind(widthProperty());
        scrollPane.setContent(vbox);
        getChildren().addAll(titleLabel, scrollPane);
    }

    private VBox vbox = new VBox();
    private ScrollPane scrollPane = new ScrollPane();
    private Label titleLabel = new Label();
}
