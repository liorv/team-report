package com.lior.teamreport.client;

import java.io.Serializable;
import java.util.List;

public class TeamReportDO implements Serializable
{
  private static final long serialVersionUID = -1447146928403813249L;

  public TeamReportDO() {
    team = "undefined team";
    manager = "undefined manager";
  }

  public String team;
  public String manager;
  public String date;
  public List<String> issues;
  public List<String> dev;
  public List<String> people;
  public List<String> initiatives;
  public List<String> done;

  public static TeamReportDO findEntity(Long id) {
    return new TeamReportDO();
  }

  public Integer getVersion() {
    return 0;
  }

  public String getTeam() {
    return team;
  }

  public String getManager() {
    return manager;
  }
  
  public String getDate() {
    return date;
  }

  public List<String> getIssues() {
    return issues;
  }

  public List<String> getDev() {
    return dev;
  }

  public List<String> getInitiatives() {
    return initiatives;
  }

  public List<String> getDone() {
    return done;
  }

}
