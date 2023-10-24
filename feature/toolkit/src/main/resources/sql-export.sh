#!/bin/bash
HOST="127.0.0.1"
PORT="3306"
USER="root"
PASSWORD=""
DATABASE=""
FILE="./sql"

function show_help(){
    echo "Example: ./sql-export.sh -h127.0.0.1 -P3306 -uroot -p123456 -f./sql -dmysql"
    echo "Options:"
    echo "-h|--host : Mysql host,default is 127.0.0.1"
    echo "-P|--port : Mysql port,default is 3306"
    echo "-u|--user : Mysql user,default is root"
    echo "-p|--password : Mysql password"
    echo "-f|--path : Export sql root path,default is ./sql"
    echo "-d|--database : Export database name"
}

#选项后面的冒号表示该选项需要参数
SHORT_OPTS="h:,P:,u:,p:,f:,d:"
LONG_OPTS="host:,port:,user:,password:,path:,database:,help"
ARGS=$(getopt --options $SHORT_OPTS --longoptions $LONG_OPTS -- "$@")

eval set -- "$ARGS"
while true;
do
    case "$1" in
        -h|--host)
           HOST="$2"
           # shift用于参数的左移，shift n表示向左移出n个参数，这样使 $1 能读取到后面的参数
           shift 2
           ;;
        -P|--port)
           PORT="$2"
           shift 2
           ;;
        -u|--user)
           USER="$2"
           shift 2
           ;;
        -p|--password)
           PASSWORD="$2"
           shift 2
           ;;
        -f|--path)
           FILE="$2"
           shift 2
           ;;
        -d|--database)
           DATABASE="$2"
           shift 2
           ;;
        --help)
           show_help ""
           exit 0
           ;;
        --)
           shift
           break
           ;;
    esac
done

if [ -z "$DATABASE" ];
then
    echo "Error,please input databases"
    show_help ""
    exit 1
fi

echo "Host: $HOST"
echo "Port: $PORT"
echo "User: $USER"
echo "Password: $PASSWORD"
echo "Path: $FILE"
echo "Database: $DATABASE"
echo "------------------START------------------"

if [ ! -d "$FILE" ];
then
  mkdir "$FILE"
fi

database_ddl_path="$FILE/${DATABASE}_database_ddl.sql"
table_ddl_path="$FILE/${DATABASE}_table_ddl.sql"
data_dml_path="$FILE/${DATABASE}_data_dml.sql"

if [ -n "$PASSWORD" ];
then
    mysqldump --databases "$DATABASE"  --set-gtid-purged=OFF -t -d -h"$HOST" -P"$PORT" -u"$USER" -p"$PASSWORD" > "$database_ddl_path"
else
    mysqldump --databases "$DATABASE"  --set-gtid-purged=OFF -t -d -h"$HOST" -P"$PORT" -u"$USER" > "$database_ddl_path"
fi

if [ -n "$PASSWORD" ];
then
    mysqldump --databases "$DATABASE"  --set-gtid-purged=OFF -n -d -h"$HOST" -P"$PORT" -u"$USER" -p"$PASSWORD" > "$table_ddl_path"
else
    mysqldump --databases "$DATABASE"  --set-gtid-purged=OFF -n -d -h"$HOST" -P"$PORT" -u"$USER" > "$table_ddl_path"
fi

if [ -n "$PASSWORD" ];
then
    mysqldump --databases "$DATABASE"  --set-gtid-purged=OFF -n -t -c -h"$HOST" -P"$PORT" -u"$USER" -p"$PASSWORD" > "$data_dml_path"
else
    mysqldump --databases "$DATABASE"  --set-gtid-purged=OFF -n -t -c -h"$HOST" -P"$PORT" -u"$USER" > "$data_dml_path"
fi
echo "------------------END------------------"