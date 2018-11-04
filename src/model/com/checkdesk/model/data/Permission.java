package com.checkdesk.model.data;

/**
 * Permissions
 */
public class Permission
        extends Entity
{
    private int viewersId;
    private String name;

    public Permission()
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

    @Override
    public String toString()
    {
        return this.name;
    }
}
