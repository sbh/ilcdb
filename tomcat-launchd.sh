#!/bin/bash

function shutdown()
{
  $CATALINA_HOME/bin/catalina.sh stop
}

export TOMCAT_JVM_PID=/tmp/$$

. $CATALINA_HOME/bin/catalina.sh start

trap shutdown HUP INT QUIT ABRT KILL ALRM TERM TSTP

wait `cat $TOMCAT_JVM_PID`

