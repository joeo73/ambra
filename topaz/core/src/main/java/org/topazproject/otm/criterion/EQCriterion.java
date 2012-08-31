/* $HeadURL::                                                                            $
 * $Id$
 *
 * Copyright (c) 2007-2008 by Topaz, Inc.
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
package org.topazproject.otm.criterion;

import org.topazproject.otm.ClassMetadata;
import org.topazproject.otm.Criteria;
import org.topazproject.otm.OtmException;
import org.topazproject.otm.annotations.Entity;

/**
 * A criterion for an "equals" operation on a field value.
 *
 * @author Pradeep Krishnan
 *
 * @see SubjectCriterion
 * @see PredicateCriterion
 */
@Entity(types = {Criterion.RDF_TYPE + "/eq"})
public class EQCriterion extends AbstractBinaryCriterion {
  /**
   * Creates a new EQCriterion object.
   */
  public EQCriterion() {
  }

  /**
   * Creates a new EQCriterion object.
   *
   * @param name field/predicate name
   * @param value field/predicate value
   */
  public EQCriterion(String name, Object value) {
    super(name, value);
  }

  /*
   * inherited javadoc
   */
  public String toQuery(Criteria criteria, String subjectVar, String varPrefix, QL ql)
                throws OtmException {
    Criterion     impl;
    ClassMetadata cm = criteria.getClassMetadata();

    if (cm.getIdField().getName().equals(getFieldName()))
      impl = new SubjectCriterion(getValue().toString());
    else
      impl = new PredicateCriterion(getFieldName(), getValue());

    return impl.toQuery(criteria, subjectVar, varPrefix, ql);
  }
}