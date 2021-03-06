/* $HeadURL$
 * $Id$
 *
 * Copyright (c) 2006-2010 by Public Library of Science
 * http://plos.org
 * http://ambraproject.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambraproject.action.article;

import org.ambraproject.ApplicationException;
import org.ambraproject.action.BaseActionSupport;
import org.ambraproject.models.Journal;
import org.ambraproject.service.article.BrowseService;
import org.ambraproject.service.journal.JournalService;
import org.ambraproject.service.xml.XMLService;
import org.ambraproject.views.IssueInfo;
import org.ambraproject.views.TOCArticleGroup;
import org.ambraproject.views.VolumeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * BrowseIssueAction retrieves data for presentation of an issue and a table of contents. Articles
 * contained in the issue are grouped into article types.
 *
 * @author Alex Worden
 * @author Joe Osowski
 *
 */
public class BrowseIssueAction extends BaseActionSupport{
  private static final Logger log  = LoggerFactory.getLogger(BrowseIssueAction.class);

  private String issue;
  private JournalService journalService;
  private BrowseService browseService;
  private IssueInfo issueInfo;
  private String issueFullDescription;
  private String issueDescription;
  private String issueTitle;
  private String issueImageCredit;
  private List<TOCArticleGroup> articleGroups;

  private XMLService secondaryObjectService;

  private VolumeInfo volumeInfo;

  @Override
  public String execute() {
    // Was Issue specified?  If not, then use Current Issue.
    // If no Current Issue, then use most recent Issue from the most recent Volume.

    //There are some pretty big inefficiencies here.  We load up complete article classes when
    //we only need doi/title/authors

    if (issue == null || issue.length() == 0) {
      Journal currentJournal = journalService.getJournal(getCurrentJournal());

      if (currentJournal != null) {
        if (currentJournal.getCurrentIssue() != null) {
          issue = currentJournal.getCurrentIssue().getIssueUri().trim();
          issueInfo = browseService.createIssueInfo(currentJournal.getCurrentIssue());
        } else {
          // Current Issue has not been set for this Journal,
          // so get the most recent issue from the most recent volume.
          String currentIssueUri = browseService.getLatestIssueFromLatestVolume(currentJournal);
          if (currentIssueUri != null) {
            issue = currentIssueUri;
            issueInfo = browseService.getIssueInfo(currentIssueUri); // Get data on this Issue.
          }
        }
      }
    } else {  //  An Issue was specified.
      issueInfo = browseService.getIssueInfo(issue); // Get data on this Issue.
    }

    //If no issue is found, return 404
    if (issue == null || issue.length() == 0) {
      return NONE;
    } else if (issueInfo == null) {
      log.error("Found issue, Failed to retrieve IssueInfo for issue id='" + issue + "'");
      return ERROR;
    }

    //  Issue should always have a parent Volume.
    volumeInfo = browseService.getVolumeInfo(issueInfo.getParentVolume(), this.getCurrentJournal());

    if (issueInfo.getDescription() != null) {
      issueFullDescription = issueInfo.getDescription();
      issueTitle = issueInfo.getIssueTitle();
      issueImageCredit = issueInfo.getIssueImageCredit();
      issueDescription = issueInfo.getIssueDescription();

      // only transform the description (title and image credit do not need to be transformed)
      try {
        issueDescription = secondaryObjectService.getTransformedDescription(issueDescription);
        issueImageCredit = secondaryObjectService.getTransformedDescription(issueImageCredit);
      } catch (ApplicationException e) {
        log.error("Failed to translate issue description to HTML.", e);
      }

    } else {
      log.error("The currentIssue description was null. Issue DOI='" + issueInfo.getIssueURI() + "'");
      issueFullDescription = "No description found for this issue";
    }

    articleGroups = browseService.getArticleGrpList(issueInfo, getAuthId());

    return SUCCESS;
  }

  /**
   * Used by the view to retrieve the IssueInfo from the struts value stack.
   * @return the IssueInfo.
   */
  public IssueInfo getIssueInfo() {
    return issueInfo;
  }

  /**
   * Used by the view to retrieve an ordered list of TOCArticleGroup objects. Each TOCArticleGroup
   * represents a collection of article types that are defined in defaults.xml.  The groups are
   * listed by the view in the order returned here with links to the articles in that group
   * category.
   *
   * @return ordered list of TOCArticleGroup(s)
   */
  public List<TOCArticleGroup> getArticleGroups() {
    return articleGroups;
  }

  /**
   * If the request parameter 'issue' is specified, stuts will call this method. The action will
   * return a BrowseIssue page for this specific issue doi.
   * @param issue The issue for ToC view.
   */
  public void setIssue(String issue) {
    this.issue = issue;
  }

  /**
   * Spring injected method sets the JournalService.
   *
   * @param journalService The JournalService to set.
   */
  @Required
  public void setJournalService(JournalService journalService) {
    this.journalService = journalService;
  }

  /**
   * Spring injected
   *
   * @param secondaryObjectService ArticleXMLUtils
   */
  @Required
  public void setSecondaryObjectService(XMLService secondaryObjectService) {
    this.secondaryObjectService = secondaryObjectService;
  }

  /**
   * Spring injected method sets the browseService.
   * @param browseService The browseService to set.
   */
  @Required
  public void setBrowseService(BrowseService browseService) {
    this.browseService = browseService;
  }

  /**
   * returns the VolumeInfo for the current issue's parent volume
   * @return VolumeInfo
   */
  public VolumeInfo getVolumeInfo() {
    return volumeInfo;
  }

  public String getIssueImageCredit() {
    return issueImageCredit;
  }

  public String getIssueTitle() {
    return issueTitle;
  }

  public String getIssueDescription() {
    return issueDescription;
  }

  public String getIssueFullDescription() {
    return issueFullDescription;
  }
}
