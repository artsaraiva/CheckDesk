/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arthu
 */
public class GroupUtilities
{
    public static List<Group> cacheGroup = new ArrayList<>();
    
    public static final User allUser = new User();
    static
    {
        allUser.setName("TODOS");
    }

    public static void saveGroup(Group group)
    {
        try
        {
            if (!group.getUsers().contains(allUser))
            {
                if (group.getId() == 0)
                {
                    EntityService.getInstance().save(group);
                }

                else
                {
                    EntityService.getInstance().update(group);
                }
            }

            else if (group.getId() != 0)
            {
                group.getUsers().remove(allUser);
                EntityService.getInstance().update(group);
            }
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    public static Group getGroup(Integer groupId)
    {
        return getGroup(groupId, true);
    }
    
    public static Group getGroup(Integer groupId, boolean select)
    {
        Group result = null;
        
        if (groupId != null)
        {
            for (Group group : cacheGroup)
            {
                if (group.getId() == groupId)
                {
                    result = group;
                }
            }

            if (select && result == null)
            {
                try
                {
                    result = (Group) EntityService.getInstance().getValue(Group.class, groupId);
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }
        
        return result;
    }
    
    public static void cacheGroup(Group group)
    {
        if (!cacheGroup.contains(group))
        {
            cacheGroup.add(group);
        }
    }
}
