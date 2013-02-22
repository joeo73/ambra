/*
 * Copyright (c) 2006-2012 by Public Library of Science http://plos.org http://ambraproject.org
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambraproject.queue;

import org.ambraproject.action.BaseTest;
import org.ambraproject.models.Journal;
import org.ambraproject.models.JournalAlert;
import org.ambraproject.models.UserProfile;
import org.ambraproject.testutils.EmbeddedSolrServerFactory;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.jvnet.mock_javamail.Mailbox;
import static org.testng.Assert.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sun.net.idn.StringPrep;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ContextConfiguration
public class EmailAlertsRouteTest extends BaseTest {
  private static final Logger log = LoggerFactory.getLogger(EmailAlertsRouteTest.class);

  @Autowired
  protected EmbeddedSolrServerFactory solrJournalAlerts;

  private static final String DOI_1 = "10.1371/journal.pone.1002222";
  private static final String DOI_2 = "10.1371/journal.pmed.1002223";
  private static final String DOI_3 = "10.1371/journal.pone.1002224";
  private static final String DOI_4 = "10.1371/journal.pmed.1002225";
  private static final String JOURNAL_KEY_1 = "PLOSONE";
  private static final String JOURNAL_KEY_2 = "PLOSMedicine";
  private static final String CATEGORY_1 = "Category1";
  private static final String CATEGORY_2 = "Category2";

  private List<String> addresses = new ArrayList<String>();

  @BeforeMethod
  public void seedJournalSearch() {
    Calendar searchTime = Calendar.getInstance();
    searchTime.set(Calendar.YEAR, 2007);
    searchTime.set(Calendar.MONTH, 5);
    searchTime.set(Calendar.DAY_OF_MONTH, 15);

    Journal journal = new Journal(JOURNAL_KEY_1);
    journal.setAlerts(new ArrayList<JournalAlert>() {{
      add(new JournalAlert("alert1_weekly"));
      add(new JournalAlert("alert1_monthly"));
    }});

    //Now load the journal to get assigned IDs
    Long id1 = Long.valueOf(dummyDataStore.store(journal));
    Journal stored_journal1 = dummyDataStore.get(Journal.class, id1);

    journal = new Journal(JOURNAL_KEY_2);
    journal.setAlerts(new ArrayList<JournalAlert>() {{
      add(new JournalAlert("alert2_monthly"));
    }});

    //Now load the journal to get assigned IDs
    Long id2 = Long.valueOf(dummyDataStore.store(journal));
    Journal stored_journal2 = dummyDataStore.get(Journal.class, id2);

    for (int i = 1; i <= 100; i++) {
      UserProfile user = new UserProfile("savedSearch" + i + "@example.org", "user-" + i, "password-" + i);

      Set<JournalAlert> alerts = new HashSet<JournalAlert>();
      alerts.addAll(stored_journal1.getAlerts());
      alerts.addAll(stored_journal2.getAlerts());
      user.setJournalAlerts(alerts);

      //I re-use the emails in the unit test
      addresses.add(user.getEmail());

      dummyDataStore.store(user);
    }
  }

  @BeforeMethod
  public void seedSolrData() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Calendar todayMinus5 = Calendar.getInstance();
    todayMinus5.add(Calendar.DAY_OF_MONTH, -5);

    Calendar todayMinus28 = Calendar.getInstance();
    todayMinus28.add(Calendar.DAY_OF_MONTH, -28);

    Calendar todayMinus35 = Calendar.getInstance();
    todayMinus35.add(Calendar.DAY_OF_MONTH, -35);

    // 2 occurrences in "everything": "Spleen"
    Map<String, String[]> document1 = new HashMap<String, String[]>();
    document1.put("id", new String[]{DOI_1});
    document1.put("title", new String[]{"The First Title, with Spleen testing"});
    document1.put("title_display", new String[]{"The First Title, with Spleen testing"});
    document1.put("author", new String[]{"alpha delta epsilon"});
    document1.put("body", new String[]{"Body of the first document: Yak and Spleen"});
    document1.put("everything", new String[]{
      "body first document yak spleen first title with spleen"});
    document1.put("elocation_id", new String[]{"111"});
    document1.put("volume", new String[]{"1"});
    document1.put("doc_type", new String[]{"full"});
    document1.put("publication_date", new String[]{sdf.format(todayMinus5.getTime()) + "T00:00:00Z"});
    document1.put("cross_published_journal_key", new String[]{JOURNAL_KEY_1});
    document1.put("subject", new String[]{CATEGORY_1, CATEGORY_2});
    document1.put("article_type_facet", new String[]{"Not an issue image"});
    document1.put("author", new String[]{"doc1 author"});
    document1.put("author_display", new String[]{"doc1 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document1.get("id"), document1.get("cross_published_journal_key"), document1.get("publication_date") });

    // 2 occurrences in "everything": "Yak"
    Map<String, String[]> document2 = new HashMap<String, String[]>();
    document2.put("id", new String[]{DOI_2});
    document2.put("title", new String[]{"The Second Title, with Yak YEk"});
    document2.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document2.put("author", new String[]{"beta delta epsilon"});
    document2.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document2.put("everything", new String[]{
      "description second document yak islets Langerhans second title with yak testing"});
    document2.put("elocation_id", new String[]{"222"});
    document2.put("volume", new String[]{"2"});
    document2.put("doc_type", new String[]{"full"});
    document2.put("publication_date", new String[]{sdf.format(todayMinus28.getTime()) + "T00:00:00Z"});
    document2.put("cross_published_journal_key", new String[]{JOURNAL_KEY_2});
    document2.put("subject", new String[]{CATEGORY_2});
    document2.put("article_type_facet", new String[]{"Not an issue image"});
    document2.put("author", new String[]{"doc2 author"});
    document2.put("author_display", new String[]{"doc2 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document2.get("id"), document2.get("cross_published_journal_key"), document2.get("publication_date") });

    // 2 occurrences in "everything": "Gecko"
    Map<String, String[]> document3 = new HashMap<String, String[]>();
    document3.put("id", new String[]{DOI_3});
    document3.put("title", new String[]{"The Third Title, with Gecko savedsearch "});
    document3.put("title_display", new String[]{"The Second Title, with Yak Gecko"});
    document3.put("author", new String[]{"gamma delta"});
    document3.put("body", new String[]{"Contents of the second document: Gecko and Islets of Langerhans"});
    document3.put("everything", new String[]{
      "contents of the second document gecko islets langerhans third title with gecko savedsearch"});
    document3.put("elocation_id", new String[]{"333"});
    document3.put("volume", new String[]{"3"});
    document3.put("doc_type", new String[]{"full"});
    document3.put("publication_date", new String[]{sdf.format(todayMinus28.getTime()) + "T00:00:00Z"});
    document3.put("cross_published_journal_key", new String[]{JOURNAL_KEY_1});
    document3.put("subject", new String[]{CATEGORY_1});
    document3.put("article_type_facet", new String[]{"Not an issue image"});
    document3.put("author", new String[]{"doc3 author"});
    document3.put("author_display", new String[]{"doc3 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document3.get("id"), document3.get("cross_published_journal_key"), document3.get("publication_date") });

    // 2 occurrences in "everything": "Yak"
    Map<String, String[]> document4 = new HashMap<String, String[]>();
    document4.put("id", new String[]{DOI_4});
    document4.put("title", new String[]{"The Second Title, with Yak YEk"});
    document4.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document4.put("author", new String[]{"beta delta epsilon"});
    document4.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document4.put("everything", new String[]{
      "description second document yak islets Langerhans second title with yak testing everything debug"});
    document4.put("elocation_id", new String[]{"222"});
    document4.put("volume", new String[]{"2"});
    document4.put("doc_type", new String[]{"full"});
    document4.put("publication_date", new String[]{sdf.format(todayMinus35.getTime()) + "T00:00:00Z"});
    document4.put("cross_published_journal_key", new String[]{JOURNAL_KEY_2});
    document4.put("subject", new String[]{CATEGORY_2});
    document4.put("article_type_facet", new String[]{"Not an issue image"});
    document4.put("author", new String[]{"doc2 author"});
    document4.put("author_display", new String[]{"doc4 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document4.get("id"), document4.get("cross_published_journal_key"), document4.get("publication_date") });

    solrJournalAlerts.addDocument(document1);
    solrJournalAlerts.addDocument(document2);
    solrJournalAlerts.addDocument(document3);
    solrJournalAlerts.addDocument(document4);
  }

  @Produce(uri = "direct:getsearches")
  protected ProducerTemplate start;

  @Test
  public void testWeeklyCron() throws InterruptedException, MessagingException, Exception {

    start.sendBody("monthly");
    start.sendBody("weekly");

    //Wait for it!
    //We send the queue a lot of requests, the only way to complete them in 5 seconds
    //would be in parallel, this validates that that part is working
    Thread.sleep(5000);

    //Check the mocked up inboxes for messages
    for(String address : addresses) {
      log.debug("Checking on email: {}", address);

      List<Message> inbox = Mailbox.get(address);

      //Check results!
      assertEquals(inbox.size(), 3);

      //TODO: check for correct size of inbox

      if(inbox.size() > 0) {
        Message message = inbox.get(0);

        MimeMultipart mail = (MimeMultipart)message.getContent();

        //inspect the HTML version
        BodyPart bp = mail.getBodyPart(0);
        String body = (String)bp.getContent();

        //TODO: Check body for DOIs for expected articles

        //TODO: Check for expected subject
        assertEquals("Expected Subject", message.getSubject());
      }
    }
  }
}

