package com.lior.teamreport.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lior.teamreport.client.TeamReportDO;
import com.lior.teamreport.client.TeamReportService;

@Service("teamReportService")
public class TeamReportServiceImpl implements TeamReportService
{
  @Autowired
  private ArrayList<TeamData> teams;

  TeamReportServiceImpl() {
  }

  public static TeamReportServiceImpl findEntity(Long id) {
    return new TeamReportServiceImpl();
  }

  public List<TeamReportDO> findAllTeamReports() {
    return TeamReportLoader.load(teams);
  }
}
