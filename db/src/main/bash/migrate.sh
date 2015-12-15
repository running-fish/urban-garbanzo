#!/bin/bash
#
# Applies migrations.

BASEDIR=$(dirname $0)
MIGRATIONS=$(ls $BASEDIR/../resources/migrations/*.sql)

HOST=127.0.0.1

for f in $MIGRATIONS
do
  mysql -h $HOST -u root < $f
done;
