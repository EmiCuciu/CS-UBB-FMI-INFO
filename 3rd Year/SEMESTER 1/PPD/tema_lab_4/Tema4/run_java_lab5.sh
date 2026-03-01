#!/bin/bash
# Run ParallelLab5 with 3 parameters: p_r, p_w, queue_capacity
# Output: doar timpul (in ms) pentru a putea fi folosit in run_process_lab5.sh
java -cp src/main/java com.example.ParallelLab5 $1 $2 $3 2>&1 | grep "Timp total:" | awk '{print $3}'

