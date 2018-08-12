/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.data.Survey;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author arthu
 */
public class HomeTable
    extends ListView<Survey>
{

    public HomeTable()
    {
       initComponents();
    }
    
    private void initComponents()
    {
       // setMinWidth( 560 );
        setCellFactory(new CellFactory());
        
        setItems(FXCollections.observableArrayList(
                new Survey(0, null, null, null, "Pesquisa de Satisfação", "", new Date(), 0),
                new Survey(0, null, null, null, "Pesquisa de Teste", "", new Date(), 0),
                new Survey(0, null, null, null, "Pesquisa de TCC", "", new Date(), 0)
        ));
    }
    
    private class CellFactory
        implements Callback<ListView<Survey>,ListCell<Survey>>
    {
        @Override
        public ListCell<Survey> call(ListView<Survey> param)
        {
            return new ListCell<Survey>()
            {
                @Override
                protected void updateItem(Survey item, boolean empty)
                {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty)
                    {
                        setText(null);
                        setTextFill(null);
                        setGraphic(null);
                    }
                    
                    else
                    {
                        setGraphic(new HomeTableItem(item));
                    }
                }
            };
        }
    }
}
