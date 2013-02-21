/*
 * Copyright (c) 2006-2011 by Public Library of Science http://plos.org http://ambraproject.org
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambraproject.search;

import org.ambraproject.views.JournalAlertView;

import java.util.List;

/**
 * Utilities for getting and sending journal search alerts
 *
 */
public interface JournalSearchAlerts {
  /**
   * Return a list of journal alerts filtered by type
   *
   * @param type 'weekly' or 'monthly'

   * @return a list of journal alerts
   */
  public List<JournalAlertView> getJournalAlerts(String type);

  /**
   * Execute the search for the journal alert and email all of the users
   * associated with the alert the search results
   *
   * @param alert the alert to process
   */
  public void sendJournalAlert(JournalAlertView alert);
}
