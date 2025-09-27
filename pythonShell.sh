#!/bin/bash

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
LOGDIR="performance_logs_python"
mkdir -p $LOGDIR

CPU_LOG="$LOGDIR/cpu_usage_$TIMESTAMP.log"
DISK_LOG="$LOGDIR/disk_usage_$TIMESTAMP.log"
TIME_LOG="$LOGDIR/time_output_$TIMESTAMP.log"

# Run disk I/O stats in background
iostat -d 2 10 > $DISK_LOG &
IOSTAT_PID=$!

# Run top for CPU usage
top -b -d 2 -n 5 >> $CPU_LOG &

# Run the python program with timing
echo "Running Python program..."
{ time python3 codeInPython.py ; } 2>> $TIME_LOG

# Kill background monitoring
kill $IOSTAT_PID 2>/dev/null

echo "Python run complete. Logs stored in $LOGDIR"

