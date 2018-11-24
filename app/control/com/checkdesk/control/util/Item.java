/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

/**
 *
 * @author MNicaretta
 */
public class Item
{
    private String label;
    private int value;

    public Item(String label, int value)
    {
        this.label = label;
        this.value = value;
    }

    public String getLabel()
    {
        return label;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return label;
    }
}
