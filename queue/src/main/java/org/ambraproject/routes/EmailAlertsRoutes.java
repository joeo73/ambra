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

package org.ambraproject.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Email alert routes
 */
public class EmailAlertsRoutes extends SpringRouteBuilder {

  private static final Logger log = LoggerFactory.getLogger(EmailAlertsRoutes.class);
  private String weeklyCron;
  private String monthlyCron;

  @Override
  public void configure() throws Exception {
    //Weekly alert emails
    log.info("Setting route for sending 'Weekly' email alerts");

    from("quartz:ambra/searchalert/weeklyemail?cron=" + weeklyCron)
      .setBody(simple("weekly"))
      .to("direct:getsearches");

    //Monthly alert emails
    log.info("Setting route for sending 'Monthly' email alerts");

    from("quartz:ambra/searchalert/monthlyemail?cron=" + monthlyCron)
      .setBody(simple("monthly"))
      .to("direct:getsearches");

    from("direct:getsearches")
      .split().method("journalSearchAlerts", "getSearchAlerts")
      .to("direct:sendmails");

    //Send results in parallel
    from("direct:sendmails")
      .to("seda:searchInParallel");

    from("seda:searchInParallel?concurrentConsumers=25")
      .to("bean:journalSearchAlerts?method=sendSearchAlert");
  }

  @Required
  public void setWeeklyCron(String weeklyCron) {
    this.weeklyCron = weeklyCron;
  }

  @Required
  public void setMonthlyCron(String monthlyCron) {
    this.monthlyCron = monthlyCron;
  }
}
