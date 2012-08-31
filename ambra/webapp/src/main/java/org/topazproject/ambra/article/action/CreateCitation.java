/* $HeadURL::                                                                            $
 * $Id$
 *
 * Copyright (c) 2007-2010 by Public Library of Science
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

package org.topazproject.ambra.article.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.topazproject.ambra.action.BaseActionSupport;
import org.topazproject.ambra.article.service.ArticlePersistenceService;
import org.topazproject.ambra.models.Article;
import org.topazproject.ambra.models.ArticleContributor;
import org.topazproject.ambra.models.Citation;
import org.topazproject.ambra.util.UriUtil;

import java.net.URI;
import java.util.List;

/**
 * Action to create a citation.  Does not care what the output format is.
 *
 * @author Stephen Cheng
 *
 */
@SuppressWarnings("serial")
public class CreateCitation extends BaseActionSupport {
  private static final Logger log = LoggerFactory.getLogger(CreateCitation.class);

  private ArticlePersistenceService articlePersistenceService;     // OTM service Spring injected.

  private String articleURI;
  private String title;
  private Citation citation;
  private List<ArticleContributor> authors;

  /**
   * Get Citation object from database
   */
  @Override
  @Transactional(readOnly = true)
  public String execute () throws Exception {

    UriUtil.validateUri(articleURI, "articleUri=<" + articleURI + ">");

    Article article = articlePersistenceService.getArticle(URI.create(articleURI), getAuthId());

    citation = article.getDublinCore().getBibliographicCitation();
    title = article.getDublinCore().getTitle();

    authors = article.getAuthors();
    
    return SUCCESS;
  }



  /**
   * @param articlePersistenceService ArticlePersistenceService Spring Injected
   */
  @Required
  public void setArticlePersistenceService(ArticlePersistenceService articlePersistenceService) {
    this.articlePersistenceService = articlePersistenceService;
  }

  /**
   * @return Returns the articleURI.
   */
  public String getArticleURI() {
    return articleURI;
  }

  /**
   * @param articleURI The articleURI to set.
   */
  public void setArticleURI(String articleURI) {
    this.articleURI = articleURI;
  }

  /**
   * @return Returns the citation.
   */
  public Citation getCitation() {
    return citation;
  }

  /**
   * @return Returns the article title
   */
  public String getTitle() {
    return title;
  }

  /**
   *
   * @return the authors of the article
   */
  public List<ArticleContributor> getAuthors() {
    return authors;
  }
}