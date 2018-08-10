package com.checkdesk.control.data;
// Generated 09/08/2018 21:08:03 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * UserGroups generated by hbm2java
 */
public class UserGroups  implements java.io.Serializable {


     private int id;
     private String name;
     private Set userses = new HashSet(0);
     private Set formses = new HashSet(0);
     private Set optionses = new HashSet(0);
     private Set surveysesForRefParticipants = new HashSet(0);
     private Set surveysesForRefViewers = new HashSet(0);
     private Set permissionses = new HashSet(0);
     private Set categorieses = new HashSet(0);

    public UserGroups() {
    }

	
    public UserGroups(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public UserGroups(int id, String name, Set userses, Set formses, Set optionses, Set surveysesForRefParticipants, Set surveysesForRefViewers, Set permissionses, Set categorieses) {
       this.id = id;
       this.name = name;
       this.userses = userses;
       this.formses = formses;
       this.optionses = optionses;
       this.surveysesForRefParticipants = surveysesForRefParticipants;
       this.surveysesForRefViewers = surveysesForRefViewers;
       this.permissionses = permissionses;
       this.categorieses = categorieses;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Set getUserses() {
        return this.userses;
    }
    
    public void setUserses(Set userses) {
        this.userses = userses;
    }
    public Set getFormses() {
        return this.formses;
    }
    
    public void setFormses(Set formses) {
        this.formses = formses;
    }
    public Set getOptionses() {
        return this.optionses;
    }
    
    public void setOptionses(Set optionses) {
        this.optionses = optionses;
    }
    public Set getSurveysesForRefParticipants() {
        return this.surveysesForRefParticipants;
    }
    
    public void setSurveysesForRefParticipants(Set surveysesForRefParticipants) {
        this.surveysesForRefParticipants = surveysesForRefParticipants;
    }
    public Set getSurveysesForRefViewers() {
        return this.surveysesForRefViewers;
    }
    
    public void setSurveysesForRefViewers(Set surveysesForRefViewers) {
        this.surveysesForRefViewers = surveysesForRefViewers;
    }
    public Set getPermissionses() {
        return this.permissionses;
    }
    
    public void setPermissionses(Set permissionses) {
        this.permissionses = permissionses;
    }
    public Set getCategorieses() {
        return this.categorieses;
    }
    
    public void setCategorieses(Set categorieses) {
        this.categorieses = categorieses;
    }




}


