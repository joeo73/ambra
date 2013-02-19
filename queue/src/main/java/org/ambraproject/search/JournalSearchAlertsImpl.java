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

import org.ambraproject.service.journal.JournalService;
import org.ambraproject.service.search.SolrSearchService;
import org.ambraproject.views.JournalAlertView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JournalSearchAlertsImpl implements JournalSearchAlerts {
  private static final Logger log = LoggerFactory.getLogger(JournalSearchAlertsImpl.class);

  SolrSearchService searchService;
  JournalService journalService;

  /**
   * @inheritDoc
   */
  public List<JournalAlertView> getSearchAlerts(String type) {
    List<JournalAlertView> alertViews = new ArrayList<JournalAlertView>();

    for(JournalAlertView alertView : journalService.getJournalAlerts()) {
      if(alertView.getAlertName().contains(type)) {
        alertViews.add(alertView);
      }
    }

    return alertViews;
  }

  /**
   * @inheritDoc
   */
  public void sendSearchAlert(JournalAlertView alert) {
    log.info("Received send request for: {}", alert.getAlertName());
    log.info("Received thread ID: {}", Thread.currentThread().getId());
    log.info("Received thread Name: {}", Thread.currentThread().getName());

    try {
      Thread.sleep(5000);
    } catch(InterruptedException ex) {

    }

    log.info("Completed send request for: {}", alert.getAlertName());
  }

  public void setSearchService(SolrSearchService searchService) {
    this.searchService = searchService;
  }

  public void setJournalService(JournalService journalService) {
    this.journalService = journalService;
  }
}
