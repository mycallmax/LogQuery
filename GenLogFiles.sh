#!/bin/bash
# Generates log files of different sizes and different 
# pattern distribution.
#

# Clear logs
cd logs
rm *
cd ..

# 1MB logfiles with "frequent", "rare" and "Uniform" patterns
java LogGenerator TestLog 1 frequent   > /dev/null 2 > /dev/null & 
java LogGenerator TestLog 1 rare  > /dev/null  2 > /dev/null & 
java LogGenerator TestLog 1 uniform > /dev/null 2 > /dev/null  & 

# 5MB logfiles with "frequent", "rare" and "Uniform" patterns
java LogGenerator TestLog 5 frequent > /dev/null 2 > /dev/null & 
java LogGenerator TestLog 5 rare  > /dev/null  2 > /dev/null &
java LogGenerator TestLog 5 uniform  > /dev/null 2 > /dev/null &

# 10MB logfiles with "frequent", "rare" and "Uniform" patterns
#nohup java LogGenerator TestLog 10 frequent > /dev/null &
#nohup java LogGenerator TestLog 10 rare > /dev/null &
#nohup java LogGenerator TestLog 10 uniform > /dev/null &

# 100MB logfiles with "frequent", "rare" and "Uniform" patterns
#nohup java LogGenerator TestLog 100 frequent &
#nohup java LogGenerator TestLog 100 rare &
#nohup java LogGenerator TestLog 100 uniform &

exit 0
