<!-- begin : main content -->
<div id="content">
  <!-- begin : home page wrapper -->
  <div id="wrap">
    <div id="home">
      <!-- begin : layout wrapper -->
      <div class="col">
        <!-- begin : wrapper for cols 1 & 2 -->
        <div id="first" class="col">
        <!-- SWT removed col 1 -->
        <!-- begin : col 2 -->
          <div class="col last">
            <div class="horizontalTabs">
              <ul id="tabsContainer">
                <!-- Tabs generated by global file horizontalTabs.js - parameters are set in local file config_home.js -->
              </ul>
							<div id="tabPaneSet" class="contentwrap">
                  <#include "article/recentArticles.ftl">
              </div>
            </div><!-- end : horizontalTabs -->
            <!-- begin : calls to action blocks -->
            <div class="ctaWrap">
              <div id="cta1">
                <strong>Publish with PLoS</strong>
                <a href="${checklist}">We want to publish your work</a>
              </div>
              <div id="cta2">
                <strong>Have Your Say</strong>
                <a href="${comment}">Add ratings and discussions</a>
              </div>
              <div class="clearer">&nbsp;</div>
            </div>
            <!-- end : calls to action blocks -->
            <div class="block">
              <h2>Featured Collection</h2>
              <@s.url id="featured" namespace="/article" action="fetchArticle" articleURI="info:doi/10.1371/journal.pntd.0000065"/>
              <div class="article section lastSection">
                <h3><@s.a href="${featured}" title="Read Open-Access Article">Aging, Bench to Bedside: A Collection in <em>PLoS Genetics</em></@s.a></h3>
                <img src="images/imgPlaceholder_90px.gif" alt="article image" />
                <p>In his <a href="#">Perspective</a>, Thomas Nystrom addresses aging in bacteria, while Leonard Hayflick closes this new Collection with <a href="#">'Entropy Explains Aging, Genetic Determinism Explains Longevity, and Undefined Terminology Explains Misunderstanding Both'</a>.</p>
                <div class="clearer">&nbsp;</div>
              </div>
            </div>
            <!-- end block -->
            <div class="block">
              <h2>Featured Research</h2>
              <@s.url id="newNoted1" namespace="/article" action="fetchArticle" articleURI="info:doi/10.1371/journal.pntd.0000056"/>
              <@s.url id="newNoted2" namespace="/article" action="fetchArticle" articleURI="info:doi/10.1371/journal.pntd.0000001"/>
              <@s.url id="newNoted3" namespace="/article" action="fetchArticle" articleURI="info:doi/10.1371/journal.pntd.0000067"/>
              <div class="article section">
                <h3><@s.a href="${newNoted1}" title="Read Open-Access Article">Genome-Wide Analysis of Rad52 Foci Reveals Diverse Mechanisms Impacting Recombination</@s.a></h3>
                <img src="images/imgPlaceholder_90px.gif" alt="article image" />
                <p>This cell biological approach demonstrates the diversity of processes that converge on homologous recombination, protect against spontaneous DNA damage, and facilitate efficient repair.</p>
                <div class="clearer">&nbsp;</div>
              </div>
              <div class="article section">
                <h3><@s.a href="${newNoted2}" title="Read Open-Access Article">Most <em>Caenorhabditis elegans</em> microRNAs Are Individually Not Essential for Development or Viability</@s.a></h3>
                <img src="images/imgPlaceholder_90px.gif" alt="article image" />
                <p>This study identifies a new role for miRNAs in <em>Caenorhabditis elegans</em> and also demonstrates that most miRNAs are not essential for development.</p>
                <div class="clearer">&nbsp;</div>
              </div>
              <div class="article section lastSection">
                <h3><@s.a href="${newNoted3}" title="Read Open-Access Article">Sustained Post-Mating Response in Drosophila melanogaster Requires Multiple Seminal Fluid Proteins</@s.a></h3>
                <img src="images/imgPlaceholder_90px.gif" alt="article image" />
                <p>In addition to assigning functions to particular seminal proteins, these results suggest that fruit flies can serve as a model with which to dissect the functions of conserved protein classes in seminal fluid.</p>
                <div class="clearer">&nbsp;</div>
              </div>
            </div><!-- end : block -->
            <div class="other block">
              <h2>Other PLoS Content</h2>
              <div class="section">
                <h3>Browse articles in the Genetics and Genomics subject area</h3>
                <p><a href="#"><em>PLoS Biology</em></a>, <a href="#"><em>PLoS Medicine</em></a>, <a href="#"><em>PLoS Computational Biology</em></a>, <a href="#"><em>PLoS Pathogens</em></a>, <a href="#"><em>PLoS ONE</em></a>, and <a href="#"><em>PLoS Neglected Tropical Diseases</em></a></p>
              </div>
              <div class="section lastSection">
                <h3>Browse Article Collections</h3>
                <p><a href="#"><em>PLoS Biology</em></a>, <a href="#"><em>PLoS Medicine</em></a>, and <a href="#"><em>PLoS Computational Biology</em></a></p>
              </div>
            </div><!-- end : other block -->
          </div><!-- end : col last -->
        </div><!-- end : wrapper for cols 1 & 2 -->
        <!-- begin : wrapper for cols 3 & 4 -->
        <div id="second" class="col">
          <!-- begin : col 3 -->
          <div class="subcol first">
            <div id="issue" class="block"><h3><a href="${tocStatic}">October 2007 Issue</a></h3><a href="${tocStatic}"><img src="images/imgPlaceholder_251px.gif" alt="issue cover image" /></a></div><!-- keep div#issue hmtl all on one line to avoid extra space below issue image in IE -->
            <!-- end : issue block -->
            <!-- begin : mission block -->
            <div id="mission" class="block">
              <p><strong><em><a href="${info}">PLoS Genetics</a></em></strong> is a <a href="#">peer-reviewed</a>, open-access journal that reflects the full breadth and interdisciplinary nature of <a href="#">genetics</a> and <a href="#">genomics</a> research by publishing outstanding original contributions in all areas of biology. View a list of <a href="#">Google Scholar</a> citations and read about our first <a href="#">ISI impact factor</a> of 7.67.</p>
            </div>
            <!-- end : mission block -->
            <!-- begin : advocacy blocks -->
            <div id="adWrap">
            <script language='JavaScript' type='text/javascript' src='http://ads.plos.org/adx.js'></script>
            <script language='JavaScript' type='text/javascript'>
            <!--
               if (!document.phpAds_used) document.phpAds_used = ',';
               phpAds_random = new String (Math.random()); phpAds_random = phpAds_random.substring(2,11);
               document.write ("<" + "script language='JavaScript' type='text/javascript' src='");
               document.write ("http://ads.plos.org/adjs.php?n=" + phpAds_random);
               document.write ("&amp;what=zone:195&amp;source=NTD&amp;target=_top&amp;block=1");
               document.write ("&amp;exclude=" + document.phpAds_used);
               if (document.referrer)
                  document.write ("&amp;referer=" + escape(document.referrer));
               document.write ("'><" + "/script>");
            //-->
            </script><noscript><a href='http://ads.plos.org/adclick.php?n=a1ec113d' target='_top'><img src='http://ads.plos.org/adview.php?what=zone:195&amp;source=NTD&amp;n=a1ec113d' border='0' alt=''></a></noscript>
                        <script language='JavaScript' type='text/javascript' src='http://ads.plos.org/adx.js'></script>
            <script language='JavaScript' type='text/javascript'>
            <!--
               if (!document.phpAds_used) document.phpAds_used = ',';
               phpAds_random = new String (Math.random()); phpAds_random = phpAds_random.substring(2,11);
               document.write ("<" + "script language='JavaScript' type='text/javascript' src='");
               document.write ("http://ads.plos.org/adjs.php?n=" + phpAds_random);
               document.write ("&amp;what=zone:196&amp;source=NTD&amp;target=_top&amp;block=1");
               document.write ("&amp;exclude=" + document.phpAds_used);
               if (document.referrer)
                  document.write ("&amp;referer=" + escape(document.referrer));
               document.write ("'><" + "/script>");
            //-->
            </script><noscript><a href='http://ads.plos.org/adclick.php?n=ace5c997' target='_top'><img src='http://ads.plos.org/adview.php?what=zone:196&amp;source=NTD&amp;n=ace5c997' border='0' alt=''></a></noscript>
                        <script language='JavaScript' type='text/javascript' src='http://ads.plos.org/adx.js'></script>
            <script language='JavaScript' type='text/javascript'>
            <!--
               if (!document.phpAds_used) document.phpAds_used = ',';
               phpAds_random = new String (Math.random()); phpAds_random = phpAds_random.substring(2,11);
               document.write ("<" + "script language='JavaScript' type='text/javascript' src='");
               document.write ("http://ads.plos.org/adjs.php?n=" + phpAds_random);
               document.write ("&amp;what=zone:197&amp;source=NTD&amp;target=_top&amp;block=1");
               document.write ("&amp;exclude=" + document.phpAds_used);
               if (document.referrer)
                  document.write ("&amp;referer=" + escape(document.referrer));
               document.write ("'><" + "/script>");
            //-->
            </script><noscript><a href='http://ads.plos.org/adclick.php?n=aec547bc' target='_top'><img src='http://ads.plos.org/adview.php?what=zone:197&amp;source=NTD&amp;n=aec547bc' border='0' alt=''></a></noscript>
            </div>
            <!-- end : advocacy blocks -->
            <!-- begin : stay-connected block -->
            <div id="connect" class="block">
              <h3>Stay Connected</h3>
              <ul>
                  <li><img src="images/icon_alerts_small.gif" alt="email alerts icon" /><a href="${freemarker_config.registrationURL}"><strong>E-mail Alerts</strong></a><br />Sign up for alerts by e-mail</li>
                  <li><img src="images/icon_rss_small.gif" alt="rss icon" /><@s.url action="rssInfo" namespace="/static" includeParams="none" id="rssinfo"/><a href="${Request[freemarker_config.journalContextAttributeKey].baseUrl}${rssPath}"><strong>RSS</strong></a> (<a href="${rssinfo}">What is RSS?</a>)<br />Subscribe to content feed</li>
                  <li><img src="images/icon_join.gif" alt="join PLoS icon" /><a href="http://www.plos.org/support/donate.php" title="Join PLoS: Show Your Support"><strong>Join PLoS</strong></a><br />Support the open-access movement!</li>
              </ul>
            </div>
            <!-- end : stay-connected block -->
            <!-- begin : blog block -->
            <div id="blog" class="block">
              <h3>From the PLoS Blog</h3>
              <p>Read the <a href="http://www.plos.org/cms/blog" title="PLoS Blog">PLoS Blog</a> and contribute your views on scientific research and open-access publishing.</p>
              <ul class="articles">
                  <li><a href="http://www.plos.org/cms/node/274">Oops we missed our own birthday</a></li>
                  <li><a href="http://www.plos.org/cms/node/272">Journal Clubs - think of the future!</a></li>
                  <li><a href="http://www.plos.org/cms/node/271">Using open-access articles for student projects</a></li>
              </ul>
            </div>
            <!-- end : blog block -->
          </div><!-- end : subcol first -->
          <!-- end : col 3 -->
          <!-- begin : col 4 -->
          <div class="subcol last">
            <div class="block banner"><!--skyscraper-->
              <script language='JavaScript' type='text/javascript' src='http://ads.plos.org/adx.js'></script>
