package com.checkdesk.control.data;
// Generated 09/08/2018 21:08:03 by Hibernate Tools 4.3.1



/**
 * Permissions generated by hbm2java
 */
public class Permissions  implements java.io.Serializable {


     private int id;
     private UserGroups userGroups;
     private String name;

    public Permissions() {
    }

	
    public Permissions(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Permissions(int id, UserGroups userGroups, String name) {
       this.id = id;
       this.userGroups = userGroups;
       this.name = name;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public UserGroups getUserGroups() {
        return this.userGroups;
    }
    
    public void setUserGroups(UserGroups userGroups) {
        this.userGroups = userGroups;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }




}


