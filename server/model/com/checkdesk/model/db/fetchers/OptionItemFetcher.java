/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.OptionItem;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import com.checkdesk.model.db.Schemas.OptionItems;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 */
public class OptionItemFetcher
        implements Fetcher<OptionItem>
{
    @Override
    public OptionItem fetch(ResultSet resultSet) throws Exception
    {
        OptionItem result = new OptionItem();

        int count = 1;

        result.setId(resultSet.getInt(count++));
        result.setName(resultSet.getString(count++));
        result.setValue(resultSet.getString(count++));
        result.setOptionId(resultSet.getInt(count++));

        return result;
    }

    @Override
    public String insert(Database db, Schema schema, OptionItem value) throws Exception
    {
        OptionItems I = (OptionItems) schema.alias("");

        return "insert into " + schema.name +
               " ( " +
                    I.columns.NAME       + ", " +
                    I.columns.VALUE      + ", " +
                    I.columns.REF_OPTION +
               " ) values( " +
                    db.quote(value.getName())  + ", " +
                    db.quote(value.getValue()) + ", " +
                    value.getOptionId()        +
               " )"+
               " returning " + I.columns.ID;
    }

    @Override
    public String update(Database db, Schema schema, OptionItem value) throws Exception
    {
        OptionItems I = (OptionItems) schema.alias("");
        
        return "update " + I.name + " set " +
                    I.columns.NAME       + " = " + db.quote(value.getName())  + ", " +
                    I.columns.VALUE      + " = " + db.quote(value.getValue()) + ", " +
                    I.columns.REF_OPTION + " = " + value.getOptionId()        +
               " where " +
                    I.columns.ID + " = " + value.getId();
    }

    @Override
    public String delete(Database db, Schema schema, OptionItem value) throws Exception
    {
        OptionItems I = (OptionItems) schema.alias("");
        
        return "delete from " + I.name +
               " where " + I.columns.ID + " = " + value.getId();
    }
}
