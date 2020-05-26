#!/bin/sh
source ~/.bash_profile
pid_file='pid.pid'
start()
{
        echo $"Starting messenger application server ......"
        java -server -Xms256m -Xmx256m  -jar messenger.jar > output 2>&1 &
        echo $! > $pid_file

        echo $"messenger application server started!"
}

stop()
{
        echo $"Stopping messenger application server ......"
        pid=`cat $pid_file`
        kill -9 $pid
        echo "stop "$pid
        sleep 1
}

restart()
{
        stop
        sleep 5
        start
}

case "$1" in
start)
        start
        ;;
stop)
        stop
        ;;
restart)
        restart
        ;;
*)
        echo $"Usage: $0 {start|stop|restart}"
        exit 1
esac
