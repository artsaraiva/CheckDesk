	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import java.math.BigInteger;

/**
 *
 * @author arthu
 */
public class PermissionController
{

    private static PermissionController defaultInstance;

    public static PermissionController getInstance()
    {
        if (defaultInstance == null)
        {
            defaultInstance = new PermissionController();
        }

        return defaultInstance;
    }

    public boolean hasPermission(User user, String role)
    {
        boolean hasPermission = false;
        try
        {
            BigInteger count = (BigInteger) EntityService.getInstance().getValue("select count(*) from user_permissions where user_id = " + user.getId()
                    + " and permission_name = '" + role + "'");

            hasPermission = count.intValue() == 1;
        }
        catch (Exception ex)
        {
            ApplicationController.logException(ex);
        }

        return hasPermission;
    }
}