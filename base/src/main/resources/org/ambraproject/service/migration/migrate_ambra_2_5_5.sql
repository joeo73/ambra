create table journalAlert (
  journalAlertID bigint(20) not null auto_increment,
  journalID bigint(20) not null,
  sortOrder integer not null,
  alertKey varchar(30) character set utf8 collate utf8_bin not null,
  emailSubject varchar(100) character set utf8 collate utf8_bin not null,
  emailArticleOrder varchar(15) character set utf8 collate utf8_bin not null,
  created datetime not null,
  lastModified datetime not null,
  primary key (journalAlertID),
  unique key (alertKey),
  constraint foreign key (journalID) references journal (journalID)
) engine=innodb default charset=utf8;

create table userProfileJournalAlertJoinTable (
  journalAlertID bigint(20) not null,
  userProfileID bigint(20) not null,
  unique key (userProfileID, journalAlertID),
  constraint foreign key (journalAlertID) references journalAlert (journalAlertID),
  constraint foreign key (userProfileID) references userProfile (userProfileID)
) engine=innodb default charset=utf8;

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'biology_monthly', 'PLOS Biology new articles published this month', 'ISSUE_TOC',
    now(), now() from journal j where j.journalKey = 'PLoSBiology';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'biology_weekly', 'PLOS Biology new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSBiology';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'clinical_trials_monthly', 'Clinical Trials new articles published this month', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSClinicalTrials';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'clinical_trials_weekly', 'Clinical Trials new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSClinicalTrials';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'computational_biology_monthly', 'Computational Biology new articles published this month', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSCompBiol';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'computational_biology_weekly', 'Computational Biology new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSCompBiol';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'genetics_monthly', 'PLOS Genetics new articles published this month', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSGenetics';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'genetics_weekly', 'PLOS Genetics new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSGenetics';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'medicine_monthly', 'PLOS Medicine new articles published this month', 'ISSUE_TOC',
    now(), now() from journal j where j.journalKey = 'PLoSMedicine';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'medicine_weekly', 'PLOS Medicine new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSMedicine';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'pathogens_monthly', 'PLOS Pathogens new articles published this month', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSPathogens';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'pathogens_weekly', 'PLOS Pathogens new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSPathogens';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'plosntds_monthly', 'PLOS Neglected Tropical Diseases new articles published this month', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSNTD';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 2, 'plosntds_weekly', 'PLOS Neglected Tropical Diseases new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSNTD';

insert into journalAlert(journalID, sortOrder, alertKey, emailSubject, emailArticleOrder, created, lastModified)
  select j.journalID, 1, 'plosone_weekly', 'PLOS ONE new articles published this week', 'ARTICLE_TYPE',
    now(), now() from journal j where j.journalKey = 'PLoSONE';


