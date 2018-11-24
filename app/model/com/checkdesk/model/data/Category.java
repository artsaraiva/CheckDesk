package com.checkdesk.model.data;

/**
 * Categories
 */
public class Category
        extends Entity
{
    private Integer parentId;
    private Integer viewersId;
    private int ownerId;
    private String name;
    private String info;

    public Category()
    {
    }

    public Integer getParentId()
    {
        return this.parentId;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId == null || parentId == 0 ? null : parentId;
    }

    public Integer getViewersId()
    {
        return this.viewersId;
    }

    public void setViewersId(Integer viewersId)
    {
        this.viewersId = viewersId == null || viewersId == 0 ? null : viewersId;
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
