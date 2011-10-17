package com.lior.teamreport.server;

public class TeamData
{
  public TeamData() {}

  public TeamData(String teamId, String mgr, String reportUrl) {
    team = teamId;
    manager = mgr;
    url = reportUrl;
  }

  public String team;
  public String manager;
  public String url;
  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public String getManager() {
    return manager;
  }

  public void setManager(String manager) {
    this.manager = manager;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
