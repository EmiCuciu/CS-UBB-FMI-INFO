#!/bin/bash


SCRIPT_PATH="./run_test.sh"
RETRIES=${1:-3}          # Număr de rulări pentru fiecare configurație
OUTPUT_FILE=${2:-"benchmark_results.csv"}

INDEX=1
RESULTS=()

echo "════════════════════════════════════════════════════════"
echo "  Benchmark Script - Performance Testing"
echo "════════════════════════════════════════════════════════"
echo "  Retries per configuration: $RETRIES"
echo "  Output file: $OUTPUT_FILE"
echo "════════════════════════════════════════════════════════"
echo ""

if [ -f "$OUTPUT_FILE" ]; then
    echo "$OUTPUT_FILE exists. Output will be appended."
    INDEX=$(tail -n1 $OUTPUT_FILE | cut -d"," -f1)
    INDEX=$((INDEX+1))
else
    echo "$OUTPUT_FILE does not exist. A new file will be created."
    echo "trial_no,avg_duration_sec,num_clients,verification_interval,avg_response_time_ms,throughput_req_per_sec,success_rate_percent" > $OUTPUT_FILE
fi

echo ""

CONFIGS=(
    "10:5"
    "10:10"
)

for config in "${CONFIGS[@]}"; do
    IFS=':' read -r NUM_CLIENTS VERIFICATION <<< "$config"

    echo "════════════════════════════════════════════════════════"
    echo "  Testing configuration: $NUM_CLIENTS clients, ${VERIFICATION}s verification"
    echo "════════════════════════════════════════════════════════"
    echo ""

    DURATIONS=()
    AVG_TIMES=()
    THROUGHPUTS=()
    SUCCESS_RATES=()

    for i in $(seq 1 $RETRIES); do
        echo "Trial no: $i for $NUM_CLIENTS clients with ${VERIFICATION}s verification"

        # Rulează testul
        duration=$($SCRIPT_PATH $NUM_CLIENTS $VERIFICATION "trial_${INDEX}_${i}")
        DURATIONS+=($duration)

        # Extrage metricile de performanță din CSV
        PERF_FILE="test_results/trial_${INDEX}_${i}_performance.csv"
        if [ -f "$PERF_FILE" ]; then
            METRICS=$(tail -n 1 "$PERF_FILE")
            AVG_TIME=$(echo "$METRICS" | cut -d',' -f2)
            THROUGHPUT=$(echo "$METRICS" | cut -d',' -f3)
            SUCCESS_RATE=$(echo "$METRICS" | cut -d',' -f4)

            AVG_TIMES+=($AVG_TIME)
            THROUGHPUTS+=($THROUGHPUT)
            SUCCESS_RATES+=($SUCCESS_RATE)
        fi

        echo "  Duration: ${duration}s"
        echo ""

        # Pauză între rulări
        sleep 5
    done

    # Calculează medii pentru durate
    total_duration=0
    count=0
    for dur in "${DURATIONS[@]}"; do
        total_duration=$((total_duration + dur))
        count=$((count + 1))
    done
    avg_duration=$((total_duration / count))

    sum_avg_time=0
    sum_throughput=0
    sum_success_rate=0

    for val in "${AVG_TIMES[@]}"; do
        sum_avg_time=$(echo "$sum_avg_time + $val" | bc)
    done

    for val in "${THROUGHPUTS[@]}"; do
        sum_throughput=$(echo "$sum_throughput + $val" | bc)
    done

    for val in "${SUCCESS_RATES[@]}"; do
        sum_success_rate=$(echo "$sum_success_rate + $val" | bc)
    done

    avg_avg_time=$(echo "scale=2; $sum_avg_time / $count" | bc)
    avg_throughput=$(echo "scale=2; $sum_throughput / $count" | bc)
    avg_success_rate=$(echo "scale=2; $sum_success_rate / $count" | bc)

    echo "$INDEX,$avg_duration,$NUM_CLIENTS,$VERIFICATION,$avg_avg_time,$avg_throughput,$avg_success_rate" >> $OUTPUT_FILE

    echo "════════════════════════════════════════════════════════"
    echo "  Results for $NUM_CLIENTS clients, ${VERIFICATION}s verification:"
    echo "    - Average duration: ${avg_duration}s"
    echo "    - Average response time: ${avg_avg_time}ms"
    echo "    - Average throughput: ${avg_throughput} req/s"
    echo "    - Average success rate: ${avg_success_rate}%"
    echo "════════════════════════════════════════════════════════"
    echo ""

    INDEX=$((INDEX + 1))
done

echo ""
echo "════════════════════════════════════════════════════════"
echo "  Benchmark Completed!"
echo "════════════════════════════════════════════════════════"
echo "  Results saved to: $OUTPUT_FILE"
echo ""
echo "Summary:"
cat $OUTPUT_FILE
echo ""
