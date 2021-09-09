#!/bin/sh
## test connections

echo "1st"

curl "http://localhost:5555/rad/jc.api:greeting/hello/john" \
     -H 'Accept: application/json' \
     -H 'Cookie: ssnid=f044cde13371479197db4a25dc5bfb36; ATGID=Anonymous' &

echo "2nd"

curl "http://localhost:5555/rad/jc.api:greeting/hello/bob" \
-H 'Accept: application/json' \
-H 'Cookie: ssnid=f044cde13371479197db4a25dc5bfb36; ATGID=Anonymous' &

echo "3rd"

curl "http://localhost:5555/rad/jc.api:greeting/hello/sam" \
-H 'Accept: application/json' \
-H 'Cookie: ssnid=f044cde13371479197db4a25dc5bfb36; ATGID=Anonymous' &

echo "4th"

curl "http://localhost:5555/rad/jc.api:greeting/hello/ham" \
-H 'Accept: application/json' \
-H 'Cookie: ssnid=f044cde13371479197db4a25dc5bfb36; ATGID=Anonymous' &
