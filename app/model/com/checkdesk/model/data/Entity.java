/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.data;

import com.checkdesk.control.util.Item;
import java.io.Serializable;

/**
 *
 * @author MNicaretta
 */
public abstract class Entity
        implements Serializable
{
    public final static int STATE_ACTIVE = 0;
    public final static int STATE_INACTIVE = 1;

    public final static Item[] STATES =
    {
        new Item("Ativo", STATE_ACTIVE),
        new Item("Inativo", STATE_INACTIVE)
    };

    protected int id;
    protected int state;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @Override
    public boolean equals(Object source)
    {
        if (source instanceof Entity)
        {
            return ((Entity) source).getId() == this.id;
        }

        return false;
    }
}
