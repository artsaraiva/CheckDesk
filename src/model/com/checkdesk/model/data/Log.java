package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

import com.checkdesk.control.util.LogUtilities;
import java.text.DateFormat;
import java.util.Date;

/**
 * Logs generated by hbm2java
 */
public class Log
        implements java.io.Serializable
{
    public static final int EVENT_ADD         = 0;
    public static final int EVENT_UPDATE      = 1;
    public static final int EVENT_DELETE      = 2;
    public static final int EVENT_ACTIVE_LOGS = 3;
    
    private int id;
    private Date timestamp;
    private User user;
    private int event;
    private String objectName;
    private String objectClass;
    private String command;

    public Log()
    {
    }

    public Log(int id, User user, int event, String objectName, String objectClass, String command)
    {
        this.id = id;
        this.user = user;
        this.event = event;
        this.objectName = objectName;
        this.objectClass = objectClass;
        this.command = command;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Date getTimestamp()
    {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
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
        return new StringBuilder().append(DateFormat.getDateInstance().format(timestamp)).append("|")
                                  .append(user != null ? user.getLogin() : "").append("|")
                                  .append(LogUtilities.getEvent(event)).append("|")
                                  .append(objectClass.substring(objectClass.lastIndexOf(".") + 1)).toString();
    }
}
