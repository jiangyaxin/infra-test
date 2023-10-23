#!/bin/bash
HOST="127.0.0.1"
PORT="3306"
USER="root"
PASSWORD=""
FILE="./sql"

function show_help(){
    echo "Example: ./sql-script.sh -h127.0.0.1 -P3306 -uroot -p123456 -f./sql"
    echo "Options:"
    echo "-h|--host : Mysql host,default is 127.0.0.1"
    echo "-P|--port : Mysql port,default is 3306"
    echo "-u|--user : Mysql user,default is root"
    echo "-p|--password : Mysql password"
    echo "-f|--path=./sql : Execute sql script recursively,sql root path,default is ./sql"
}

if [ $# == 0 ]; then
    echo "Error,please input args"
    show_help "$@"
    exit 1
fi

#选项后面的冒号表示该选项需要参数
SHORT_OPTS="h:,P:,u:,p:,f:"
LONG_OPTS="host:,port:,user:,password:,path:,help"
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
        --help)
           show_help "$@"
           exit 0
           ;;
        --)
           shift
           break
           ;;
    esac
done

echo "Host: $HOST"
echo "Port: $PORT"
echo "User: $USER"
echo "Password: $PASSWORD"
echo "Path: $FILE"
echo "------------------START------------------"

function custom_function(){
  sql_file_path="$1"
  echo "Process $sql_file_path"

  if [ -n "$PASSWORD" ];
  then
      mysql -h"$HOST" -P"$PORT" -u"$USER" -p"$PASSWORD" < "$sql_file_path"
  else
      mysql -h"$HOST" -P"$PORT" -u"$USER" < "$sql_file_path"
  fi
}

function recursion_folder(){
  absolute_path=$(realpath -s "$1")

  if test -f "$absolute_path"
  then
     custom_function "$absolute_path"
     return
  fi

  echo "Work in: $absolute_path"
  cd "$absolute_path" || return

  children_of_dir=$(ls "$absolute_path")

  for child in $children_of_dir
  do
    child_absolute_path=$(realpath -s "$child")
    if test -d "$child_absolute_path"
    then
      recursion_folder "$child_absolute_path"
    else
      custom_function "$child_absolute_path"
    fi
  done
  cd ../
}

recursion_folder "$FILE"
echo "------------------END------------------"