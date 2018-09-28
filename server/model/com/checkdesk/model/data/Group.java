package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Groups generated by hbm2java
 */
public class Group
        implements java.io.Serializable
{
    private int id;
    private String name;
    private Set users = new HashSet(0);
    private Set options = new HashSet(0);
    private Set surveysViewers = new HashSet(0);
    private Set categories = new HashSet(0);
    private Set surveysParticipants = new HashSet(0);
    private Set permissions = new HashSet(0);
    private Set forms = new HashSet(0);

    public Group()
    {
    }

    public Group(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Group(int id, String name, Set users, Set options, Set surveysViewers, Set categories, Set surveysParticipants, Set permissions, Set forms)
    {
        this.id = id;
        this.name = name;
        this.users = users;
        this.options = options;
        this.surveysViewers = surveysViewers;
        this.categories = categories;
        this.surveysParticipants = surveysParticipants;
        this.permissions = permissions;
        this.forms = forms;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set getUsers()
    {
        return this.users;
    }

    public void setUsers(Set users)
    {
        this.users = users;
    }

    public Set getOptions()
    {
        return this.options;
    }

    public void setOptions(Set options)
    {
        this.options = options;
    }

    public Set getSurveysViewers()
    {
        return this.surveysViewers;
    }

    public void setSurveysViewers(Set surveysViewers)
    {
        this.surveysViewers = surveysViewers;
    }

    public Set getCategories()
    {
        return this.categories;
    }

    public void setCategories(Set categories)
    {
        this.categories = categories;
    }

    public Set getSurveysParticipants()
    {
        return this.surveysParticipants;
    }

    public void setSurveysParticipants(Set surveysParticipants)
    {
        this.surveysParticipants = surveysParticipants;
    }

    public Set getPermissions()
    {
        return this.permissions;
    }

    public void setPermissions(Set permissions)
    {
        this.permissions = permissions;
    }

    public Set getForms()
    {
        return this.forms;
    }

    public void setForms(Set forms)
    {
        this.forms = forms;
    }
}
