/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author MNicaretta
 */
public class DefaultTable<T>
        extends BorderPane
{
    public static class Events
    {
        public static final EventType ON_ADD = new EventType("onAddItem");
        public static final EventType ON_SELECT = new EventType("onSelectTableItem");
    }

    private DateFormat df = DateFormat.getDateInstance();
    private javafx.scene.control.MenuItem[] actions = new javafx.scene.control.MenuItem[0];

    public DefaultTable()
    {
        initComponents();
    }

    private Callback<ListView<T>, ListCell<T>> cellFactory()
    {
        return (ListView<T> p) -> new ListCell<T>()
        {
            @Override
            protected void updateItem(T item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item == null || empty)
                {
                    setText(null);
                    setTextFill(null);
                    setGraphic(null);
                }

                else
                {
                    getStyleClass().add("table-item");

                    HBox hbox = new HBox();

                    String text = item.toString();
                    
                    if (item instanceof Date)
                    {
                        text = df.format((Date) item);
                    }
                    
                    Label label = new Label(text);
                    label.getStyleClass().add("table-label");

                    hbox.getChildren().add(label);
                    
                    if (actions.length != 0)
                    {
                        ImageView image = new ImageView(new Image(ResourceLocator.getInstance().getImageResource("context.png")));
                        image.setFitHeight(24);
                        image.setFitWidth(24);
                        Pane pane = new Pane();
                        pane.setPrefSize(24, 24);
                        pane.getChildren().add(image);
                        pane.setCursor(Cursor.HAND);
                        pane.setOnMouseClicked((MouseEvent t) ->
                        {
                            new ContextMenu(actions).show(pane, Side.BOTTOM, 0, 0);
                        });

                        hbox.getChildren().add(pane);
                    }

                    hbox.setSpacing(3);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);

                    hbox.widthProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
                    {
                        label.setPrefWidth(hbox.getWidth() - 30);
                    });
                }
            }

        };
    }

    public void setItems(List<T> items)
    {
        list.setItems(FXCollections.observableArrayList(items));
    }
    
     public List<T> getItems()
    {
        return list.getItems();
    }

    public T getSelectedItem()
    {
        return list.getSelectionModel().getSelectedItem();
    }

    public void setSelectedItem(T item)
    {
        list.getSelectionModel().select(item);
    }

    public int getSelectedIndex()
    {
        return list.getSelectionModel().getSelectedIndex();
    }

    public void setSelectedIndex(int index)
    {
        list.getSelectionModel().select(index);
    }
    
    public void clearSelection()
    {
        list.getSelectionModel().clearSelection();
    }

    public void setActions(javafx.scene.control.MenuItem[] items)
    {
        this.actions = items;
    }
    
    public void setShowAddPane(boolean show)
    {
        setTop(show ? pane : null);
    }

    private void initComponents()
    {
        pane.getStyleClass().add("add-pane");
        pane.getChildren().add(addItem);

        list.setCellFactory(cellFactory());
        list.setStyle("-fx-focus-color: transparent;-fx-faint-focus-color: transparent;");

        setTop(pane);
        setCenter(list);

        pane.setOnMouseClicked((MouseEvent t) ->
        {
            if (t.getButton() == MouseButton.PRIMARY)
            {
                fireEvent(new Event(Events.ON_ADD));
            }
        });

        list.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends T> ov, T t, T t1) ->
        {
            fireEvent(new Event(Events.ON_SELECT));
        });
    }

    private HBox pane = new HBox();
    private Label addItem = new Label("Adicionar");
    private ListView<T> list = new ListView<>();
}
