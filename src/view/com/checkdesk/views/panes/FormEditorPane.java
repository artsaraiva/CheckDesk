/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.Item;
import com.checkdesk.model.data.Attachment;
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
import java.util.List;
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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
import javafx.scene.web.HTMLEditor;

/**
 *
 * @author MNicaretta
 */
public class FormEditorPane
        extends DefaultPane
{
    private FormWrapper source;
    private ObservableList<QuestionCell> questionCells = FXCollections.observableArrayList();
    private QuestionCell selectedCell = null;
    private boolean onlyOptions = false;

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
        infoField.setHtmlText(form.getInfo());
        
        for (QuestionWrapper questionWrapper : source.getQuestions())
        {
            addItem(questionWrapper);
        }
    }

    public void obtainInput()
    {
        Form form = source.getForm();
        
        form.setName(nameField.getText());
        form.setViewersId(viewersTable.createGroup().getGroupId());
        form.setInfo(infoField.getHtmlText());
        
        List<QuestionWrapper> questions = new ArrayList<>();
        
        for (QuestionCell cell : questionCells)
        {
            questions.add(cell.getSource());
        }
        
        source.setQuestions(questions);
    }
    
    public boolean validateInput()
    {
        boolean result = true;
        
        for (QuestionCell cell : questionCells)
        {
            result &= cell.validate();
        }
        
        return result;
    }

    private void setSelected(QuestionCell selected)
    {
        this.selectedCell = selected;

        for (QuestionCell cell : questionCells)
        {
            cell.setBackground(cell == selected ? background : null);
        }
    }

    private void addItem(QuestionWrapper questionWrapper)
    {
        QuestionCell questionCell = new QuestionCell(questionWrapper);
        HBox.setHgrow(questionCell, Priority.ALWAYS);

        listbox.getChildren().add(questionCells.size(), questionCell);
        questionCells.add(questionCell);
    }

    @Override
    protected void resize()
    {
        tabPane.setPrefSize(getWidth(), getHeight());
        listbox.setPrefWidth(getWidth());
    }

    @Override
    public void refreshContent(){}
    
    public void setEnable(boolean enable)
    {
        addPane.setDisable(!enable);
        
        if(!enable)
        {
            addPane.setStyle("-fx-border-color: #CBC7C7");
        }
        
        removeItem.setDisable(!enable);
        
        for (QuestionCell cell : questionCells)
        {
            cell.setEnable(enable);
        }
    }

    private void initComponents()
    {
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
        listbox.setSpacing(5);

        HBox.setHgrow(listbox, Priority.ALWAYS);
        HBox.setHgrow(addPane, Priority.ALWAYS);
        
        addPane.getStyleClass().add("add-pane");
        addPane.getChildren().add(new Label("Adicionar"));
        
        listbox.getChildren().add(addPane);
        
        listTab.setContent(new ScrollPane(listbox));
        
        generalTab.setClosable(false);
        listTab.setClosable(false);
        tabPane.getTabs().addAll(generalTab, listTab);
        
        getChildren().add(tabPane);

        listbox.setOnContextMenuRequested((ContextMenuEvent event) ->
        {
            contextMenu.show(selectedCell, event.getScreenX(), event.getScreenY());
        });

        addPane.setOnMouseClicked((MouseEvent event) ->
        {
            if (event.getButton() == MouseButton.PRIMARY)
            {
                Question question = new Question();
                question.setFormId(source.getForm().getId());
                
                addItem(new QuestionWrapper(question));
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
                questionCells.remove(selectedCell);
                listbox.getChildren().remove(selectedCell);

                selectedCell = null;
            }
        });
        
        contextMenu.getItems().add(removeItem);
    }

    private class QuestionCell
            extends HBox
    {
        private QuestionWrapper source;

        public QuestionCell(QuestionWrapper question)
        {
            setId(UUID.randomUUID().toString());

            initDragEvent();
            initComponents();
            
            setSource(question);
        }

        private void setSource(QuestionWrapper source)
        {
            this.source = source;
            
            Question question = source.getQuestion();
            
            nameField.setText(question.getName());
            
            Item selected = FormUtilities.getQuestionType(question.getType());
            
            if (!typeField.getItems().contains(selected))
            {
                selected = typeField.getItems().get(0);
            }
            
            typeField.setValue(selected);
            optionSelector.setSelected(FormUtilities.getOption(question.getOptionId()));
            
            attachmentSelector.setQuestion(question);
            
            validate();
        }
        
        public QuestionWrapper getSource()
        {
            Question question = source.getQuestion();
            
            question.setName(nameField.getText());
            question.setType(typeField.getValue().getValue());
            question.setOptionId(optionSelector.getSelected() != null ? optionSelector.getSelected().getId() : null);
            question.setConstraints("");
            
            source.setAttachments(attachmentSelector.getSelected());

            return source;
        }
        
        private void resize()
        {
            double width = FormEditorPane.this.getWidth() / getChildren().size();
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
        }
        
        public boolean validate()
        {
            boolean result = false;

            Validation validation = DefaultEditor.getTextValidation(nameField);

            if (result = validation.validate())
            {
                nameField.setBorder( null );
                nameField.setTooltip( null );
                nameField.setStyle( "-fx-faint-focus-color: transparent;" );
            }

            else
            {
                nameField.setTooltip( new Tooltip( validation.getError() ) );
                nameField.setBorder( new Border( new BorderStroke( Paint.valueOf( "#FF0000" ),
                                                                        BorderStrokeStyle.SOLID,
                                                                        new CornerRadii( 5 ),
                                                                        BorderWidths.DEFAULT,
                                                                        new Insets( -1 ) ) ) );

                nameField.setStyle( "-fx-focus-color: transparent;-fx-faint-focus-color: transparent;" );
            }
            
            if (typeField.getValue().getValue() == Question.TYPE_SINGLE_CHOICE ||
                typeField.getValue().getValue() == Question.TYPE_MULTI_CHOICE)
            {
                validation = DefaultEditor.getTextValidation(optionSelector);
                
                boolean validate = validation.validate();
                result &= validate;
                
                if (validate)
                {
                    optionSelector.setBorder( null );
                    optionSelector.setTooltip( null );
                    optionSelector.setStyle( "-fx-faint-focus-color: transparent;" );
                }

                else
                {
                    optionSelector.setTooltip( new Tooltip( validation.getError() ) );
                    optionSelector.setBorder( new Border( new BorderStroke( Paint.valueOf( "#FF0000" ),
                                                                            BorderStrokeStyle.SOLID,
                                                                            new CornerRadii( 5 ),
                                                                            BorderWidths.DEFAULT,
                                                                            new Insets( -1 ) ) ) );

                    optionSelector.setStyle( "-fx-focus-color: transparent;-fx-faint-focus-color: transparent;" );
                }
            }

            return result;
        }

        private void initDragEvent()
        {
            final HBox thisCell = this;

            setAlignment(Pos.CENTER);

            setOnDragDetected(event ->
            {
                setSelected(this);
                
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
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            setOnDragEntered(event ->
            {
                if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString())
                {
                    setOpacity(0.3);
                    setBorder(dragBorder);
                    setPadding(new Insets(3));
                }
            });

            setOnDragExited(event ->
            {
                if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString())
                {
                    setOpacity(1);
                    setBorder(null);
                    setPadding(new Insets(5));
                }
            });

            setOnDragDropped(event ->
            {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString())
                {
                    QuestionCell dbCell = (QuestionCell) listbox.lookup("#" + db.getString());

                    ObservableList<Node> items = FXCollections.observableArrayList(listbox.getChildren());

                    int draggedIdx = items.indexOf(dbCell);
                    int thisIdx = items.indexOf(thisCell);

                    items.set(draggedIdx, thisCell);
                    items.set(thisIdx, dbCell);

                    listbox.getChildren().setAll(items);

                    success = true;
                }

                event.setDropCompleted(success);

                event.consume();
            });

            setOnDragDone(DragEvent::consume);
        }

        private void initComponents()
        {
            setSpacing(10);
            setPadding(new Insets(5));
            
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
        private ComboBox<Item> typeField = new ComboBox<>();
        private ItemSelector<Option> optionSelector = new ItemSelector<>();
        private AttachmentSelector attachmentSelector = new AttachmentSelector();
        
        private Border dragBorder = new Border(new BorderStroke(Paint.valueOf("#0066CC"),
                                                                BorderStrokeStyle.DASHED,
                                                                CornerRadii.EMPTY,
                                                                new BorderWidths(2)));
    }
}
