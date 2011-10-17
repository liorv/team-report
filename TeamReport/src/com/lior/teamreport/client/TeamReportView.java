package com.lior.teamreport.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Image;
import com.lior.teamreport.shared.CustomFlowPanel;

public class TeamReportView implements EntryPoint
{
  /**
   * Create a remote service to talk to the server-side service.
   */
  private final TeamReportServiceAsync teamReportService = GWT
      .create(TeamReportService.class);
  
  

  static FlowPanel contentPanel = new CustomFlowPanel.Vertical();
  static FlowPanel cbPanel = new CustomFlowPanel.Vertical();
  
  static CheckBox cbShowIssues = new CheckBox("Show Issues");
  static CheckBox cbShowCurrentDevelopment = new CheckBox(
      "Show Current Development");
  static CheckBox cbShowLeadInitiatives = new CheckBox("Lead Initiatives");
  static CheckBox cbShowCompleted = new CheckBox("Completed");

  static List<TeamReportDO> teamReports;

  private static Panel tabbedPanel(int numTabs, Widget o) {
    HorizontalPanel retval = new HorizontalPanel();
    Label l = new Label(" ");
    l.setPixelSize(40 * numTabs, 0);

    retval.add(l);
    retval.add(o);
    return retval;
  }

  private static VerticalPanel drawListCategory(CheckBox cb, List<String> values)
  {
    if (!cb.getValue()) return null;

    VerticalPanel vpanel = new VerticalPanel();
    Label catgLabel = new Label(cb.getText());
    catgLabel.setStyleName("categoryTitle");
    vpanel.add(tabbedPanel(1, catgLabel));
    vpanel.add(vGap(5));
    if (values == null)
      vpanel.add(tabbedPanel(2, new Label("None")));
    else {
      for (String value : values)
        vpanel.add(tabbedPanel(2, new Label(value)));
    }
    vpanel.add(vGap(5));
    Set<VerticalPanel> vpanelSet = catg2panelSet.get(cb);
    if (vpanelSet == null) {
      vpanelSet = new HashSet<VerticalPanel>();
      catg2panelSet.put(cb, vpanelSet);
    }
    vpanelSet.add(vpanel);
    return vpanel;
  }

  private static Widget vGap(int gap) {
    Label l = new Label(" ");
    l.setPixelSize(10, gap);
    return l;
  }

  private static Map<CheckBox, Set<VerticalPanel>> catg2panelSet =
      new HashMap<CheckBox, Set<VerticalPanel>>();

  private static FlexTable titlePanel = new FlexTable();

  private static void refresh(boolean redraw, CheckBox cb) {
    if (!redraw) {
      Set<VerticalPanel> vpanelSet = catg2panelSet.get(cb);
      for (VerticalPanel vpanel : vpanelSet)
        vpanel.setVisible(cb.getValue());
    }
    else {
      contentPanel.clear();
      contentPanel.add(titlePanel);
      contentPanel.add(cbPanel);
      contentPanel.add(vGap(20));

      for (TeamReportDO tr : teamReports) {
        Label teamLabel = new Label("Team " + tr.getTeam());
        Label teamLabel2 =
            new Label("manager=" + tr.getManager() + " (updated: "
                + tr.getDate() + ")");
        teamLabel.setStyleName("teamTitle");
        teamLabel2.setStyleName("updatedOn");
        contentPanel.add(teamLabel);
        contentPanel.add(teamLabel2);
        contentPanel.add(vGap(20));
        contentPanel.add(drawListCategory(cbShowIssues, tr.getIssues()));
        contentPanel
            .add(drawListCategory(cbShowCurrentDevelopment, tr.getDev()));
        contentPanel.add(drawListCategory(cbShowLeadInitiatives,
            tr.getInitiatives()));
        contentPanel.add(drawListCategory(cbShowCompleted, tr.getDone()));
      }// end for
    } // end if
  }

  private static class MyClickHandler implements ClickHandler
  {
    private CheckBox cb;

    public MyClickHandler(CheckBox cb) {
      this.cb = cb;
    }

    @Override
    public void onClick(ClickEvent event) {
      refresh(false, cb);
    }
  }

  private void registerHandler(CheckBox cb) {
    cb.addClickHandler(new MyClickHandler(cb));
  }

  public void onModuleLoad() {
    registerHandler(cbShowCurrentDevelopment);
    registerHandler(cbShowIssues);
    registerHandler(cbShowLeadInitiatives);
    registerHandler(cbShowCompleted);

    RootPanel rootPanel = RootPanel.get();
    if (Document.get() != null) {
      Document.get().setTitle("Team Reports 0.1");
    }

    FlowPanel rootFlowPanel = new CustomFlowPanel.Vertical();
    rootPanel.add(rootFlowPanel, 0, 0);
    rootFlowPanel.setSize("100%", "100%");

    // NORTH
    Image image = new Image("images/AlgoCore.png");
    image.setSize("50px", "50px");

    Label lblNewLabel = new Label("Algo Core Team Reports");
    lblNewLabel.setStyleName("headingTitle");
    lblNewLabel.setWordWrap(false);
    titlePanel.setStyleName((String) null);
    titlePanel.setCellSpacing(20);

    titlePanel.setWidget(0, 0, image);
    titlePanel.setWidget(0, 1, lblNewLabel);

    // LEFT CHECKBOXES

    cbShowIssues.setValue(true);
    cbShowIssues.setText("Issues");
    cbShowCurrentDevelopment.setValue(true);
    cbShowCurrentDevelopment.setText("Current Development");
    cbShowLeadInitiatives.setValue(true);
    cbShowCompleted.setValue(true);

    cbPanel.add(cbShowIssues);
    cbPanel.add(cbShowCurrentDevelopment);
    cbPanel.add(cbShowLeadInitiatives);
    cbPanel.add(cbShowCompleted);

    // CENTER -- DOWNLOADED DATA
    ScrollPanel scrollPanel = new ScrollPanel();
    scrollPanel.add(contentPanel);
    rootFlowPanel.add(scrollPanel);
    contentPanel.setWidth("100%");

    AsyncCallback<List<TeamReportDO>> teamReportsHandler =
        new AsyncCallback<List<TeamReportDO>>() {

          @Override
          public void onFailure(Throwable caught) {}

          @Override
          public void onSuccess(List<TeamReportDO> result) {
            teamReports = result;
            refresh(true, null);
          }
        };

    teamReportService.findAllTeamReports(teamReportsHandler);
  }
}
