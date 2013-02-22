package org.ambraproject.views;

import org.ambraproject.models.JournalAlert;
import org.ambraproject.models.JournalAlertOrderCode;

/**
 * Immutable view wrapper around a JournalAlert
 *
 * @author Joe Osowski
 */
public class JournalAlertView {
  private final String journalKey;
  private final String alertKey;
  private final String emailSubject;
  private final JournalAlertOrderCode emailArticleOrder;
  private final Long alertID;

  public JournalAlertView(String journalKey, JournalAlert journalAlert) {
    this.journalKey = journalKey;
    this.alertKey = journalAlert.getAlertKey();
    this.emailSubject = journalAlert.getEmailSubject();
    this.emailArticleOrder = journalAlert.getEmailArticleOrder();
    this.alertID = journalAlert.getID();
  }

  public String getJournalKey() {
    return journalKey;
  }

  public String getAlertKey() {
    return alertKey;
  }

  public String getEmailSubject() {
    return emailSubject;
  }

  public JournalAlertOrderCode getEmailArticleOrder() {
    return emailArticleOrder;
  }

  public Long getAlertID() {
    return alertID;
  }
}