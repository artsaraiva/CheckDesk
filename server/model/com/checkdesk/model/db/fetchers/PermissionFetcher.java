/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Permission;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Permissions;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class PermissionFetcher
        implements Fetcher<Permission>
{
    @Override
    public Permission fetch(ResultSet resultSet) throws Exception
    {
        Permission result = new Permission();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setViewersId(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Permission value) throws Exception
    {
        Permissions P = (Permissions) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    P.columns.NAME        + ", " +
                    P.columns.REF_VIEWERS +
               " ) values( " +
                    db.quote(value.getName()) + ", " +
                    value.getViewersId()      +
               " )"+
               " returning " + P.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Permission value) throws Exception
    {
        Permissions P = (Permissions) schema.alias("");
        
        return "update " + P.name + " set " +
                    P.columns.NAME        + " = " + db.quote(value.getName())  + ", " +
                    P.columns.REF_VIEWERS + " = " + value.getViewersId()        +
               " where " +
                    P.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Permission value) throws Exception
    {
        Permissions P = (Permissions) schema.alias("");
        
        return "delete from " + P.name +
               " where " + P.columns.ID + " = " + value.getId();
    }
}
