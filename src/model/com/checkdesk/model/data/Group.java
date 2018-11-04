package com.checkdesk.model.data;

import java.util.List;

/**
 * Groups
 */
public class Group
        extends Entity
{
    private int groupId;
    private List<User> users;

    public Group()
    {
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }
    
    public boolean contains(User user)
    {
        return users.contains(user);
    }
    
    public void add(User user)
    {
        if (user != null && !users.contains(user))
        {
            users.add(user);
        }
    }
}
