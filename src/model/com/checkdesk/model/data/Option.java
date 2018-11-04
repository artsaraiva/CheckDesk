package com.checkdesk.model.data;

/**
 * Option
 */
public class Option
        extends Entity
{
    private int viewersId;
    private String name;
    private int type;

    public Option()
    {
    }

    public int getViewersId()
    {
        return this.viewersId;
    }

    public void setViewersId(int viewersId)
    {
        this.viewersId = viewersId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
