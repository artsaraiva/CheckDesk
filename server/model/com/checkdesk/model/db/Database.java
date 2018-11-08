package com.checkdesk.model.db;

import com.checkdesk.control.ConfigurationManager;
import com.checkdesk.model.data.Entity;
import com.checkdesk.model.db.fetchers.Fetcher;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcelo.nicaretta
 */
public class Database
{
    private static final String DB_URL = ConfigurationManager.getInstance().getString("db.url");
    private static final String DB_USER = ConfigurationManager.getInstance().getString("db.user");
    private static final String DB_PASSWORD = ConfigurationManager.getInstance().getString("db.password");
    private static final String DB_DRIVER = ConfigurationManager.getInstance().getString("db.driver");

    private static Database instance;
    private static Connection connection;
    private static Statement statement;

    public static Database getInstance() throws Exception
    {
        if (instance == null)
        {
            instance = new Database();
        }

        initConnection();

        return instance;
    }

    private Database() throws Exception
    {
        DriverManager.registerDriver((Driver) Class.forName(DB_DRIVER).newInstance());
    }

    private static void initConnection() throws Exception
    {
        if (connection == null || connection.isClosed())
        {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
    }

    public void release() throws Exception
    {
        if (connection != null && statement != null)
        {
            statement.close();
        }
    }

    public void executeCommand(String sql) throws Exception
    {
        if (connection != null)
        {
            statement = connection.createStatement();

            statement.execute(sql);

            statement.close();
        }
    }

    public <T extends Entity> T fetchOne(String sql, Fetcher<T> fetcher) throws Exception, Exception
    {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        T t = null;

        if (resultSet.next())
        {
            t = fetcher.fetch(resultSet);
        }

        resultSet.close();

        statement.close();

        return t;
    }

    public <T extends Entity> List<T> fetchAll(String sql, Fetcher<T> fetcher) throws Exception
    {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        List<T> list = new ArrayList();

        while (resultSet.next())
        {
            list.add(fetcher.fetch(resultSet));
        }

        resultSet.close();

        statement.close();

        return list;
    }

    public int queryInt(String sql) throws Exception
    {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        int result = 0;

        if (resultSet.next())
        {
            result = resultSet.getInt(1);
        }

        resultSet.close();

        statement.close();

        return result;
    }

    public Object query(String sql) throws Exception
    {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        Object result = null;

        if (resultSet.next())
        {
            result = resultSet.getObject(1);
        }

        resultSet.close();

        statement.close();

        return result;
    }

    public List<Object[]> queryMatrix(String sql, int columns) throws Exception
    {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        List<Object[]> result = new ArrayList<>();

        while (resultSet.next())
        {
            Object[] list = new Object[columns];
            
            for (int i = 0; i < columns; i++)
            {
                list[i] = resultSet.getObject(i + 1);
            }
            
            result.add(list);
        }

        resultSet.close();

        statement.close();

        return result;
    }
    
    public List queryList(String sql) throws Exception
    {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        List result = new ArrayList();

        while (resultSet.next())
        {
            result.add(resultSet.getObject(1));
        }

        resultSet.close();

        statement.close();

        return result;
    }

    public String quote(Object object)
    {
        if (object == null)
        {
            return "null";
        }
        
        if (object instanceof Number)
        {
            return String.valueOf(object);
        }
        
        return "\'" + object.toString() + "\'";
    }

    public String quote(String sql)
    {
        if (sql == null)
        {
            return "null";
        }

        if (sql.contains("\'"))
        {
            sql = sql.replace("\'", "\''");
        }

        return "\'" + sql + "\'";
    }

    public String quote(char c)
    {
        return quote(String.valueOf(c));
    }
}
