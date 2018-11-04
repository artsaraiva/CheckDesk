/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.control.util.GroupUtilities;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Groups;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 *
 * @author MNicaretta
 */
public class GroupFetcher
        implements Fetcher<Group>
{
    @Override
    public Group fetch(ResultSet resultSet) throws Exception
    {
        int groupId = resultSet.getInt(1);
        
        Group result = GroupUtilities.getGroup(groupId);

        if (result == null)
        {
            result = new Group();
            result.setId(groupId);
            
            GroupUtilities.cacheGroup(result);
        }
        
        result.add((User) EntityService.getInstance().getValue(User.class, resultSet.getInt(2)));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Group value) throws Exception
    {
        Groups G = (Groups) schema.alias("");

        List ids = EntityService.getInstance().getFieldValues(Group.class.getDeclaredField("id"),
                                                              Group.class,
                                                              Arrays.asList(new Parameter(Group.class.getDeclaredField("id"),
                                                                                          0,
                                                                                          Parameter.COMPARATOR_MAX_VALUE)));
        int currentId = 0;
        
        if (!ids.isEmpty())
        {
            currentId = (int) ids.get(0);
        }
        
        value.setGroupId(currentId + 1);
        
        StringJoiner joiner = new StringJoiner(", ");
        
        joiner.add("( " +
                        value.getGroupId() + ", " +
                        0 + ", " +
                   " )");
        
        for (User user : value.getUsers())
        {
            joiner.add("( " +
                            value.getGroupId() + ", " +
                            user.getId() + ", " +
                       " )");
        }
        
        return "insert into " + schema.name +
               " ( " +
                    G.columns.REF_GROUP + ", " +
                    G.columns.REF_USER  +
               " ) values " + joiner.toString(); 
    }

    @Override
    public String update(Database db, Schema schema, Group value) throws Exception
    {
        return "";
    }

    @Override
    public String delete(Database db, Schema schema, Group value) throws Exception
    {
        Groups G = (Groups) schema.alias("");
        
        return "delete from " + G.name +
               " where " +
                G.columns.REF_GROUP + " = " + value.getGroupId() +
                " and " +
                G.columns.REF_GROUP + " <> " + G.columns.ID;
    }

}
