/*
 * $HeadURL$
 * $Id$
 * Copyright (c) 2006-2012 by Public Library of Science http://plos.org http://ambraproject.org
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ambraproject.models;

/**
 * POJO Object to contain all the information about journal alerts.
 * It is mapped with table journalAlert.
 */
public class JournalAlert extends AmbraEntity {

  private String alertKey;
  private String emailSubject;
  private JournalAlertOrderCode emailArticleOrder;

  public JournalAlert() {
    super();
  }

  public JournalAlert(String alertKey, String emailSubject, JournalAlertOrderCode emailArticleOrder) {
    this();
    this.alertKey = alertKey;
    this.emailSubject = emailSubject;
    this.emailArticleOrder = emailArticleOrder;
  }
  
  /**
   * Get the alert key
   * @return the alert key
   */
  public String getAlertKey() {
    return alertKey;
  }

  /**
   * Set the alert key
   * @param alertKey the name of the alert
   */
  public void setAlertKey(String alertKey) {
    this.alertKey = alertKey;
  }

  /**
   * Get the subject of the email
   *
   * @return the subject of the email to send
   */
  public String getEmailSubject() {
    return emailSubject;
  }

  /**
   * Set the alert email subject
   * @param emailSubject
   */
  public void setEmailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
  }

  /**
   * Get the order of articles to use in this alert
   *
   * @return the order of articles to use in this alert
   */
  public JournalAlertOrderCode getEmailArticleOrder() {
    return emailArticleOrder;
  }

  /**
   * Set the order of articles to use in this alert
   *
   * @param emailArticleOrder the order of articles to use in this alert
   */
  public void setEmailArticleOrder(JournalAlertOrderCode emailArticleOrder) {
    this.emailArticleOrder = emailArticleOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JournalAlert that = (JournalAlert) o;

    if (!alertKey.equals(that.alertKey)) return false;
    if (emailArticleOrder != that.emailArticleOrder) return false;
    if (!emailSubject.equals(that.emailSubject)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = alertKey.hashCode();
    result = 31 * result + emailSubject.hashCode();
    result = 31 * result + emailArticleOrder.hashCode();
    return result;
  }
}
