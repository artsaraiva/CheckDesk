package com.checkdesk.model.data;

/**
 * Forms
 */
public class Form
        extends Entity
{
    private int viewersId;
    private String name;
    private String info;

    public Form()
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
