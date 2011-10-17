package com.lior.teamreport.shared;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class CustomFlowPanel extends FlowPanel
{
  protected abstract String getFlowStyle();
  
  public static class Horizontal extends CustomFlowPanel
  {
    @Override
    protected String getFlowStyle() {
      return "inline";
    }
  }

  public static class Vertical extends CustomFlowPanel
  {
    @Override
    protected String getFlowStyle() {
      return "block";
    }
  }

  @Override
  public void add(Widget w) {
    w.getElement().getStyle().setProperty("display", getFlowStyle());
    super.add(w);
  }
}


