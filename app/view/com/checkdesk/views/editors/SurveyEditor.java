/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.PermissionController;
import com.checkdesk.control.util.CategoryUtilities;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.Item;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Category;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.data.User;
import com.checkdesk.model.util.FormWrapper;
import com.checkdesk.model.util.SurveyWrapper;
import com.checkdesk.views.panes.BrowsePane;
import com.checkdesk.views.panes.FormEditorPane;
import com.checkdesk.views.parts.BrowseButton;
import com.checkdesk.views.parts.DatePicker;
import com.checkdesk.views.parts.GroupTable;
import com.checkdesk.views.parts.ItemSelector;
import com.checkdesk.views.pickers.ItemPicker;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.util.Validation;
import java.io.File;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

/**
 *
 * @author MNicaretta
 */
public class SurveyEditor
        extends DefaultEditor<SurveyWrapper>
{

    private static final int PAGE_INFO = 0;
    private static final int PAGE_PARTICIPANTS = 1;
    private static final int PAGE_FORM = 2;

    private int pageIndex = 0;

    public SurveyEditor(EditorCallback<SurveyWrapper> callback)
    {
        super(callback);

        initComponents();

        setTitle("Editor de Pesquisas");
        setHeaderText("Editor de Pesquisas");

        setSource(callback.getSource());
    }

    private void setSource(SurveyWrapper value)
    {
        typeField.setItems(SurveyUtilities.getItems());
        ownerSelector.setItems(UserUtilities.getUsers());
        categorySelector.setItems(CategoryUtilities.getCategories());

        this.source = value;

        Survey survey = source.getSurvey();

        titleField.setText(survey.getTitle());
        createdField.setDate(survey.getCreatedDate());
        typeField.setValue(SurveyUtilities.getType(survey.getType()));
        infoField.setHtmlText(survey.getInfo() != null ? survey.getInfo() : "");
        participantsTable.setGroup(survey.getParticipantsId());
        viewersTable.setGroup(survey.getParticipantsId());
        ownerSelector.setSelected(UserUtilities.getUser(survey.getOwnerId()));
        categorySelector.setSelected(CategoryUtilities.getCategory(survey.getOwnerId()));

        setValidations();
    }

    @Override
    protected void obtainInput()
    {
        Survey survey = source.getSurvey();

        survey.setTitle(titleField.getText());
        survey.setType(typeField.getValue().getValue());
        survey.setInfo(infoField.getHtmlText());
        survey.setParticipantsId(participantsTable.createGroup().getGroupId());
        survey.setViewersId(viewersTable.createGroup().getGroupId());
        survey.setOwnerId(ownerSelector.getSelected().getId());
        survey.setCategoryId(categorySelector.getSelected().getId());

        formPane.obtainInput();
    }

    @Override
    protected boolean validateInput()
    {
        return super.validateInput() && formPane.validateInput();
    }

    @Override
    protected void nextPage()
    {
        pageIndex++;
        updatePage();
    }

    @Override
    protected void previousPage()
    {
        pageIndex--;
        updatePage();
    }

    private void updatePage()
    {
        getDialogPane().getButtonTypes().setAll(btPrevious, btNext, btSave, btCancel);

        switch (pageIndex)
        {
            case PAGE_INFO:
                getDialogPane().setContent(gridPane);
                getButton(btPrevious).setDisable(true);
                removeButton(btSave);
                break;

            case PAGE_PARTICIPANTS:
                vbox.getChildren().setAll(viewersLabel, viewersTable);

                if (typeField.getValue().getValue() == Survey.TYPE_PRIVATE)
                {
                    vbox.getChildren().addAll(0, FXCollections.observableArrayList(participantsLabel, participantsTable));
                }

                getDialogPane().setContent(vbox);
                removeButton(btSave);
                break;

            case PAGE_FORM:
                if (source.getFormWrapper().getForm() == null)
                {
                    getDialogPane().setContent(browsePane);
                }

                else
                {
                    formPane = new FormEditorPane();
                    formPane.setSource(source.getFormWrapper());
                    getDialogPane().setContent(formPane);
                }
                removeButton(btNext);
                break;
        }
    }

    private void showAddForm()
    {
        FormEditorPane pane = (FormEditorPane) addButton.getPane();

        FormWrapper wrapper = new FormWrapper(new Form());
        wrapper.setType(typeField.getSelectionModel().getSelectedItem().getValue());
        
        source.setFormWrapper(wrapper);

        pane.setSource(source.getFormWrapper());
        getDialogPane().setContent(formPane = pane);
    }

    private void showEditForm()
    {
        FormEditorPane pane = (FormEditorPane) editButton.getPane();
        ItemPicker<Form> picker = new ItemPicker<>();
        picker.setItems(FormUtilities.getForms());

        picker.open("Selecione um formulário");

        if (picker.getSelected() != null)
        {
            FormWrapper wrapper = new FormWrapper(picker.getSelected().clone());
            wrapper.setType(typeField.getSelectionModel().getSelectedItem().getValue());
            
            source.setFormWrapper(wrapper);
            pane.setSource(source.getFormWrapper());
            pane.setEnable(PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), editButton.getRole()));

            getDialogPane().setContent(formPane = pane);
        }
    }

    private void showImportForm()
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(FormUtilities.XML_FILTER);
        File file = chooser.showOpenDialog(getDialogPane().getScene().getWindow());

        if (file != null)
        {
            FormWrapper formWrapper = FormUtilities.importForm(file);

            if (formWrapper != null)
            {
                formWrapper.setType(typeField.getSelectionModel().getSelectedItem().getValue());
                source.setFormWrapper(formWrapper);
                
                FormEditorPane pane = (FormEditorPane) editButton.getPane();
                pane.setSource(source.getFormWrapper());
                pane.setEnable(PermissionController.getInstance().hasPermission(ApplicationController.getInstance().getActiveUser(), editButton.getRole()));

                getDialogPane().setContent(formPane = pane);
            }
        }
    }

    private void setValidations()
    {
        addValidation(titleField);
        addValidation(ownerSelector);
        addValidation(createdField, new Validation()
        {
            private String error = "";

            @Override
            public boolean validate()
            {
                boolean result = false;

                if (createdField.getDate() == null)
                {
                    error = "Esse campo deve ser preenchido";
                }

                else
                {
                    result = true;
                }

                return result;
            }

            @Override
            public String getError()
            {
                return error;
            }
        });
        addValidation(categorySelector);
    }

    private void initComponents()
    {
        setWidth(500);
        setHeight(450);

        updatePage();

        //Primeira Página
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int count = 0;

        HBox.setHgrow(typeField, Priority.ALWAYS);
        infoField.setPrefHeight(250);

        createdField.setDisable(true);
        ownerSelector.setDisable(true);

        gridPane.addRow(count++, titleLabel, titleField);
        gridPane.addRow(count++, ownerLabel, ownerSelector);
        gridPane.addRow(count++, createdLabel, createdField);
        gridPane.addRow(count++, categoryLabel, categorySelector);
        gridPane.addRow(count++, typeLabel, typeField);
        gridPane.add(infoLabel, 0, count++, 2, 1);
        gridPane.add(infoField, 0, count++, 2, 1);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        //Segunda Página
        vbox.getChildren().addAll(participantsLabel, participantsTable, viewersLabel, viewersTable);

        getDialogPane().setContent(gridPane);

        //Terceira Página
        addButton.setDisable(!PermissionController.getInstance()
                .hasPermission(ApplicationController.getInstance()
                        .getActiveUser(),
                        addButton.getRole()));

        browsePane.setButtons(new BrowseButton[]
        {
            addButton,
            editButton,
            importButton
        });

        browsePane.addEventHandler(BrowsePane.Events.ON_SELECT, (Event event) ->
        {
            if (browsePane.getSelectedButton() == addButton)
            {
                showAddForm();
            }

            else
            {
                if (browsePane.getSelectedButton() == editButton)
                {
                    showEditForm();
                }

                else
                {
                    if (browsePane.getSelectedButton() == importButton)
                    {
                        showImportForm();
                    }
                }
            }
        });
        
        typeField.valueProperty().addListener((ObservableValue<? extends Item> observableValue, Item oldValue, Item newValue) ->
        {
            if (source.getFormWrapper() != null)
            {
                source.getFormWrapper().setType(typeField.getSelectionModel().getSelectedItem().getValue());
            }
        });
    }

    // Primeira Página
    private GridPane gridPane = new GridPane();

    private Label titleLabel = new Label("Título");
    private TextField titleField = new TextField();

    private Label ownerLabel = new Label("Autor:");
    private ItemSelector<User> ownerSelector = new ItemSelector();

    private Label createdLabel = new Label("Criado em:");
    private DatePicker createdField = new DatePicker();

    private Label categoryLabel = new Label("Categoria:");
    private ItemSelector<Category> categorySelector = new ItemSelector();

    private Label typeLabel = new Label("Tipo:");
    private ComboBox<Item> typeField = new ComboBox();

    private Label infoLabel = new Label("Informações:");
    private HTMLEditor infoField = new HTMLEditor();

    //Segunda Página
    private VBox vbox = new VBox();
    private Label participantsLabel = new Label("Participantes:");
    private GroupTable participantsTable = new GroupTable();
    private Label viewersLabel = new Label("Autorizações:");
    private GroupTable viewersTable = new GroupTable();

    private BrowsePane browsePane = new BrowsePane();
    private BrowseButton addButton = new BrowseButton(new FormEditorPane(), "Nova", "bi_users.png", "add.form");
    private BrowseButton editButton = new BrowseButton(new FormEditorPane(), "Editar", "bi_forms.png", "edit.form");
    private BrowseButton importButton = new BrowseButton(new FormEditorPane(), "Importar", "bi_import.png", "import.form");
    private FormEditorPane formPane;
}
