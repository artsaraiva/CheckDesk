/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.db.fetchers;

import com.checkdesk.model.data.Entity;
import com.checkdesk.model.db.Database;
import com.checkdesk.model.db.Schemas.Schema;
import java.sql.ResultSet;

/**
 *
 * @author MNicaretta
 * @param <T>
 */
public interface Fetcher<T extends Entity>
{
    public T fetch(ResultSet resultSet) throws Exception;
    public String insert(Database db, Schema schema, T value) throws Exception;
    public String update(Database db, Schema schema, T value) throws Exception;
    public String delete(Database db, Schema schema, T value) throws Exception;
}
