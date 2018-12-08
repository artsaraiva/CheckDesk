/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.util.ReleaseUtilities;
import com.checkdesk.views.parts.DefaultTable;
import javafx.event.Event;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

/**
 *
 * @author arthu
 */
public class ReleasePane
        extends DefaultPane
{

    public ReleasePane()
    {
        initComponents();
    }

    @Override
    protected void resize()
    {
        releaseTable.setPrefHeight(getHeight());
        releaseTable.setPrefWidth(getWidth() / 3);
        releaseDetails.setPrefHeight(getHeight());
        releaseDetails.setPrefWidth(getWidth() * 2 / 3);
    }

    @Override
    public void refreshContent()
    {
        releaseTable.setItems(ReleaseUtilities.getReleases());
    }

    private void initComponents()
    {
        releaseTable.setShowAddPane(false);
        
        releaseDetails.setPrefHeight(200);

        releaseDetails.getEngine().documentProperty().addListener((obj, prev, newv) ->
        {
            String heightText = releaseDetails.getEngine().executeScript(
                    "var body = document.body,"
                    + "    html = document.documentElement;"
                    + "Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight);"
            ).toString();

            Double height = Double.parseDouble(heightText.replace("px", "")) + 15;
            releaseDetails.setPrefHeight(height);
        });
        
        hbox.getChildren().addAll(releaseTable, releaseDetails);

        getChildren().add(hbox);

        releaseTable.addEventHandler(DefaultTable.Events.ON_SELECT, (Event event) ->
        {
            String html = ReleaseUtilities.buildTopicsHtml(null, releaseTable.getSelectedItem());
            releaseDetails.getEngine().loadContent("<style>body{font-size:12px;font-family:arial}</style><body class=\"details-html\" contenteditable=\"false\">" + html + "</body>");
        });
    }

    private HBox hbox = new HBox();
    private DefaultTable<String> releaseTable = new DefaultTable();
    private WebView releaseDetails = new WebView();
}
