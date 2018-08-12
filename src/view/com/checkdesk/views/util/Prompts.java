/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.util;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;

/**
 *
 * @author MNicaretta
 */
public class Prompts
{
    public static void info ( String message )
    {
        info( null, message );
    }
    
    public static void info ( String title, String message )
    {
        Alert dialogoInfo = new Alert( Alert.AlertType.INFORMATION );
        dialogoInfo.getDialogPane().setPrefSize( 400, 200 );
        dialogoInfo.setTitle( "Informação" );
        dialogoInfo.setHeaderText( title == null ? "Informação" : title );
        dialogoInfo.setContentText( message );
        dialogoInfo.showAndWait();
    }
}
