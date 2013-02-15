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

  private String alertName;

  public JournalAlert() {
    super();
  }

  public JournalAlert(String alertName) {
    this();
    this.alertName = alertName;
  }
  
  /**
   * getter for alert name
   * @return the alert name
   */
  public String getAlertName() {
    return alertName;
  }

  /**
   * setter for alert name
   * @param alertName the name of the alert
   */
  public void setAlertName(String alertName) {
    this.alertName = alertName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JournalAlert)) return false;

    JournalAlert that = (JournalAlert) o;

    if (getID() != null ? !getID().equals(that.getID()) : that.getID() != null) return false;
    if (alertName != null ? !alertName.equals(that.alertName) : that.alertName != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int result = getID() != null ? getID().hashCode() : 0;
    result = 31 * result + (alertName != null ? alertName.hashCode() : 0);
    return result;
  }
}
