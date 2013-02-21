create table journalAlert (
  journalAlertID bigint(20) not null auto_increment,
  journalID bigint(20) not null,
  sortOrder integer not null,
  alertName varchar(30) character set utf8 collate utf8_bin default null,
  created datetime not null,
  lastModified datetime not null,
  primary key (journalAlertID),
  unique key (alertName),
  constraint foreign key (journalID) references journal (journalID)
) engine=innodb default charset=utf8;

create table userProfileJournalAlertJoinTable (
  journalAlertID bigint(20) not null,
  userProfileID bigint(20) not null,
  unique key (userProfileID, journalAlertID),
  constraint foreign key (journalAlertID) references journalAlert (journalAlertID),
  constraint foreign key (userProfileID) references userProfile (userProfileID)
) engine=innodb default charset=utf8;

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'biology_monthly', now(), now() from journal j where j.journalKey = 'PLoSBiology';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'biology_weekly', now(), now() from journal j where j.journalKey = 'PLoSBiology';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'clinical_trials_monthly', now(), now() from journal j where j.journalKey = 'PLoSClinicalTrials';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'clinical_trials_weekly', now(), now() from journal j where j.journalKey = 'PLoSClinicalTrials';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'computational_biology_monthly', now(), now() from journal j where j.journalKey = 'PLoSCompBiol';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'computational_biology_weekly', now(), now() from journal j where j.journalKey = 'PLoSCompBiol';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'genetics_monthly', now(), now() from journal j where j.journalKey = 'PLoSGenetics';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'genetics_weekly', now(), now() from journal j where j.journalKey = 'PLoSGenetics';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'medicine_monthly', now(), now() from journal j where j.journalKey = 'PLoSMedicine';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'medicine_weekly', now(), now() from journal j where j.journalKey = 'PLoSMedicine';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'pathogens_monthly', now(), now() from journal j where j.journalKey = 'PLoSPathogens';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'pathogens_weekly', now(), now() from journal j where j.journalKey = 'PLoSPathogens';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'plosntds_monthly', now(), now() from journal j where j.journalKey = 'PLoSNTD';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 2, 'plosntds_weekly', now(), now() from journal j where j.journalKey = 'PLoSNTD';

insert into journalAlert(journalID, sortOrder, alertName, created, lastModified)
  select j.journalID, 1, 'plosone_weekly', now(), now() from journal j where j.journalKey = 'PLoSONE';
