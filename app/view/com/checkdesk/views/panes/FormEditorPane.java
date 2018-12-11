/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ValidationController;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.Item;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.util.FormWrapper;
import com.checkdesk.model.util.QuestionWrapper;
import com.checkdesk.views.editors.DefaultEditor;
import com.checkdesk.views.parts.GroupTable;
import com.checkdesk.views.parts.ItemSelector;
import com.checkdesk.views.pickers.AttachmentSelector;
import com.checkdesk.views.pickers.OptionPicker;
import com.checkdesk.views.util.Validation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
import javafx.scene.web.HTMLEditor;

/**
 *
 * @author MNicaretta
 */
public class FormEditorPane
        extends DefaultPane
{
    private FormWrapper source;
    private Map<QuestionWrapper, TreeItem<QuestionWrapper>> questionMap = new HashMap<>();
    private Map<TreeItem<QuestionWrapper>, QuestionCell> itemMap = new HashMap<>();
    private boolean onlyOptions = false;
    
    private List<Validation> validations = new ArrayList<>();

    public FormEditorPane()
    {
        initComponents();
    }

    public void setSource(FormWrapper value)
    {
        this.source = value;
        this.onlyOptions = source.getType() == Survey.TYPE_TOTEM;

        Form form = source.getForm();

        nameField.setText(form.getName());
        viewersTable.setGroup(form.getViewersId());
        
        if (form.getInfo() != null && !form.getInfo().isEmpty())
        {
            infoField.setHtmlText(form.getInfo());
        }

        addQuestions(null, source.getQuestions());
    }

    private void addQuestions(QuestionWrapper parent, Map<QuestionWrapper, List<QuestionWrapper>> questions)
    {
        TreeItem<QuestionWrapper> parentItem = questionMap.get(parent);
        
        List<QuestionWrapper> list = questions.get(parent);
        
        if (list != null)
        {
            for (QuestionWrapper wrapper : list)
            {
                TreeItem<QuestionWrapper> item = new TreeItem<>(wrapper);
                questionMap.put(wrapper, item);

                parentItem.getChildren().add(item);

                addQuestions(wrapper, questions);
            }
        }
    }

    public void obtainInput()
    {
        Form form = source.getForm();

        form.setName(nameField.getText());
        form.setViewersId(viewersTable.createGroup().getGroupId());
        form.setInfo(infoField.getHtmlText());

        Map<QuestionWrapper, List<QuestionWrapper>> questions = new HashMap<>();

        populateMap(questionTree.getRoot(), questions);
        
        source.setQuestions(questions);
    }
    
    private void populateMap(TreeItem<QuestionWrapper> item, Map<QuestionWrapper, List<QuestionWrapper>> map)
    {
        List<QuestionWrapper> list = map.get(item.getValue());
        
        if (list == null)
        {
            map.put(item.getValue(), list = new ArrayList<>());
        }
        
        for (TreeItem<QuestionWrapper> child : item.getChildren())
        {
            list.add(itemMap.get(child).getSource());
            populateMap(child, map);
        }
    }

    public List<Validation> getValidations()
    {
        return validations;
    }

    private void addItem()
    {
        Question question = new Question();
        question.setFormId(source.getForm().getId());
        
        questionTree.getRoot().getChildren().add(new TreeItem<>(new QuestionWrapper(question)));
    }

    @Override
    protected void resize()
    {
        tabPane.setPrefSize(getWidth(), getHeight());
        questionTree.setPrefWidth(getWidth());
    }

    @Override
    public void refreshContent() {}

    public void setEnable(boolean enable)
    {
        addPane.setDisable(!enable);

        if (!enable)
        {
            addPane.setStyle("-fx-border-color: #CBC7C7");
        }

        removeItem.setDisable(!enable);

        for (QuestionCell cell : itemMap.values())
        {
            cell.setEnable(enable);
        }
    }

    private void initComponents()
    {
        validations.add(ValidationController.addValidation(nameField));
        
        //GeneralTab
        int count = 0;

        VBox.setVgrow(viewersTable, Priority.ALWAYS);
        VBox.setVgrow(infoField, Priority.ALWAYS);

        GridPane.setValignment(viewersLabel, VPos.TOP);
        GridPane.setValignment(infoLabel, VPos.TOP);

        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.addRow(count++, nameLabel, nameField);
        gridPane.addRow(count++, viewersLabel, viewersTable);
        gridPane.addRow(count++, infoLabel, infoField);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        generalTab.setContent(gridPane);

        //ListTab
        TreeItem<QuestionWrapper> root = new TreeItem<>();
        questionMap.put(null, root);
        questionTree.setRoot(root);
        questionTree.setShowRoot(false);
        questionTree.setContextMenu(contextMenu);
        
        HBox.setHgrow(vbox, Priority.ALWAYS);
        HBox.setHgrow(addPane, Priority.ALWAYS);

        addPane.getStyleClass().add("add-pane");
        addPane.getChildren().add(new Label("Adicionar"));

        vbox.getChildren().addAll(questionTree, addPane);

        listTab.setContent(new ScrollPane(vbox));

        generalTab.setClosable(false);
        listTab.setClosable(false);
        tabPane.getTabs().addAll(generalTab, listTab);

        getChildren().add(tabPane);

        questionTree.setCellFactory((TreeView<QuestionWrapper> tree) ->
        {
            return new TreeCell<QuestionWrapper>()
            {
                @Override
                protected void updateItem(QuestionWrapper item, boolean empty)
                {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty)
                    {
                        setGraphic(null);
                    }
                    
                    else
                    {
                        QuestionCell cell = itemMap.get(getTreeItem());
                        
                        if (cell == null)
                        {
                            itemMap.put(getTreeItem(), cell = new QuestionCell(this));
                        }
                        
                        cell.updateCell(this);
                        
                        setGraphic(cell);
                    }
                }
            };
        });
        
        addPane.setOnMouseClicked((MouseEvent event) ->
        {
            if (event.getButton() == MouseButton.PRIMARY)
            {
                addItem();
            }
        });
    }

    private TabPane tabPane = new TabPane();
    private Tab generalTab = new Tab("Geral");
    private Tab listTab = new Tab("Perguntas");

    //GeneralTab
    private GridPane gridPane = new GridPane();
    private Label nameLabel = new Label("Nome:");
    private TextField nameField = new TextField();

    private Label viewersLabel = new Label("Autorizações:");
    private GroupTable viewersTable = new GroupTable();

    private Label infoLabel = new Label("Informações:");
    private HTMLEditor infoField = new HTMLEditor();

    //ListTab
    private VBox vbox = new VBox();
    private TreeView<QuestionWrapper> questionTree = new TreeView<>();
    private HBox addPane = new HBox();

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem removeItem = new MenuItem("Excluir");
    {
        removeItem.setOnAction((ActionEvent event) ->
        {
            TreeItem selected = questionTree.getSelectionModel().getSelectedItem();
            TreeItem parent = selected.getParent();
            
            int index = parent.getChildren().indexOf(selected);
            
            parent.getChildren().addAll(index, selected.getChildren());
            parent.getChildren().remove(selected);
            
            itemMap.remove(selected);
        });

        contextMenu.getItems().add(removeItem);
    }

    private class QuestionCell
            extends HBox
    {
        private static final int DRAG_TOP    = 1;
        private static final int DRAG_CENTER = 2;
        private static final int DRAG_BOTTOM = 3;
        
        private TreeCell<QuestionWrapper> cell;
        
        public QuestionCell(TreeCell<QuestionWrapper> cell)
        {
            setId(UUID.randomUUID().toString());

            initDragEvent();
            initComponents();
            
            setSource(cell);
        }

        private void setSource(TreeCell<QuestionWrapper> cell)
        {
            this.cell = cell;
            
            if (cell.getTreeItem() != null)
            {
                Question question = cell.getTreeItem().getValue().getQuestion();

                nameField.setText(question.getName());
                typeField.setValue(FormUtilities.getQuestionType(question.getType()));
                optionSelector.setSelected(FormUtilities.getOption(question.getOptionId()));

                attachmentSelector.setQuestion(question);
            }
        }
        
        public void updateCell(TreeCell<QuestionWrapper> cell)
        {
            this.cell = cell;
            
            cell.setBorder(borderNone);
            cell.getTreeItem().setExpanded(true);
        }
        
        public QuestionWrapper getSource()
        {
            Question question = cell.getTreeItem().getValue().getQuestion();

            question.setName(nameField.getText());
            question.setType(typeField.getValue().getValue());
            question.setOptionId(optionSelector.getSelected() != null ? optionSelector.getSelected().getId() : null);
            question.setConstraints("");

            cell.getTreeItem().getValue().setAttachments(attachmentSelector.getSelected());

            return cell.getTreeItem().getValue();
        }

        private void resize()
        {
            double width = getWidth() / getChildren().size();
            nameField.setPrefWidth(width - 15);
            typeField.setPrefWidth(width - 15);
            optionSelector.setPrefWidth(width - 15);
            attachmentSelector.setPrefWidth(width - 15);
        }

        public void setEnable(boolean enable)
        {
            setDisable(!enable);
            nameField.setDisable(!enable);
            typeField.setDisable(!enable);
            optionSelector.setDisable(!enable);
            attachmentSelector.setDisable(!enable);
            
            if (enable)
            {
                initDragEvent();
            }
            
            else
            {
                cancelDragEvent();
            }
        }

        private int dragOption(double eventY)
        {
            int result;
            
            double height = this.getHeight();

            double top = height * (1.0 / 3.0);
            double bottom = height * (2.0 / 3.0);

            if (typeField.getValue().getValue() != Question.TYPE_CATEGORY)
            {
                top = height * (1.0 / 2.0);
                bottom = height * (1.0 / 2.0);
            }

            if (eventY <= top)
            {
                result = DRAG_TOP;
            }

            else if (eventY >= bottom)
            {
                result = DRAG_BOTTOM;
            }

            else
            {
                result = DRAG_CENTER;
            }
            
            return result;
        }
        
        private void initDragEvent()
        {
            final QuestionCell thisCell = this;

            setAlignment(Pos.CENTER);

            setOnDragDetected(event ->
            {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(thisCell.getId());
                dragboard.setContent(content);

                event.consume();
            });

            setOnDragOver(event ->
            {
                if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString())
                {
                    QuestionCell dragged = (QuestionCell) questionTree.lookup("#" + event.getDragboard().getString());
                    
                    if (!this.isChild(dragged))
                    {
                        event.acceptTransferModes(TransferMode.MOVE);

                        switch (dragOption(event.getY()))
                        {
                            case DRAG_TOP:
                                cell.setBorder(borderTop);
                                cell.setOpacity(0.6);
                                break;

                            case DRAG_CENTER:
                                cell.setBorder(borderAll);
                                cell.setOpacity(0.3);
                                break;

                            case DRAG_BOTTOM:
                                cell.setBorder(borderBottom);
                                cell.setOpacity(0.6);
                                break;
                        }
                    }
                }

                event.consume();
            });

            setOnDragExited(event ->
            {
                if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString())
                {
                    cell.setOpacity(1);
                    cell.setBorder(borderNone);
                }
            });

            setOnDragDropped(event ->
            {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;

                if (dragboard.hasString())
                {
                    TreeItem dragged = ((QuestionCell) questionTree.lookup("#" + dragboard.getString())).cell.getTreeItem();
                    dragged.getParent().getChildren().remove(dragged);

                    TreeItem dropped = cell.getTreeItem();

                    int index = dropped.getParent().getChildren().indexOf(dropped);
                    
                    switch (dragOption(event.getY()))
                    {
                        case DRAG_TOP:
                            dropped.getParent().getChildren().add(index, dragged);
                            questionTree.getSelectionModel().select(dragged);
                            break;
                            
                        case DRAG_CENTER:
                            dropped.getChildren().add(dragged);
                            dropped.setExpanded(true);
                            break;
                            
                        case DRAG_BOTTOM:
                            dropped.getParent().getChildren().add(index + 1, dragged);
                            questionTree.getSelectionModel().select(dragged);
                            break;
                    }

                    success = true;
                }

                event.setDropCompleted(success);

                event.consume();
            });

            setOnDragDone(DragEvent::consume);
        }
        
        private void cancelDragEvent()
        {
            setOnDragDetected(null);
            setOnDragOver(null);
            setOnDragExited(null);
            setOnDragDropped(null);
            setOnDragDone(null);
        }
        
        private boolean isChild(QuestionCell parent)
        {
            boolean result = false;
            
            TreeItem item = cell.getTreeItem();
            
            while (item != questionTree.getRoot())
            {
                if (item == parent.cell.getTreeItem())
                {
                    result = true;
                    break;
                }
                
                item = item.getParent();
            }
            
            return result;
        }

        private void initComponents()
        {
            validations.add(ValidationController.addValidation(nameField));
            validations.add(new Validation(optionSelector, optionSelector.focusedProperty(), Validation.getCallback(optionSelector))
            {
                @Override
                protected String validate()
                {
                    String error = "";

                    if (typeField.getValue() != null)
                    {
                        if (typeField.getValue().getValue() == Question.TYPE_SINGLE_CHOICE ||
                            typeField.getValue().getValue() == Question.TYPE_MULTI_CHOICE)
                        {
                            if (optionSelector.getText() == null || optionSelector.getText().isEmpty())
                            {
                                error = "Esse campo deve ser preenchido";
                            }
                        }
                    }

                    return error;
                }
            });
            
            setSpacing(10);
            setPadding(new Insets(8, 0, 8, 0));

            VBox.setVgrow(this, Priority.ALWAYS);
            HBox.setHgrow(this, Priority.ALWAYS);

            typeField.setItems(FXCollections.observableArrayList(FormUtilities.getQuestionTypes(onlyOptions)));
            optionSelector.changePicker(new OptionPicker());
            optionSelector.setItems(FormUtilities.getOptions());

            typeField.valueProperty().addListener((ObservableValue<? extends Item> value, Item oldValue, Item newValue) ->
            {
                optionSelector.setSelected(null);
                getChildren().setAll(nameField, typeField, optionSelector, attachmentSelector);

                if (typeField.getValue().getValue() != Question.TYPE_SINGLE_CHOICE &&
                    typeField.getValue().getValue() != Question.TYPE_MULTI_CHOICE)
                {
                    getChildren().remove(optionSelector);
                }

                resize();
            });

            widthProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) ->
            {
                resize();
            });
        }

        private TextField nameField = new TextField();
        private ComboBox<Item> typeField = new ComboBox<>();
        private ItemSelector<Option> optionSelector = new ItemSelector<>();
        private AttachmentSelector attachmentSelector = new AttachmentSelector();

        private Validation optionValidation = ValidationController.addValidation(optionSelector);

        private Border borderTop = new Border(new BorderStroke(Paint.valueOf("#0066CC"), null, null, null,
                                                               BorderStrokeStyle.DASHED, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                                                               CornerRadii.EMPTY,
                                                               new BorderWidths(2),
                                                               Insets.EMPTY));

        private Border borderBottom = new Border(new BorderStroke(null, null, Paint.valueOf("#0066CC"), null,
                                                                  BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.DASHED, BorderStrokeStyle.NONE,
                                                                  CornerRadii.EMPTY,
                                                                  new BorderWidths(2),
                                                                  Insets.EMPTY));

        private Border borderAll = new Border(new BorderStroke(Paint.valueOf("#0066CC"),
                                                               BorderStrokeStyle.DASHED,
                                                               CornerRadii.EMPTY,
                                                               new BorderWidths(2)));

        private Border borderNone = new Border(new BorderStroke(null,
                                                                BorderStrokeStyle.NONE,
                                                                CornerRadii.EMPTY,
                                                                new BorderWidths(2)));
    }
}
