#!/bin/bash

# Create log directory
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
LOGDIR="performance_logs_java"
mkdir -p $LOGDIR

CPU_LOG="$LOGDIR/cpu_usage_$TIMESTAMP.log"
DISK_LOG="$LOGDIR/disk_usage_$TIMESTAMP.log"
TIME_LOG="$LOGDIR/time_output_$TIMESTAMP.log"

# Compile Java code
echo "Compiling Main.java..."
javac Main.java
if [ $? -ne 0 ]; then
    echo "Compilation failed"
    exit 1
fi
echo "Compilation successful."

# Run iostat for disk I/O in background
iostat -d 2 10 > $DISK_LOG &
IOSTAT_PID=$!

# Run top CPU monitoring in batch mode (approx 10 seconds)
top -b -d 2 -n 5 >> $CPU_LOG &

# Run Java program timed and capture output and CPU times
echo "Running Java program..."
{ time java Main ; } 2>> $TIME_LOG

# Stop iostat and top after program finishes
kill $IOSTAT_PID 2>/dev/null

echo "Java execution and monitoring completed."
echo "Log files saved in $LOGDIR/"

