/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.model.data.Attachment;
import com.checkdesk.views.util.EditorCallback;
import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 *
 * @author MNicaretta
 */
public class AttachmentEditor
        extends DefaultEditor<Attachment>
{
    private File file;
    
    public AttachmentEditor(EditorCallback<Attachment> callback)
    {
        super(callback);
        
        initComponents();
        
        setTitle("Editor de Anexo");
        setHeaderText("Editor de Anexo");
        
        setSource(callback.getSource());
    }
    
    private void setSource(Attachment attachment)
    {
        nameField.setText(attachment.getName());
        file = attachment.getAttachment();
        
        if (file == null)
        {
            chooseFile();
        }
        
        setValidations();
    }

    @Override
    protected void obtainInput()
    {
        source.setAttachment(file);
        source.setName(nameField.getText());
        
        String name = file.getName();
        source.setType(name.substring(name.lastIndexOf(".")));
    }
    
    private void chooseFile()
    {
        FileChooser chooser = new FileChooser();
        
        File choosed = chooser.showOpenDialog( getDialogPane().getScene().getWindow() );
        
        if (file == null && choosed == null)
        {
            close();
        }
        
        else if (choosed != null)
        {
            file = choosed;
            filePath.setText(file.getAbsolutePath());
        }
    }
    
    private void setValidations()
    {
        addValidation(nameField);
    }
    
    private void initComponents()
    {
        setWidth(400);
        setHeight(100);
        
        vbox.setPrefSize(getWidth(), getHeight());
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(0, 0, 10, 0));
        HBox.setHgrow(nameField, Priority.ALWAYS);
        
        changeFile.setPrefSize(getWidth(), 60);
        
        getDialogPane().setContent(vbox);
        
        changeFile.setOnAction((t) ->
        {
            chooseFile();
        });
    }
    
    private Label nameLabel = new Label("Nome:");
    private TextField nameField = new TextField();
    
    private Label filePath = new Label();
    private Button changeFile = new Button("Alterar arquivo");
    
    private HBox hbox = new HBox(nameLabel, nameField);
    private VBox vbox = new VBox(hbox, filePath, changeFile);
}
