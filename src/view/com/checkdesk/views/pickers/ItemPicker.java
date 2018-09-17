/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.pickers;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.parts.DefaultTable;
import com.checkdesk.views.util.EditorButton;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author MNicaretta
 */
public class ItemPicker<T>
        extends Dialog<T>
{
    private List<T> items;
    protected ButtonType btAccept = new ButtonType("Selecionar", ButtonBar.ButtonData.OK_DONE);
    protected ButtonType btCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    protected ButtonType selectedButton;

    private boolean selectOnClick = true;

    public ItemPicker()
    {
        initComponents();

        setTitle("Seletor");
    }

    public void setItems(List<T> items)
    {
        this.items = items;

        list.setItems(FXCollections.observableList(items));
    }

    public List<T> getItems()
    {
        return items;
    }

    public boolean isSelectOnClick()
    {
        return selectOnClick;
    }

    public void setSelectOnClick(boolean selectOnClick)
    {
        this.selectOnClick = selectOnClick;
    }

    public T getSelected()
    {
        return list.getSelectedItem();
    }

    protected boolean validate()
    {
        return getSelected() != null;
    }

    public void open(String text)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                searchField.requestFocus();
                list.clearSelection();
            }

        });

        setHeaderText(text);
        showAndWait();
    }

    private void initComponents()
    {
        setDialogPane(new Pane());
        getDialogPane().getStylesheets().add(ResourceLocator.getInstance().getStyleResource("default.css"));
        setResizable(false);

        list.setShowAddPane(false);

        searchField.setPromptText("Digite para filtrar");
        searchField.setPrefWidth(425);
        list.setPrefSize(500, 300);

        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(15);
        hbox.getChildren().addAll(searchLabel, searchField);
        vbox.getChildren().addAll(hbox, list);

        getDialogPane().setPrefSize(500, 300);

        getDialogPane().getButtonTypes().addAll(btAccept, btCancel);
        getDialogPane().setContent(vbox);

        setOnCloseRequest((DialogEvent t) ->
        {
            if (selectedButton != btCancel)
            {
                if (!validate())
                {
                    t.consume();
                }
            }

            else
            {
                list.clearSelection();
            }
        });

        setResultConverter(new Callback()
        {
            @Override
            public Object call(Object p)
            {
                return selectedButton = (ButtonType) p;
            }

        });

        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            ObservableList<T> filterList = FXCollections.observableArrayList();

            for (T item : items)
            {
                String filter = searchField.getText().toLowerCase().trim();

                if (item.toString().toLowerCase().contains(filter))
                {
                    filterList.add(item);
                }
            }

            list.setItems(filterList);
        });

        searchField.setOnKeyPressed((KeyEvent event) ->
        {
            if (event.getCode() == KeyCode.DOWN)
            {
                list.requestFocus();
                list.setSelectedIndex(0);
            }
        });

        list.setOnKeyPressed((KeyEvent event) ->
        {
            if (event.getCode().equals(KeyCode.ENTER))
            {
                close();
            }
        });
    }

    private VBox vbox = new VBox();
    private HBox hbox = new HBox();

    protected Label searchLabel = new Label("Filtrar: ");
    protected TextField searchField = new TextField();
    protected DefaultTable<T> list = new DefaultTable<>();
    
    private class Pane
            extends DialogPane
    {
        @Override
        protected Node createButton(ButtonType bt)
        {
            final Button button = (Button) super.createButton(bt);
            EditorButton editorButton = new EditorButton(bt);
            editorButton.setOnMouseClicked((MouseEvent t) ->
            {
                button.fire();
            });

            return editorButton;
        }
    }
}
