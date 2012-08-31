/*
 * $HeadURL$
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

package org.topazproject.ambra.queue;

import org.topazproject.ambra.ApplicationException;

/**
 * Service for sending syndications to queuing system
 * @author Dragisa Krsmanovic
 */
public interface MessageService {

  /**
   * Send syndication message to the queue. 
   * @param target Syndication system
   * @param articleId Article DOI
   * @param archive Article archive name
   * @throws org.topazproject.ambra.ApplicationException if sending syndication message to queue fails
   */
  public void sendSyndicationMessage(String target, String articleId, String archive)
      throws ApplicationException;
}