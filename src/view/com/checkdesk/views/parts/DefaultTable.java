/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ResourceLocator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
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

                    ImageView image = new ImageView(new Image(ResourceLocator.getInstance().getImageResource("context.png")));
                    image.setCursor(Cursor.HAND);
                    image.setFitHeight(24);
                    image.setFitWidth(24);
                    image.setOnMouseClicked((MouseEvent t) ->
                    {
                        new ContextMenu(actions).show(this, Side.RIGHT, 0, 0);
                    });

                    Label label = new Label(item.toString());
                    hbox.getChildren().addAll(label, image);

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

    public T getSelectedItem()
    {
        return list.getSelectionModel().getSelectedItem();
    }

    public void setActions(javafx.scene.control.MenuItem[] items)
    {
        this.actions = items;
    }

    private void initComponents()
    {
        pane.setBorder(new Border(new BorderStroke(Paint.valueOf("#BDBDBD"),
                                                   BorderStrokeStyle.DASHED,
                                                   CornerRadii.EMPTY,
                                                   new BorderWidths(5))));

        pane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"),
                                                             CornerRadii.EMPTY,
                                                             Insets.EMPTY)));

        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10, 30, 10, 30));
        pane.setCursor(Cursor.HAND);

        addItem.setTextFill(Paint.valueOf("#BDBDBD"));
        addItem.setStyle("-fx-font-size: 14;-fx-font-weight: bold;");

        pane.getChildren().add(addItem);

        list.setCellFactory(cellFactory());
        list.setStyle("-fx-focus-color: transparent;-fx-faint-focus-color: transparent;");

        setTop(pane);
        setCenter(list);

        pane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                if (t.getButton() == MouseButton.PRIMARY)
                {
                    fireEvent(new Event(Events.ON_ADD));
                }
            }

        });

        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>()
        {
            @Override
            public void changed(ObservableValue<? extends T> ov, T t, T t1)
            {
                fireEvent(new Event(Events.ON_SELECT));
            }

        });
    }

    private HBox pane = new HBox();
    private Label addItem = new Label("Adicionar");
    private ListView<T> list = new ListView<>();
}
