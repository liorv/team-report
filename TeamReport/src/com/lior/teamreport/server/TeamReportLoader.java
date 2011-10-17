package com.lior.teamreport.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.lior.teamreport.client.TeamReportDO;

public class TeamReportLoader
{
  public static List<TeamReportDO> load(ArrayList<TeamData> data) {
    try {
      List<TeamReportDO> retval = new LinkedList<TeamReportDO>();

      for (TeamData d : data) {
        TeamReportDO tr = new TeamReportDO();
        tr.team = d.team;
        tr.manager = d.manager;
        try {
          parse(d.url, tr);
          retval.add(tr);
        }
        catch (Exception e) {
          return retval;
        }
      }

      return retval;
    }
    catch (Exception e) {
      e.printStackTrace();
      return new LinkedList<TeamReportDO>();
    }
  }

  private static List<String> readFile(BufferedReader br) throws IOException {
    List<String> retval = new LinkedList<String>();
    String line;
    for (line = br.readLine(); line != null; line = br.readLine()) {
      if (line.length() > 0) retval.add(line);
    }
    return retval;
  }

  private static void parse(String url, TeamReportDO tr) throws IOException {
    URL u = new URL(url);
    BufferedReader br =
        new BufferedReader(new InputStreamReader(u.openStream()));
    String titleLine = br.readLine();

    String meetStr = "meeting on ";
    int idx = titleLine.indexOf(meetStr);
    if (idx < 0)
      throw new Error("unable to file [" + meetStr + "] in [" + url + "]");
    idx += meetStr.length();
    tr.date = titleLine.substring(idx);

    List<String> lines = readFile(br);
    process(lines, tr);
  }

  private static void process(List<String> lines, TeamReportDO tr) {
    String[] keywords =
        {
            "Client issues",
            "dev plan",
            "people management",
            "lead initiatives",
            "complet" };

    Map<String, List<String>> chunkMap = new HashMap<String, List<String>>();
    Map<String, Pattern> patternMap = new HashMap<String, Pattern>();

    List<String> lineList = new LinkedList<String>();

    for (String keyword : keywords) {
      Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
      patternMap.put(keyword, p);
    }

    boolean isTitle = false;
    for (Iterator<String> it = lines.iterator(); it.hasNext();) {
      String line = it.next();
      for (String keyword : keywords) {
        if (patternMap.get(keyword).matcher(line).find()) {
          isTitle = true;
          lineList = chunkMap.get(keyword);
          if (lineList == null) lineList = new LinkedList<String>();
          chunkMap.put(keyword, lineList);
          break;
        }
      }
      if (isTitle)
        isTitle = false;
      else
        lineList.add(line);
    }

    tr.issues = chunkMap.get(keywords[0]);
    tr.dev = chunkMap.get(keywords[1]);
    tr.people = chunkMap.get(keywords[2]);
    tr.initiatives = chunkMap.get(keywords[3]);
    tr.done = chunkMap.get(keywords[4]);
  }
}
