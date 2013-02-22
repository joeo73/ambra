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

import org.ambraproject.ApplicationException;
import org.ambraproject.ambra.email.TemplateMailer;
import org.ambraproject.service.journal.JournalService;
import org.ambraproject.service.search.SearchParameters;
import org.ambraproject.service.search.SolrSearchService;
import org.ambraproject.views.JournalAlertView;
import org.ambraproject.views.SearchResultSinglePage;
import javax.mail.Multipart;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalSearchAlertsImpl implements JournalSearchAlerts {
  private static final Logger log = LoggerFactory.getLogger(JournalSearchAlertsImpl.class);

  SolrSearchService searchService;
  JournalService journalService;
  TemplateMailer mailer;

  /**
   * @inheritDoc
   */
  public List<JournalAlertView> getJournalAlerts(String alertType) {
    List<JournalAlertView> alertViews = new ArrayList<JournalAlertView>();

    for(JournalAlertView alertView : journalService.getJournalAlerts()) {
      if(alertView.getAlertName().contains(alertType)) {
        alertViews.add(alertView);
      }
    }

    return alertViews;
  }

  /**
   * @inheritDoc
   */
  public void sendJournalAlert(JournalAlertView alert) {
    log.info("Received thread Name: {}", Thread.currentThread().getName());
    log.info("Starting send request for: {}", alert.getAlertName());

    final Map<String, Object> context = new HashMap<String, Object>();

    Date startTime;
    Date endTime = Calendar.getInstance().getTime();

    if(alert.getAlertName().contains("weekly")) {
      //7 days into the past
      Calendar date = Calendar.getInstance();
      date.add(Calendar.DAY_OF_MONTH, -7);
      startTime = date.getTime();
    } else {
      //30 days into the past
      Calendar date = Calendar.getInstance();
      date.add(Calendar.DAY_OF_MONTH, -30);
      startTime = date.getTime();
    }

    SearchResultSinglePage results = doSearch(alert.getJournalKey(), startTime, endTime);

    log.debug("Matched {} Documents", results.getHits().size());

    //TODO: Different orderings of results
    context.put("searchHitList", results.getHits());
    context.put("startTime", startTime);
    context.put("endTime", endTime);

    //TODO: Move to config
    context.put("imagePath", "/bleh.gif");

    //Create message

    Multipart content = createContent(context);

    List<String> emails = journalService.getJournalAlertSubscribers(alert.getAlertID());

    //TODO: provide override for dev mode and allow QA to adjust in ambra.xml
    String toAddresses = StringUtils.join(emails, " ");

    //TODO: move to config?
    String fromAddress = "admin@plos.org";

    //TODO: This needs to be computed
    String subject = "Subject";

    mailer.mail(toAddresses, fromAddress, subject, context, content);

    log.info("Completed thread Name: {}", Thread.currentThread().getName());
    log.info("Completed send request for: {}", alert.getAlertName());
  }

  private Multipart createContent(Map<String, Object> context) {
    try {
      //TODO: Move filenames to configuration
      return mailer.createContent("etoc-text.ftl", "etoc-html.ftl", context);
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    } catch(MessagingException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Run SOLR search looking for new articles published in the defined window for the specific journal
   * This limits to a max of 1000 articles
   *
   * @param journalKey the key of the journal
   * @param startTime the start time
   * @param endTime the end time
   * @return
   */
  private SearchResultSinglePage doSearch(String journalKey, Date startTime, Date endTime) {
    try {
      SearchParameters sParams = new SearchParameters();

      sParams.setFilterStartDate(startTime);
      sParams.setFilterEndDate(endTime);
      sParams.setFilterJournals(new String[]{journalKey});

      //TODO: Limit to one page?  How many is too much?
      sParams.setPageSize(500);
      sParams.setStartPage(0);

      //Return everything, only search with filters
      sParams.setUnformattedQuery("*:*");

      return this.searchService.advancedSearch(sParams);
    } catch (ApplicationException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Required
  public void setSearchService(SolrSearchService searchService) {
    this.searchService = searchService;
  }

  @Required
  public void setJournalService(JournalService journalService) {
    this.journalService = journalService;
  }

  @Required
  public void setMailer(TemplateMailer mailer) {
    this.mailer = mailer;
  }
}
