#!/usr/bin/env bash

## Unit Test Configuration
server_list=("130.126.112.146" "130.126.112.117" "130.126.112.148" "130.126.112.146")
port_list=(1111 1122 1133 1144)
log_path="/tmp/kk_logs"
server_list_num=${#server_list[@]}
result_path=/tmp/LogQueryResult
answer_path=/tmp/LogQueryAnswer
filesize=1

## arg1 = unit test name, arg2 = grep args
generate_log() {
	echo "Start generating log file with size $filesize MB";
	mkdir -p $log_path
	for ((i=1;i<=$server_list_num;i++)); do
		ssh ${server_list[$(($i-1))]} "mkdir -p $log_path"
		java LogGenerator $log_path $filesize $i
		scp $log_path/machine.$i.log ${server_list[$(($i-1))]}:$log_path
	done
	echo "Finished generating log file";
}
test_grep() {
	java LogQueryClient -E "$2" > $result_path
	if [ -e $answer_path ]; then
		rm -f $answer_path
	fi
	for ((i=0;i<$server_list_num;i++)); do
		echo "Result from the Server ${server_list[$i]}:${port_list[$i]}:" >> $answer_path
		ssh ${server_list[${i}]} "grep -E \"$2\" $log_path/machine.$(($i+1)).log" >> $answer_path
	done
	if diff $result_path $answer_path; then
		echo "Unit Test $1 passed"
	else
		echo "Unit Test $1 failed"
	fi
}
## Generate the test logs
generate_log

## Test greps for all logs
test_grep "\"Frequent pattern from all logs\"" "(INFO|WARNING)"
test_grep "\"Somewhat frequent pattern from all logs\"" "FINE"
test_grep "\"Rare frequent pattern from all logs\"" "SEVERE"

## Test greps for one log
for ((j=1;j<=$server_list_num;j++)); do
	test_grep "\"Frequent pattern from one log: Machine $j\"" "(INFO|WARNING).*MachineID: $j"
	test_grep "\"Somewhat frequent pattern one log: Machine $j\"" "FINE.*MachineID: $j"
	test_grep "\"Rare frequent pattern from one log: Machine $j\"" "SEVERE.*MachineID: $j"
done

## Test greps for some logs
for ((j=1;j<=$server_list_num;j++)); do
	for ((k=$j+1;k<=$server_list_num;k++)); do
		test_grep "\"Frequent pattern from some logs: Machine $j,$k\"" "(INFO|WARNING).*SomeMachineIDs: $j,$k"
		test_grep "\"Somewhat frequent pattern some logs: Machine $j,$k\"" "FINE.*MachineID: $j,$k"
		test_grep "\"Rare frequent pattern from some logs: Machine $j,$k\"" "SEVERE.*MachineID: $j,$k"
	done
done
