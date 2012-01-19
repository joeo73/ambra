#
#$HeadURL:
#$Id:
#
# Copyright (c) 2006-2011 by Public Library of Science
#
# http://plos.org
# http://ambraproject.org
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
echo Start time: `date`
echo -n "Rebuilding database...  "
if mysql -uroot < create_db.sql
then
  echo "Finished"
  mvn package appassembler:assemble -Dmaven.test.skip=true && bash target/appassembler/bin/migrate
fi

echo -n "Tunning / migrating database...  "
mysql -uroot < migrate_db.sql

echo Finish time: `date`
