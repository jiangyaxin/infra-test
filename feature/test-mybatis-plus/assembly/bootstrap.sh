#!/bin/bash

base_dir="$(dirname "$0")"
env_path="$base_dir/env.ini"
pid_path="$base_dir/pid"
java_io_tmp_dir="$base_dir/tmp"
default_debug_port=5005
default_jmx_port=7091
jar_path="$(find "$base_dir" -name '*.jar' | head -n 1)"
cd "$base_dir"

if test -f "$env_path"
then
  export $(cut -d= -f1 "$env_path"|grep -v '^#')
  source "$env_path"
  echo "--------------------ENV INI--------------------"
  echo "source $env_path"
  echo ""
  cat "$env_path"
  echo ""
  echo "--------------------ENV INI--------------------"
    echo ""
fi

if test -z "$JAVA_HOME"
then
  echo "Error,please set JAVA_HOME"
  return
fi

if test -z "$LOG_PATH"
then
  export LOG_PATH=$base_dir/logs
fi

if test -z "$JAVA_OPTS"
then
  JAVA_OPTS="-Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Djava.net.preferIPv4Stack=true -Djava.awt.headless=true -Djava.io.tmpdir=$java_io_tmp_dir"
fi

if test -z "$HEAP_OPTS"
then
  HEAP_OPTS="-Xms512M -Xmx512M -XX:MetaspaceSize=128M"
fi

if test -z "$HEAP_DUMP_OPTS"
then
  HEAP_DUMP_OPTS="-XX:ErrorFile=$LOG_PATH/hs_err_pid%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOG_PATH/heap_dump-pid%p.hprof"
fi

if test -z "$GC_OPTS"
then
  GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2M -XX:+PrintGCDetails -Xloggc:$LOG_PATH/gc-pid%p.log"
fi

if test -z "$DEBUG_OPTS"
then
  if test -z "$DEBUG_PORT"
  then
    DEBUG_PORT=$default_debug_port
  fi
  DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=0.0.0.0:$DEBUG_PORT,server=y,suspend=n"
fi

if test -z "$JMX_OPTS"
then
  if test -z "$JMX_PORT"
  then
    JMX_PORT=$default_jmx_port
  fi
  JMX_OPTS="-Dcom.sun.management.jmxremote=true -Djava.rmi.server.hostname=$(hostname -I) -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
fi

function show_help(){
    echo "Example: ./bootstrap.sh start"
    echo "Options:"
    echo "run : Start instance with current window"
    echo "start : Start instance with background"
    echo "debug : Start instance with debugger, debug port default is $default_debug_port,jmx port default is $default_jmx_port"
    echo "stop : Stop instance"
    echo "restart : Stop & Start instance with background"
}

if [ $# == 0 ]; then
    echo "Error,please input args"
    show_help ""
    exit 1
fi

if test ! -d "$java_io_tmp_dir"
then
  mkdir -p "$java_io_tmp_dir"
fi

function print_env(){
  echo "--------------------JVM OPTS--------------------"
  echo "JAVA_HOME: $JAVA_HOME"
  echo "LOG_PATH: $LOG_PATH"
  echo "JAVA_OPTS: $JAVA_OPTS"
  echo "HEAP_OPTS: $HEAP_OPTS"
  echo "HEAP_DUMP_OPTS: $HEAP_DUMP_OPTS"
  echo "GC_OPTS: $GC_OPTS"
  echo "--------------------JVM OPTS--------------------"
  echo ""
}

function run(){
  print_env
  echo "$_run_java -jar $jar_path $_java_opts"
  "$_run_java" -jar "$jar_path" "$_java_opts"
}

function start(){
  print_env
  if test -f "$pid_path"
  then
    _pid=$(cat "$pid_path")
    if ps -p "$_pid" >/dev/null 2>&1; then
      echo "Instance is running with pid $_pid,start failed"
      ps -fp "$_pid"
      exit 1
    else
      rm "$pid_path" -f
      echo "Deleted pid file $pid_path"
    fi
  fi

  if test -d "$java_io_tmp_dir"
  then
    rm "$java_io_tmp_dir" -rf
    echo "Deleted java.io.tmpdir $pid_path"
  fi

  if [ "$start_mode" = "debug" ]; then
    _exec_str="nohup $_run_java $DEBUG_OPTS $JMX_OPTS -jar $jar_path $_java_opts >/dev/null 2>&1 &"
  else
    _exec_str="nohup $_run_java -jar $jar_path $_java_opts >/dev/null 2>&1 &"
  fi

  echo "$_exec_str"
  eval "$_exec_str"
  _pid=$(ps -ef|grep "$jar_path"|grep -v grep|awk '{print $2}')
  echo "$_pid" > "$pid_path"

  _sleep=120
  while [ $_sleep -ge 0 ]; do
      echo -n "."
      sleep 1
      if ! kill -0 "$_pid" > /dev/null 2>&1; then
          printf "\nStart instance failed with pid %s\n" "$_pid"
          exit 1
      fi
      up_port_list=$(for line in $(netstat -tlpn | grep LISTEN | grep $_pid | awk '{print $4}');do echo "${line##*:}"; done | sort | uniq)
      if [ -n "$up_port_list" ]; then
          printf "\nStart instance with pid %s,listen port: %s\n" "$_pid" "$(echo $up_port_list)"
          exit 0
      fi
      _sleep=$(expr "$_sleep" - 1)
  done

  printf "\nStart instance with pid %s,but no listen port\n" "$_pid"
}

function debug(){
  print_env
  echo "DEBUG_OPTS: $DEBUG_OPTS"
  echo "JMX_OPTS: $JMX_OPTS"
  start
}

function stop(){
  print_env
  if test ! -f "$pid_path"
  then
    printf "\nPid file missing,stop failed\n"
    exit 1
  fi

  _pid=$(cat "$pid_path")
  if test -z "$_pid"
  then
    _pid=$(ps -ef|grep "$jar_path"|grep -v grep|awk '{print $2}')
  fi

  if test -z "$_pid"
  then
    printf "\nInstance with pid %s already stopped\n" "$_pid"
    exit 0
  fi

  _ps_result=$(ps -ef|grep "$jar_path"|grep -v grep)
  echo $_ps_result
  if test -z "$_ps_result"
  then
    printf "\nInstance with pid %s already stopped\n" "$_pid"
    exit 0
  fi

  kill -15 "$_pid" && wait_for_process_exit "$_pid"
  rm "$pid_path" -f
  echo "Deleted pid file $pid_path"
  printf "\nStop instance with pid %s\n" "$_pid"
}

function wait_for_process_exit() {
        _pid=$1
        begin=$(date +%s)
        local end
        while kill -0 "$_pid" > /dev/null 2>&1
        do
                echo -n "."
                sleep 1;
                end=$(date +%s)
                if [ $((end-begin)) -gt 60  ];then
                        echo -e "\nERROR: Timeout\n"
                        exit 1;
                fi
        done
}

_java_opts="$JAVA_OPTS $HEAP_OPTS $HEAP_DUMP_OPTS $GC_OPTS"
_run_java="$JAVA_HOME/bin/java"
start_mode=$1
if [ "$start_mode" = "run" ]; then
  run
elif [ "$start_mode" = "start" ]; then
  start
elif [ "$start_mode" = "debug" ]; then
  debug
elif [ "$start_mode" = "stop" ]; then
  stop
elif [ "$start_mode" = "restart" ]; then
  stop
  start
else
  show_help
  exit 0
fi