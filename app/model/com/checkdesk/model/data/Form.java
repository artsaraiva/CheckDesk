package com.checkdesk.model.data;

/**
 * Forms
 */
public class Form
        extends Entity
{
    private Integer viewersId;
    private String name;
    private String info;

    public Form()
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

    @Override
    public Form clone()
    {
        Form form = new Form();
        form.setViewersId(viewersId);
        form.setName(name);
        form.setInfo(info);
        
        return form;
    }
}
