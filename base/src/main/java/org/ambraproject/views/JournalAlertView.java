package org.ambraproject.views;

import org.ambraproject.models.JournalAlert;

/**
 * Immutable view wrapper around a JournalAlert
 *
 * @author Joe Osowski
 */
public class JournalAlertView {
  private final String journalKey;
  private final String alertName;
  private final Long alertID;

  public JournalAlertView(String journalKey, JournalAlert journalAlert) {
    this.journalKey = journalKey;
    this.alertName = journalAlert.getAlertName();
    this.alertID = journalAlert.getID();
  }

  public String getJournalKey() {
    return journalKey;
  }

  public String getAlertName() {
    return alertName;
  }

  public Long getAlertID() {
    return alertID;
  }
}