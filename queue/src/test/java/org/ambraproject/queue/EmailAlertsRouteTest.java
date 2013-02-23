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
import org.ambraproject.models.JournalAlertOrderCode;
import org.ambraproject.models.UserProfile;
import org.ambraproject.testutils.EmbeddedSolrServerFactory;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.jvnet.mock_javamail.Mailbox;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
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
  private static final String DOI_5 = "10.1371/journal.pbio.0002226";
  private static final String DOI_6 = "10.1371/journal.pbio.0002245";
  private static final String DOI_7 = "10.1371/journal.pbio.0002238";
  private static final String JOURNAL_KEY_1 = "PLOSONE";
  private static final String JOURNAL_KEY_2 = "PLOSMedicine";
  private static final String JOURNAL_KEY_3 = "PLOSBiology";
  private static final String CATEGORY_1 = "Category1";
  private static final String CATEGORY_2 = "Category2";

  private List<String> addresses = new ArrayList<String>();
  //Yeah, this is a bit weird.  We need a list of messages
  //To look for in the results.  Each message should contain a list of DOIs
  //In a certain order
  private List<Map<String,List<String>>> messageDOIs = new ArrayList<Map<String,List<String>>>();

  @BeforeMethod
  public void seedJournalSearch() {
    Calendar searchTime = Calendar.getInstance();
    searchTime.set(Calendar.YEAR, 2007);
    searchTime.set(Calendar.MONTH, 5);
    searchTime.set(Calendar.DAY_OF_MONTH, 15);

    Journal journal = new Journal(JOURNAL_KEY_1);
    journal.setAlerts(new ArrayList<JournalAlert>() {{
      add(new JournalAlert("alert1_weekly", JOURNAL_KEY_1 + "_weekly_subject", JournalAlertOrderCode.ARTICLE_TYPE));
      add(new JournalAlert("alert1_monthly", JOURNAL_KEY_1 + "_monthly_subject", JournalAlertOrderCode.ARTICLE_TYPE));
    }});

    //Now load the journal to get assigned IDs
    Long id1 = Long.valueOf(dummyDataStore.store(journal));
    Journal stored_journal1 = dummyDataStore.get(Journal.class, id1);

    journal = new Journal(JOURNAL_KEY_2);
    journal.setAlerts(new ArrayList<JournalAlert>() {{
      add(new JournalAlert("alert2_monthly", JOURNAL_KEY_2 + "_monthly_subject", JournalAlertOrderCode.ARTICLE_TYPE));
    }});

    //Now load the journal to get assigned IDs
    Long id2 = Long.valueOf(dummyDataStore.store(journal));
    Journal stored_journal2 = dummyDataStore.get(Journal.class, id2);

    //Test the ordering for the issue table of contents
    journal = new Journal(JOURNAL_KEY_3);
    journal.setAlerts(new ArrayList<JournalAlert>() {{
      add(new JournalAlert("alert3_monthly", JOURNAL_KEY_3 + "_monthly_subject", JournalAlertOrderCode.ISSUE_TOC));
    }});

    //Now load the journal to get assigned IDs
    Long id3 = Long.valueOf(dummyDataStore.store(journal));
    Journal stored_journal3 = dummyDataStore.get(Journal.class, id3);

    for (int i = 0; i < 5; i++) {
      UserProfile user = new UserProfile("savedSearch" + i + "@example.org", "user-" + i, "password-" + i);

      Set<JournalAlert> alerts = new HashSet<JournalAlert>();
      alerts.addAll(stored_journal1.getAlerts());
      alerts.addAll(stored_journal2.getAlerts());
      user.setJournalAlerts(alerts);

      dummyDataStore.store(user);

      //I re-use the emails in the unit test
      addresses.add(user.getEmail());

      //Yeah, this is a bit weird.  We need a list of messages
      //To look for in the results.  Each message should contain a list of DOIs
      //In a certain order
      messageDOIs.add(new HashMap<String, List<String>>() {{
        //Use keys that match subject emails as mailbox message ordering isn't deterministic
        put(JOURNAL_KEY_3 + "_monthly_subject",
          new ArrayList<String>() {{
            add(DOI_7);
            add(DOI_6);
            add(DOI_5);
          }}
        );
      }});
    }

    for (int i = 5; i < 10; i++) {
      UserProfile user = new UserProfile("savedSearch" + i + "@example.org", "user-" + i, "password-" + i);

      Set<JournalAlert> alerts = new HashSet<JournalAlert>();
      alerts.addAll(stored_journal1.getAlerts());
      user.setJournalAlerts(alerts);

      //I re-use the emails in the unit test
      addresses.add(user.getEmail());

      //Yeah, this is a bit weird.  We need a list of messages
      //To look for in the results.  Each message should contain a list of DOIs
      //In a certain order
      messageDOIs.add(new HashMap<String, List<String>>() {{
        //Use keys that match subject emails as mailbox message ordering isn't deterministic
        put(JOURNAL_KEY_1 + "_weekly_subject",
          new ArrayList<String>() {{
            add(DOI_1);
          }}
        );

        put(JOURNAL_KEY_1 + "_monthly_subject",
          new ArrayList<String>() {{
            add(DOI_3);
            add(DOI_1);
          }}
        );
      }});

      dummyDataStore.store(user);
    }

    for (int i = 10; i < 15; i++) {
      UserProfile user = new UserProfile("savedSearch" + i + "@example.org", "user-" + i, "password-" + i);

      Set<JournalAlert> alerts = new HashSet<JournalAlert>();
      alerts.addAll(stored_journal3.getAlerts());
      user.setJournalAlerts(alerts);

      //I re-use the emails in the unit test
      addresses.add(user.getEmail());

      //Yeah, this is a bit weird.  We need a list of messages
      //To look for in the results.  Each message should contain a list of DOIs
      //In a certain order
      messageDOIs.add(new HashMap<String, List<String>>() {{
        //Use keys that match subject emails as mailbox message ordering isn't deterministic
        put(JOURNAL_KEY_3 + "_monthly_subject",
          new ArrayList<String>() {{
            add(DOI_5);
            add(DOI_6);
            add(DOI_7);
          }}
        );
      }});

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

    Map<String, String[]> document1 = new HashMap<String, String[]>();
    document1.put("id", new String[]{DOI_1});
    document1.put("title", new String[]{"The First Title, with Spleen testing"});
    document1.put("title_display", new String[]{"The First Title, with Spleen testing"});
    document1.put("author", new String[]{"alpha delta epsilon"});
    document1.put("body", new String[]{"Body of the first document: Yak and Spleen"});
    document1.put("article_type", new String[]{"Research Article"});
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

    Map<String, String[]> document2 = new HashMap<String, String[]>();
    document2.put("id", new String[]{DOI_2});
    document2.put("title", new String[]{"The Second Title, with Yak YEk"});
    document2.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document2.put("author", new String[]{"beta delta epsilon"});
    document2.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document2.put("article_type", new String[]{"Research Article"});
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

    Map<String, String[]> document3 = new HashMap<String, String[]>();
    document3.put("id", new String[]{DOI_3});
    document3.put("title", new String[]{"The Third Title, with Gecko savedsearch "});
    document3.put("title_display", new String[]{"The Second Title, with Yak Gecko"});
    document3.put("author", new String[]{"gamma delta"});
    document3.put("body", new String[]{"Contents of the second document: Gecko and Islets of Langerhans"});
    //Front matter articles should be ordered before research articles
    document3.put("article_type", new String[]{"Front Matter"});
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

    Map<String, String[]> document4 = new HashMap<String, String[]>();
    document4.put("id", new String[]{DOI_4});
    document4.put("title", new String[]{"The Second Title, with Yak YEk"});
    document4.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document4.put("author", new String[]{"beta delta epsilon"});
    document4.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document4.put("article_type", new String[]{"Research Article"});
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

    Map<String, String[]> document5 = new HashMap<String, String[]>();
    document5.put("id", new String[]{DOI_5});
    document5.put("title", new String[]{"The Second Title, with Yak YEk"});
    document5.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document5.put("author", new String[]{"beta delta epsilon"});
    document5.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document5.put("article_type", new String[]{"Research Article"});
    document5.put("everything", new String[]{
      "description second document yak islets Langerhans second title with yak testing everything debug"});
    document5.put("elocation_id", new String[]{"222"});
    document5.put("volume", new String[]{"2"});
    document5.put("doc_type", new String[]{"full"});
    document5.put("publication_date", new String[]{sdf.format(todayMinus5.getTime()) + "T00:00:00Z"});
    document5.put("cross_published_journal_key", new String[]{JOURNAL_KEY_3});
    document5.put("subject", new String[]{CATEGORY_2});
    document5.put("article_type_facet", new String[]{"Not an issue image"});
    document5.put("author", new String[]{"doc2 author"});
    document5.put("author_display", new String[]{"doc4 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document5.get("id"), document5.get("cross_published_journal_key"), document5.get("publication_date") });

    Map<String, String[]> document6 = new HashMap<String, String[]>();
    document6.put("id", new String[]{DOI_6});
    document6.put("title", new String[]{"The Second Title, with Yak YEk"});
    document6.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document6.put("author", new String[]{"beta delta epsilon"});
    document6.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document6.put("article_type", new String[]{"Research Article"});
    document6.put("everything", new String[]{
      "description second document yak islets Langerhans second title with yak testing everything debug"});
    document6.put("elocation_id", new String[]{"222"});
    document6.put("volume", new String[]{"2"});
    document6.put("doc_type", new String[]{"full"});
    document6.put("publication_date", new String[]{sdf.format(todayMinus28.getTime()) + "T00:00:00Z"});
    document6.put("cross_published_journal_key", new String[]{JOURNAL_KEY_3});
    document6.put("subject", new String[]{CATEGORY_2});
    document6.put("article_type_facet", new String[]{"Not an issue image"});
    document6.put("author", new String[]{"doc2 author"});
    document6.put("author_display", new String[]{"doc4 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document6.get("id"), document6.get("cross_published_journal_key"), document6.get("publication_date") });

    Map<String, String[]> document7 = new HashMap<String, String[]>();
    document7.put("id", new String[]{DOI_7});
    document7.put("title", new String[]{"The Second Title, with Yak YEk"});
    document7.put("title_display", new String[]{"The Second Title, with Yak YAK"});
    document7.put("author", new String[]{"beta delta epsilon"});
    document7.put("body", new String[]{"Description of the second document: Yak and Islets of Langerhans testing"});
    document7.put("article_type", new String[]{"Research Article"});
    document7.put("everything", new String[]{
      "description second document yak islets Langerhans second title with yak testing everything debug"});
    document7.put("elocation_id", new String[]{"222"});
    document7.put("volume", new String[]{"2"});
    document7.put("doc_type", new String[]{"full"});
    document7.put("publication_date", new String[]{sdf.format(todayMinus28.getTime()) + "T00:00:00Z"});
    document7.put("cross_published_journal_key", new String[]{JOURNAL_KEY_3});
    document7.put("subject", new String[]{CATEGORY_2});
    document7.put("article_type_facet", new String[]{"Not an issue image"});
    document7.put("author", new String[]{"doc2 author"});
    document7.put("author_display", new String[]{"doc4 creator"});

    log.debug("Created Doc ID: {}, Journal: {}, pubdate: {}", new Object[] {
      document7.get("id"), document7.get("cross_published_journal_key"), document7.get("publication_date") });


    solrJournalAlerts.addDocument(document1);
    solrJournalAlerts.addDocument(document2);
    solrJournalAlerts.addDocument(document3);
    solrJournalAlerts.addDocument(document4);
    solrJournalAlerts.addDocument(document5);
    solrJournalAlerts.addDocument(document6);
    solrJournalAlerts.addDocument(document7);
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
    for(int a = 0; a < addresses.size(); a++) {
      String address = addresses.get(a);
      Map<String, List<String>> messages = messageDOIs.get(a);

      log.debug("Checking on email: {}", address);

      List<Message> inboxMessages = Mailbox.get(address);

      //Check count of messages
      assertEquals(inboxMessages.size(), messages.values().size(), "Message counts are off");

      for(Message message : inboxMessages) {
        MimeMultipart mail = (MimeMultipart)message.getContent();
        String subject = message.getSubject();

        List<String> dois = messages.remove(subject);

        //This confirms two things the subject is valid
        //If it wasn't it wouldn't be in the list's collection
        assertNotNull("Subject of " + subject + " not found", dois);

        //inspect the HTML version
        BodyPart bp = mail.getBodyPart(0);
        String body = (String)bp.getContent();
        int doisFound = 0;

        //This checks for DOI existence
        for(String doi : dois) {
          int foundAt = body.indexOf(doi);

          assertTrue(foundAt > 0, "DOI '" + doi + "' not found in message '" + subject + "', or not found in correct order");

          //For the next search, start where this one ended
          //To assert that order is correct
          body = body.substring(foundAt + doi.length());

          doisFound++;
        }

        assertEquals(dois.size(), doisFound, "Not all DOIs found in sent email");

        //TODO:inspect the text version?
      }
    }
  }
}

