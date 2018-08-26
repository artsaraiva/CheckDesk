/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
    
    public static boolean confirm( String message )
    {
        return confirm( null, message );
    }
    
    public static boolean confirm( String title, String message )
    {
        ButtonType btnSim = new ButtonType( "Sim" );
        ButtonType btnNao = new ButtonType( "Não" );
        
        Alert dialogoConfirm = new Alert( Alert.AlertType.CONFIRMATION );
        dialogoConfirm.getDialogPane().setPrefSize( 400,  200 );
        dialogoConfirm.setTitle( "Confirmação" );
        dialogoConfirm.setHeaderText( title == null ? "Você tem certeza ?" : title );
        dialogoConfirm.setContentText( message );
        dialogoConfirm.getButtonTypes().setAll( btnSim, btnNao );
        dialogoConfirm.showAndWait();
        
        return dialogoConfirm.getResult() == btnSim;
    }
}
