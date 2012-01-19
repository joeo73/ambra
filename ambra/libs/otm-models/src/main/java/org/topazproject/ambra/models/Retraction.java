/* $HeadURL::$
 * $Id: Retraction.java 6541 2008-10-23 16:57:02Z ssterling $
 *
 * Copyright (c) 2007-2009 by Topaz, Inc.
 * http://topazproject.org
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
package org.topazproject.ambra.models;

import org.topazproject.otm.annotations.Entity;

/**
 * Represents a formal correction annotation.
 *
 * @author Scott Sterling ssterling@plos.org
 */
@Entity(types = {Retraction.RDF_TYPE})
public class Retraction extends Correction {
  private static final long serialVersionUID = 8454252522046414607L;
  public  static final String RDF_TYPE         = Annotea.TOPAZ_TYPE_NS + "Retraction";

  public String getType() {
    return RDF_TYPE;
  }

  @Override
  public String getWebType() {
    return WEB_TYPE_RETRACTION;
  }
}