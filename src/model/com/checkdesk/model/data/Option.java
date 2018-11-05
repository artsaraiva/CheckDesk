package com.checkdesk.model.data;

/**
 * Option
 */
public class Option
        extends Entity
{
    private Integer viewersId;
    private String name;
    private int type;

    public Option()
    {
    }

    public Integer getViewersId()
    {
        return this.viewersId;
    }

    public void setViewersId(Integer viewersId)
    {
        this.viewersId = viewersId == null || viewersId == 0 ? null : viewersId;
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
