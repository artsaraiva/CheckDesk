/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.views.parts.NavigationItem;
import com.checkdesk.views.parts.Prompts;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.json.JSONArray;
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
    
    private void searchDictionary()
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
                List<JSONObject> listEntries = new ArrayList();
                StringJoiner definitions = new StringJoiner("<br/><br/>");

                if (json.has("superEntry"))
                {
                    JSONArray superEntries = json.getJSONArray("superEntry");

                    for (int i = 0; i < superEntries.length(); i++)
                    {
                        listEntries.add(superEntries.getJSONObject(i).getJSONObject("entry"));
                    }
                }
                else
                {
                    listEntries.add(json.getJSONObject("entry"));
                }

                for (JSONObject entry : listEntries)
                {
                    JSONArray senses = entry.getJSONArray("sense");

                    for (int i = 0; i < senses.length(); i++)
                    {
                        StringBuilder def = new StringBuilder();
                        def.append("<b>def.")
                           .append(senses.length() > 1 ? String.valueOf(i + 1) : "")
                           .append("</b><br/>")
                           .append(senses.getJSONObject(i).getString("def"));

                        definitions.add(def);
                    }
                }

                Prompts.showDefinitons(name, definitions.toString());
                conn.disconnect();

            }
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        });
    }

    private void initComponents()
    {
        setHgrow(navigationPane, Priority.ALWAYS);
        setStyle("-fx-background-color: #20202C;");
        dictionaty.setImage(new Image(ResourceLocator.getInstance().getImageResource("hp_help.png")));
        dictionaty.setFitWidth(40);
        dictionaty.setFitHeight(40);
        dictionaty.setCursor(Cursor.HAND);
        getChildren().addAll(userPane, navigationPane, dictionaty);

        navigationPane.addEventHandler(NavigationPane.Events.ON_SELECT, (Event event) ->
        {
            fireEvent(event);
        });

        dictionaty.setOnMouseClicked((MouseEvent t) ->
        {
            if(t.getButton()==MouseButton.PRIMARY)
            {
                searchDictionary();
            }
        });
    }

    private LoggedUserPane userPane = new LoggedUserPane();
    private NavigationPane navigationPane = new NavigationPane();
    private ImageView dictionaty = new ImageView();
}
