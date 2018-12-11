/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.util;

import javafx.event.EventHandler;

/**
 *
 * @author MNicaretta
 */
public abstract class Callback<T>
        implements EventHandler
{
    private T source;

    public Callback(T source)
    {
        this.source = source;
    }

    public T getSource()
    {
        return this.source;
    }
}
