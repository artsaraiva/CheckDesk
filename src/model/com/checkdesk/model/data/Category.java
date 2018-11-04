package com.checkdesk.model.data;

/**
 * Categories
 */
public class Category
        extends Entity
{
    private int parentId;
    private int viewersId;
    private int ownerId;
    private String name;
    private String info;

    public Category()
    {
    }

    public int getParentId()
    {
        return this.parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public int getViewersId()
    {
        return this.viewersId;
    }

    public void setViewersId(int viewersId)
    {
        this.viewersId = viewersId;
    }

    public int getOwnerId()
    {
        return this.ownerId;
    }

    public void setOwnerId(int ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInfo()
    {
        return this.info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