<script language='JavaScript' type='text/javascript'>
<!--
   if (!document.phpAds_used) document.phpAds_used = ',';
   phpAds_random = new String (Math.random()); phpAds_random = phpAds_random.substring(2,11);
   document.write ("<" + "script language='JavaScript' type='text/javascript' src='");
   document.write ("http://ads.plos.org/adjs.php?n=" + phpAds_random);
   document.write ("&amp;what=zone:192&amp;source=NTD&amp;target=_top&amp;block=1&amp;blockcampaign=1");
   document.write ("&amp;exclude=" + document.phpAds_used);
   if (document.referrer)
      document.write ("&amp;referer=" + escape(document.referrer));
   document.write ("'><" + "/script>");
//-->
</script><noscript><a href='http://ads.plos.org/adclick.php?n=a93f3323' target='_top'><img src='http://ads.plos.org/adview.php?what=zone:192&amp;source=NTD&amp;n=a93f3323' border='0' alt=''></a></noscript>
            </div><!-- end : block banner -->
          </div><!-- end : subcol last -->
        </div><!-- end : wrapper for cols 3 & 4 -->
        <div id="lower">&nbsp;</div> <!-- displays lower background image -->
      </div><!-- end : col -->
      <div class="partner">
        <a href="http://www.fedora-commons.org" title="Fedora-Commons.org"><img src="${freemarker_config.context}/images/pone_home_fedoracommons.png" alt="Fedora-Commons.org"/></a>
				<a href="http://www.moore.org" title="Gorden and Betty Moore Foundation"><img src="${freemarker_config.context}/images/pone_home_moore.gif" alt="Moore Foundation"/></a>
        <a href="http://www.mulgara.org/" title="Mulgara.org"><img src="${freemarker_config.context}/images/pone_home_mulgara.gif" alt="Mulgara.org"/></a>
        <a href="http://www.sciencecommons.org/" title="Science Commons"><img src="${freemarker_config.context}/images/pone_home_sciencecommons.gif"  alt="Science Commons"/></a>
				<a href="http://www.unitedlayer.com/" title="UnitedLayer, LLC"><img src="${freemarker_config.context}/images/pone_home_unitedlayer.gif" alt="UnitedLayer, LLC"/></a>
      </div><!-- end : block partners -->
    </div><!-- end : home -->
  </div><!-- end : wrap -->
</div><!-- end : content -->