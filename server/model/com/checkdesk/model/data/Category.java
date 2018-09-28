package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Categories generated by hbm2java
 */
public class Category
        implements java.io.Serializable
{
    private int id;
    private Category parent;
    private Group viewers;
    private User owner;
    private String name;
    private String info;
    private Set surveys = new HashSet(0);
    private Set children = new HashSet(0);

    public Category()
    {
    }

    public Category(int id, User owner, String name, String info)
    {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.info = info;
    }

    public Category(int id, Category parent, Group viewers, User owner, String name, String info, Set surveys, Set children)
    {
        this.id = id;
        this.parent = parent;
        this.viewers = viewers;
        this.owner = owner;
        this.name = name;
        this.info = info;
        this.surveys = surveys;
        this.children = children;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Category getParent()
    {
        return this.parent;
    }

    public void setParent(Category parent)
    {
        this.parent = parent;
    }

    public Group getViewers()
    {
        return this.viewers;
    }

    public void setViewers(Group viewers)
    {
        this.viewers = viewers;
    }

    public User getOwner()
    {
        return this.owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
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

    public Set getSurveys()
    {
        return this.surveys;
    }

    public void setSurveys(Set surveys)
    {
        this.surveys = surveys;
    }

    public Set getChildren()
    {
        return this.children;
    }

    public void setChildren(Set children)
    {
        this.children = children;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
