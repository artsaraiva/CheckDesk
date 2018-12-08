/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ConfigurationManager;
import com.checkdesk.views.parts.Prompts;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

/**
 *
 * @author arthu
 */
public class ReleaseUtilities
{

    public static void showRelease()
    {
        Properties release = ConfigurationManager.getInstance().getRelease();

        String releaseDate = release.getProperty("release.date");
        boolean read = Boolean.parseBoolean(release.getProperty("release.read", "true"));

        if (!release.isEmpty() && !read)
        {
            if (Prompts.showReleaseTopics("Release " + releaseDate, buildTopicsHtml(release, null)))
            {
                release.setProperty("release.read", "true");
                try
                {
                    release.store(new FileOutputStream("releases" + File.separator + "release_" + releaseDate + ".txt"), "");
                }
                catch (IOException e)
                {
                    ApplicationController.logException(e);
                }
            }
        }
    }

    public static List<String> getReleaseTopics(Properties release)
    {
        List<String> releaseTopics = new ArrayList<>();

        int topicTotal = Integer.parseInt(release.getProperty("topic.total", "0"));

        for (int i = 1; i <= topicTotal; i++)
        {
            String topic = release.getProperty("topic." + i);

            if (topic != null)
            {
                releaseTopics.add(topic);
            }
        }

        return releaseTopics;
    }

    public static List<String> getReleases()
    {
        ArrayList<String> releases = new ArrayList<>();
        File dir = new File("releases");

        if (!dir.exists() || !dir.isDirectory())
        {
            dir.mkdirs();
        }

        for (File fileEntry : dir.listFiles())
        {
            if (!fileEntry.isDirectory())
            {
                releases.add(fileEntry.getName());
            }
        }

        return releases;
    }

    public static String buildTopicsHtml(Properties release, String fileName)
    {
        if (release == null)
        {
            release = ConfigurationManager.getInstance().getRelease(fileName);
        }
        StringJoiner topics = new StringJoiner("<br/><br/>");
        List<String> releaseTopics = getReleaseTopics(release);

        for (int i = 1; i <= releaseTopics.size(); i++)
        {
            StringBuilder topic = new StringBuilder();
            topic.append("<b>Feature ").append(i).append(" - ")
                    .append(releaseTopics.get(i - 1))
                    .append("</b><br/>");

            topics.add(topic);
        }

        return topics.toString();
    }
}
