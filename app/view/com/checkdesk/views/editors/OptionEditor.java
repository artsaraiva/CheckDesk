/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.ValidationController;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.Item;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.OptionItem;
import com.checkdesk.model.util.OptionWrapper;
import com.checkdesk.views.parts.GroupTable;
import com.checkdesk.views.util.Callback;
import com.checkdesk.views.util.Validation;
import java.util.UUID;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author MNicaretta
 */
public class OptionEditor
        extends DefaultEditor<OptionWrapper>
{
    private OptionWrapper source = null;
    private ObservableList<ItemCell> itemCells = FXCollections.observableArrayList();
    private ItemCell selectedCell = null;

    public OptionEditor(Callback<OptionWrapper> callback)
    {
        super(callback);

        initComponents();

        setTitle("Editor de Opções");
        setHeaderText("Editor de Opções");

        setSource(callback.getSource());
    }

    private void setSource(OptionWrapper value)
    {
        this.source = value;

        Option option = source.getOption();

        nameField.setText(option.getName());
        typeField.getSelectionModel().select(0);
        viewersTable.setGroup(option.getViewersId());

        for (OptionItem item : source.getItems())
        {
            addItem(item);
        }

        resize();
    }

    @Override
    public void obtainInput()
    {
        Option option = source.getOption();

        option.setName(nameField.getText());
        option.setType(typeField.getSelectionModel().getSelectedItem().getValue());
        option.setViewersId(viewersTable.createGroup().getGroupId());

        source.getItems().clear();

        for (ItemCell cell : itemCells)
        {
            source.getItems().add(cell.getSource());
        }
    }

    @Override
    public void resize()
    {
        if (tabPane != null && gridPane != null)
        {
            tabPane.setPrefSize(getWidth(), getHeight());
            double max = 0;

            for (Node node : gridPane.getChildren())
            {
                if (node instanceof Label)
                {
                    max = Math.max(max, ((Label) node).getWidth());
                }
            }

            minWidthLabel.set(max);
        }
    }

    private void setSelected(ItemCell selected)
    {
        this.selectedCell = selected;

        for (ItemCell cell : itemCells)
        {
            cell.setBackground(cell == selected ? background : null);
        }
    }

    private void addItem(OptionItem item)
    {
        ItemCell itemCell = new ItemCell(item);
        HBox.setHgrow(itemCell, Priority.ALWAYS);

        listbox.getChildren().add(itemCells.size(), itemCell);
        itemCells.add(itemCell);
    }

    public void setEnable(boolean enable)
    {
        addPane.setDisable(!enable);

        if (!enable)
        {
            addPane.setStyle("-fx-border-color: #CBC7C7");
        }

        removeItem.setDisable(!enable);

        for (ItemCell cell : itemCells)
        {
            cell.setEnable(enable);
        }
    }

    private void initComponents()
    {
        addValidation(nameField);

        setWidth(650);
        setHeight(400);

        //GeneralTab
        int count = 0;

        typeField.setItems(FormUtilities.getOptionTypes());
        VBox.setVgrow(viewersTable, Priority.ALWAYS);
        GridPane.setValignment(viewersLabel, VPos.TOP);

        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.addRow(count++, nameLabel, nameField);
        gridPane.addRow(count++, typeLabel, typeField);
        gridPane.addRow(count++, viewersLabel, viewersTable);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.minWidthProperty().bind(minWidthLabel);
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        generalTab.setContent(gridPane);

        //ListTab
        listbox.setSpacing(5);

        ScrollPane pane = new ScrollPane(listbox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        HBox.setHgrow(listbox, Priority.ALWAYS);
        HBox.setHgrow(addPane, Priority.ALWAYS);

        addPane.getStyleClass().add("add-pane");
        addPane.getChildren().add(new Label("Adicionar"));

        listbox.getChildren().add(addPane);

        listTab.setContent(pane);

        generalTab.setClosable(false);
        listTab.setClosable(false);

        tabPane.getTabs().addAll(generalTab, listTab);
        getDialogPane().setContent(tabPane);

        listbox.setOnContextMenuRequested((ContextMenuEvent event) ->
        {
            contextMenu.show(selectedCell, event.getScreenX(), event.getScreenY());
        });

        addPane.setOnMouseClicked((MouseEvent event) ->
        {
            if (event.getButton() == MouseButton.PRIMARY)
            {
                OptionItem item = new OptionItem();
                item.setOptionId(source.getOption().getId());

                addItem(item);
            }
        });
    }

    private TabPane tabPane = new TabPane();
    private Tab generalTab = new Tab("Geral");
    private Tab listTab = new Tab("Itens");

    //GeneralTab
    private GridPane gridPane = new GridPane();

    private Label nameLabel = new Label("Nome");
    private TextField nameField = new TextField();

    private Label typeLabel = new Label("Tipo");
    private ComboBox<Item> typeField = new ComboBox<>();

    private Label viewersLabel = new Label("Autorizações:");
    private GroupTable viewersTable = new GroupTable();

    //ListTab
    private VBox listbox = new VBox();
    private HBox addPane = new HBox();

    private Background background = new Background(new BackgroundFill(Paint.valueOf("#B0B0B0"),
                                                                      CornerRadii.EMPTY,
                                                                      Insets.EMPTY));

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem removeItem = new MenuItem("Excluir");

    
    {
        removeItem.setOnAction((ActionEvent event) ->
        {
            if (selectedCell != null)
            {
                itemCells.remove(selectedCell);
                listbox.getChildren().remove(selectedCell);

                selectedCell = null;
            }
        });

        contextMenu.getItems().add(removeItem);
    }

    private class ItemCell
            extends HBox
    {
        private OptionItem source;

        public ItemCell(OptionItem source)
        {
            setId(UUID.randomUUID().toString());

            initComponents();

            setSource(source);
        }

        private void setSource(OptionItem value)
        {
            this.source = value;

            nameField.setText(source.getName());
            valueField.setText(source.getValue());

            resize();
        }

        public OptionItem getSource()
        {
            source.setName(nameField.getText());
            source.setValue(valueField.getText());

            return source;
        }

        private void resize()
        {
            double width = tabPane.getWidth() / getChildren().size();
            nameField.setPrefWidth(width - 15);
            valueField.setPrefWidth(width - 15);
        }

        public void setEnable(boolean enable)
        {
            setDisable(!enable);
            nameField.setDisable(!enable);
            valueField.setDisable(!enable);
        }

        private void initComponents()
        {
            addValidation(nameField);
            addValidation(valueField);

            setSpacing(10);
            setPadding(new Insets(5));

            nameField.setPromptText("Nome");
            valueField.setPromptText("Valor");

            getChildren().addAll(nameField, valueField);

            setOnMouseClicked((MouseEvent event) ->
            {
                setSelected(this);
            });

            widthProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
            {
                resize();
            });
        }

        private TextField nameField = new TextField();
        private TextField valueField = new TextField();
    }
}
