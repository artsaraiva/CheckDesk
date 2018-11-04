/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Category;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.Categories;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class CategoryFetcher
        implements Fetcher<Category>
{
    @Override
    public Category fetch(ResultSet resultSet) throws Exception
    {
        Category result = new Category();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setInfo(resultSet.getString(count++));
        result.setParentId(resultSet.getInt(count++));
        result.setViewersId(resultSet.getInt(count++));
        result.setOwnerId(resultSet.getInt(count++));
        result.setState(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, Category value) throws Exception
    {
        Categories C = (Categories) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    C.columns.NAME        + ", " +
                    C.columns.INFO        + ", " +
                    C.columns.REF_PARENT  + ", " +
                    C.columns.REF_VIEWERS + ", " +
                    C.columns.REF_USER    + ", " +
                    C.columns.STATE       +
               " ) values( " +
                    db.quote(value.getName()) + ", " +
                    db.quote(value.getInfo()) + ", " +
                    value.getParentId()       + ", " +
                    value.getViewersId()      + ", " +
                    value.getOwnerId()        + ", " +
                    value.getState()          +
               " )"+
               " returning " + C.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, Category value) throws Exception
    {
        Categories C = (Categories) schema.alias("");
        
        return "update " + C.name + " set " +
                    C.columns.NAME        + " = " + db.quote(value.getName()) + ", " +
                    C.columns.INFO        + " = " + db.quote(value.getInfo()) + ", " +
                    C.columns.REF_PARENT  + " = " + value.getParentId()       + ", " +
                    C.columns.REF_VIEWERS + " = " + value.getViewersId()      + ", " +
                    C.columns.REF_USER    + " = " + value.getOwnerId()        + ", " +
                    C.columns.STATE       + " = " + value.getState()          +
               " where " +
                    C.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, Category value) throws Exception
    {
        Categories C = (Categories) schema.alias("");
        
        return "update " + C.name + " set " +
                    C.columns.STATE + " = " + Category.STATE_INACTIVE +
               " where " +
                    C.columns.ID + " = " + value.getId();
    }
}
