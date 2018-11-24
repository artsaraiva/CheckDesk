package com.checkdesk.model.data;

/**
 * Permissions
 */
public class Permission
        extends Entity
{
    private Integer viewersId;
    private String name;

    public Permission()
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

    @Override
    public String toString()
    {
        return this.name;
    }
}
