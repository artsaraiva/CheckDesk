/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.model.data.Permission;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 *
 * @author arthu
 */
public class PermissionTableItem
        extends GridPane
{
        private Permission permission;

    public PermissionTableItem(Permission permission)
    {
        setSource(permission);
        initComponents();
    }

    private void setSource(Permission permission)
    {
        this.permission = permission;
        permissionLabel.setText(permission.getName());
       /* iconUser.setImage(new Image(ResourceLocator.getInstance().getImageResource(survey.getOwner().getLogin())));
        surveyLabel.setText(survey.getTitle());

        DateFormat df = DateFormat.getDateInstance();
        dueLabel.setText(df.format(survey.getCreatedDate()));
        
        double randomValue = Math.random();
        progressIndicator.setProgress(randomValue);
        tooltip.setText(String.format("%.2f%%", randomValue * 100));*/
    }
    
    public Permission getPermission()
    {
        return permission;
    }

    private void initComponents()
    {
        /*iconUser.setClip(new Circle(38, 38, 36, Paint.valueOf("#FFFFFF")));

        surveyLabel.getStyleClass().add("home-table-survey");
        dueLabel.getStyleClass().add("home-table-item");
        progressIndicator.getStyleClass().add("answer-progress-graph");
        tooltip.getStyleClass().add("tooltip-progress-graph");

        progressIndicator.setTooltip(tooltip);
        addRow(0, iconUser, surveyLabel, dueLabel, progressIndicator);

        ColumnConstraints c1 = new ColumnConstraints();
        ColumnConstraints c2 = new ColumnConstraints();
        ColumnConstraints c3 = new ColumnConstraints();
        ColumnConstraints c4 = new ColumnConstraints();

        c1.setMinWidth(90);
        c2.setHgrow(Priority.ALWAYS);
        c4.setMinWidth(90);
        c4.setHalignment(HPos.RIGHT);
        getColumnConstraints().addAll(c1, c2, c3, c4);
        */
        permissionLabel.getStyleClass().add("home-table-survey");
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(90);
        getChildren().addAll(permissionLabel);
    }

    private Label permissionLabel = new Label();
}
