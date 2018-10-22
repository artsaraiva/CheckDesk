/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Log;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author MNicaretta
 */
public class LogUtilities
{

    public static final Item EVENT_ADD = new Item("Adicionar", Log.EVENT_ADD);
    public static final Item EVENT_UPDATE = new Item("Editar", Log.EVENT_UPDATE);
    public static final Item EVENT_DELETE = new Item("Excluir", Log.EVENT_DELETE);
    public static final Item EVENT_ACTIVE_LOGS = new Item("Ativar/Desativar auditoria", Log.EVENT_ACTIVE_LOGS);

    private static final int OPTION_USER = 0;
    private static final int OPTION_DATE = 1;
    private static final int OPTION_ACTION = 2;

    public static void addLog(Log log)
    {
        try
        {
            EntityService.getInstance().save(log);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }

    public static ObservableList<Item> getFilterOptions()
    {
        return FXCollections.observableArrayList(new Item("Usuário", OPTION_USER),
                new Item("Data", OPTION_DATE),
                new Item("Ação", OPTION_ACTION));
    }

    public static List filtersFor(Item item)
    {
        List result = new ArrayList();

        try
        {
            switch (item.getValue())
            {
                case OPTION_USER:
                    result = EntityService.getInstance().getValues(User.class);
                    break;

                case OPTION_DATE:
                    result = getDateList();
                    break;

                case OPTION_ACTION:
                    result = getEventsList();
                    break;
            }
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    private static List<Date> getDateList()
    {
        List<Date> objects = new ArrayList<>();

        try
        {
            List<Date> dates = EntityService.getInstance().getFieldValues(Log.class.getDeclaredField("timestamp"), Log.class);

            for (Date d : dates)
            {
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.set(Calendar.HOUR, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                if (!objects.contains(c.getTime()))
                {
                    objects.add(c.getTime());
                }
            }
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return objects;
    }

    private static List<Item> getEventsList()
    {
        return Arrays.asList(EVENT_ADD, EVENT_UPDATE, EVENT_DELETE, EVENT_ACTIVE_LOGS);
    }

    public static Item getEvent(int type)
    {
        Item result = null;

        for (Item item : getEventsList())
        {
            if (item.getValue() == type)
            {
                result = item;
                break;
            }
        }

        return result;
    }

    public static List<Log> getLogs(Date value, Date value2)
    {
        List<Log> result = new ArrayList<>();

        List<Parameter> parameters = new ArrayList<>();

        try
        {
            if (value != null)
            {
                parameters.add(new Parameter("from", Log.class.getDeclaredField("timestamp"), value, Parameter.COMPARATOR_DATE_FROM));
            }

            if (value2 != null)
            {
                parameters.add(new Parameter("until", Log.class.getDeclaredField("timestamp"), value2, Parameter.COMPARATOR_DATE_UNTIL));
            }
            
            result = EntityService.getInstance().getValues(Log.class, parameters);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static Log getLog(int logId)
    {
        Log result = null;

        try
        {
            result = (Log) EntityService.getInstance().getValue(Log.class, logId);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }
    
    public static void transferLogs(String table, Date from, Date until)
    {
        try
        {
            Map<String,Object> parameters = new LinkedHashMap<>();
            
            parameters.put("table", table); 
            parameters.put("from", new java.sql.Timestamp(from.getTime()));
            parameters.put("until",  new java.sql.Timestamp(until.getTime()));
            
            EntityService.getInstance().executeFunction("transfer_logs_to_aux_table", parameters);
        }
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
}
