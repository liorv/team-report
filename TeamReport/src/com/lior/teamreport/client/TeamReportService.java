package com.lior.teamreport.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side interface for the greeting service.
 */
@RemoteServiceRelativePath("springGwtServices/teamReportService")
public interface TeamReportService extends RemoteService
{
  List<TeamReportDO> findAllTeamReports();
}
