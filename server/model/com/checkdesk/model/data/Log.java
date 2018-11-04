package com.checkdesk.model.data;

import com.checkdesk.control.util.LogUtilities;
import com.checkdesk.control.util.UserUtilities;
import java.sql.Timestamp;
import java.text.DateFormat;

/**
 * Logs
 */
public class Log
        extends Entity
{
    public static final int EVENT_ADD         = 0;
    public static final int EVENT_UPDATE      = 1;
    public static final int EVENT_DELETE      = 2;
    public static final int EVENT_ACTIVE_LOGS = 3;
    
    private Timestamp timestamp;
    private int userId;
    private int event;
    private String objectName;
    private String objectClass;
    private String command;

    public Log()
    {
    }

    public Timestamp getTimestamp()
    {
        return this.timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getEvent()
    {
        return this.event;
    }

    public void setEvent(int event)
    {
        this.event = event;
    }

    public String getObjectName()
    {
        return this.objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    public String getObjectClass()
    {
        return this.objectClass;
    }

    public void setObjectClass(String objectClass)
    {
        this.objectClass = objectClass;
    }

    public String getCommand()
    {
        return this.command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }
    
    @Override
    public String toString()
    {
        User user = UserUtilities.getUser(userId);
        
        return new StringBuilder().append(DateFormat.getDateInstance().format(timestamp)).append("|")
                                  .append(user != null ? user.getLogin() : "").append("|")
                                  .append(LogUtilities.getEvent(event)).append("|")
                                  .append(objectClass.substring(objectClass.lastIndexOf(".") + 1)).toString();
    }
}
