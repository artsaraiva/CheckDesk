/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.views.parts.NavigationItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author arthu
 */
public class HeaderPane
        extends HBox
{

    public HeaderPane()
    {
        initComponents();
    }

    public ObservableValue getUserPaneWidth()
    {
        return userPane.widthProperty();
    }

    public void refreshContent()
    {
        userPane.refreshContent();
    }

    public void setNavigationItem(NavigationItem item)
    {
        navigationPane.setItem(item);
    }

    public NavigationItem getNavigationItem()
    {
        return navigationPane.getItem();
    }

    private void initComponents()
    {
        setHgrow(navigationPane, Priority.ALWAYS);
        dictionaryButton.setText("Dicionário");
        dictionaryButton.setMinWidth(55);
        dictionaryButton.setMinHeight(125);
        getChildren().addAll(userPane, navigationPane, dictionaryButton);

        navigationPane.addEventHandler(NavigationPane.Events.ON_SELECT, (Event event) ->
        {
            fireEvent(event);
        });

        dictionaryButton.setOnAction((ActionEvent t) ->
        {
            TextInputDialog dialog = new TextInputDialog("");

            dialog.setTitle("Dicionário");
            dialog.setHeaderText("Busque o significado de uma palavra");
            dialog.setContentText("Palavra:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent((String name) ->
            {
                try
                {
                    URL url = new URL("http://dicionario-aberto.net/search-json/" + name);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    if (conn.getResponseCode() != 200)
                    {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    
                    JSONObject json = new JSONObject(sb.toString());
                    System.out.println(json);
                    if(json.getJSONObject("superEntry") != null)
                    {
                        
                    }
                    
                    //System.out.println(json.getJSONObject("entry").getJSONArray("sense").get(0));
                   Alert alert = new Alert(Alert.AlertType.INFORMATION, "Veículo de rodas, para transporte de coisas ou pessoas. Carro de mão, pequeno veículo de uma só roda, para transporte de entulho, pedras.");
                   alert.setTitle("Dicionário");
                   alert.setHeaderText("Significado da palavra");
                   alert.showAndWait();
                    conn.disconnect();

                }
                catch (Exception e)
                {

                    ApplicationController.logException(e);

                }
            });
        });
    }

    private LoggedUserPane userPane = new LoggedUserPane();
    private NavigationPane navigationPane = new NavigationPane();
    private Button dictionaryButton = new Button();
}
