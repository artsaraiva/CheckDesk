/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.ServerConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author MNicaretta
 */
public class Prompts
{
    public static void info(String message)
    {
        info(null, message);
    }

    public static void info(String title, String message)
    {
        Alert dialogInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogInfo.getDialogPane().setPrefSize(400, 200);
        dialogInfo.setTitle("Informação");
        dialogInfo.setHeaderText(title == null ? "Informação" : title);
        dialogInfo.setContentText(message);
        dialogInfo.showAndWait();
    }

    public static boolean confirm(String message)
    {
        return confirm(null, message);
    }

    public static boolean confirm(String title, String message)
    {
        ButtonType btnSim = new ButtonType("Sim");
        ButtonType btnNao = new ButtonType("Não");

        Alert dialogConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        dialogConfirm.getDialogPane().setPrefSize(400, 200);
        dialogConfirm.setTitle("Confirmação");
        dialogConfirm.setHeaderText(title == null ? "Você tem certeza ?" : title);
        dialogConfirm.setContentText(message);
        dialogConfirm.getButtonTypes().setAll(btnSim, btnNao);
        dialogConfirm.showAndWait();

        return dialogConfirm.getResult() == btnSim;
    }

    public static void editConnectionSettings()
    {
        TextInputDialog dialogInput = new TextInputDialog();
        dialogInput.getDialogPane().setPrefSize(400, 200);
        dialogInput.setTitle("Configuração de Conexão");
        dialogInput.setHeaderText("Digite o endereço com a porta do servido:\n" +
                                  "Exemplo: 127.0.0.1:5000");
        
        dialogInput.showAndWait();
        ServerConnection.setConnectionSettings(dialogInput.getResult());
    }

}
