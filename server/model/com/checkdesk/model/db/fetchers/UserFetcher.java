/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.User;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Users;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class UserFetcher
        implements Fetcher<User>
{
    @Override
    public User fetch(ResultSet resultSet) throws Exception
    {
        User result = new User();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setLogin(resultSet.getString(count++));
        result.setEmail(resultSet.getString(count++));
        result.setPassword(resultSet.getString(count++));
        result.setPhone(resultSet.getString(count++));
        result.setType(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, User value) throws Exception
    {
        Users U = (Users) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    U.columns.NAME     + ", " +
                    U.columns.LOGIN    + ", " +
                    U.columns.EMAIL    + ", " +
                    U.columns.PASSWORD + ", " +
                    U.columns.PHONE    + ", " +
                    U.columns.TYPE     + ", " +
                    U.columns.STATE    +
               " ) values( " +
                    db.quote(value.getName())     + ", " +
                    db.quote(value.getLogin())    + ", " +
                    db.quote(value.getEmail())    + ", " +
                    db.quote(value.getPassword()) + ", " +
                    db.quote(value.getPhone())    + ", " +
                    value.getType()               + ", " +
                    value.getState()              +
               " )"+
               " returning " + U.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, User value) throws Exception
    {
        Users U = (Users) schema.alias("");
        
        return "update " + U.name + " set " +
                    U.columns.NAME     + " = " + db.quote(value.getName())     + ", " +
                    U.columns.LOGIN    + " = " + db.quote(value.getLogin())    + ", " +
                    U.columns.EMAIL    + " = " + db.quote(value.getEmail())    + ", " +
                    U.columns.PASSWORD + " = " + db.quote(value.getPassword()) + ", " +
                    U.columns.PHONE    + " = " + db.quote(value.getPhone())    + ", " +
                    U.columns.TYPE     + " = " + value.getType()               + ", " +
                    U.columns.STATE    + " = " + value.getState()              +
               " where " +
                    U.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, User value) throws Exception
    {
        Users U = (Users) schema.alias("");
        
        return "update " + U.name + " set " +
                    U.columns.STATE + " = " + User.STATE_INACTIVE +
               " where " +
                    U.columns.ID + " = " + value.getId();
    }

}
