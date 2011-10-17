package com.lior.teamreport.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TeamReportServiceAsync
{
  void findAllTeamReports(AsyncCallback<List<TeamReportDO>> callback);
}
