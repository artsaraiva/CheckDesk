/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.views.pickers.ItemPicker;
import java.util.Arrays;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author MNicaretta
 */
public class ItemSelector<T>
        extends TextField
{
    public static class Events
    {
        public static final EventType<Event> ON_SELECT = new EventType<Event>("onSelectorSelect");
    }

    private T selected;

    public ItemSelector()
    {
        initComponents();
    }

    public ItemSelector(T... items)
    {
        initComponents();

        setItems(Arrays.asList(items));
    }

    public void setItems(List<T> items)
    {
        picker.setItems(items);
        setSelected(null);
    }

    @Override
    public void setWidth(double width)
    {
        super.setWidth(width);
        setPrefWidth(width);
    }

    public void setSelected(T value)
    {
        setSelected(value, true);
    }

    public void setSelected(T value, boolean fireEvent)
    {
        this.selected = value;

        setText(selected != null ? selected.toString() : "");
        setFocused(false);

        if (fireEvent)
        {
            fireEvent(new Event(Events.ON_SELECT));
        }
    }

    public T getSelected()
    {
        return selected;
    }

    public void changePicker(ItemPicker<T> picker)
    {
        List<T> items = this.picker.getItems();

        this.picker = picker;

        if (items != null)
        {
            this.picker.setItems(items);
        }
    }

    protected void choose()
    {
        picker.open("Selecione um item");

        if (picker.getSelected() != null)
        {
            setSelected(picker.getSelected());
        }
    }

    private void initComponents()
    {
        setEditable(false);
        setPromptText("Clique para selecionar");
        setTooltip(new Tooltip("Clique para selecionar"));

        setOnMouseClicked((MouseEvent event) ->
        {
            if (event.getButton().equals(MouseButton.PRIMARY))
            {
                choose();
            }
        });
    }

    private ItemPicker<T> picker = new ItemPicker<>();
}
