package com.checkdesk.model.data;

/**
 * Users
 */
public class User
        extends Entity
{
    public static final int TYPE_SUPER    = 0;
    public static final int TYPE_ADMIN    = 1;
    public static final int TYPE_OPERATOR = 2;
    public static final int TYPE_EXPLORER = 3;
    
    private String name;
    private String login;
    private String email;
    private String password;
    private String phone;
    private int type;

    public User()
    {
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLogin()
    {
        return this.login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
