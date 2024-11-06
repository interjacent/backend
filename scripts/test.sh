#!/usr/bin/env bash

set -eux

BASE_URL=http://localhost:8080/api/v1/polls

# create poll
CREATE_RESPONSE=$(curl "$BASE_URL/" -f -H Content-Type:application/json --data-raw '{"days": [{"start": 1731186000, "end": 1731272400}, {"start": 1731358800, "end": 1731445200}]}')

POLL_ID=$(jq <<<"$CREATE_RESPONSE" -r .pollId)
ADMIN_TOKEN=$(jq <<<"$CREATE_RESPONSE" -r .adminToken)

USER_ID=12345678-1234-1234-1234-123456789012
USER_NAME=helloworld

# add a user
curl "$BASE_URL/$POLL_ID/join" -f -H Content-Type:application/json --data-raw '{"userId": "'"$USER_ID"'", "userName": "'"$USER_NAME"'"}' | jq

# check the poll
curl "$BASE_URL/$POLL_ID" -f | jq

# set intervals
curl "$BASE_URL/$POLL_ID/users/$USER_ID/intervals" -f -H Content-Type:application/json --data-raw '[{"start": 1731186000, "end": 1731272000}, {"start": 1731359000, "end": 1731445200}]' | jq
#curl "$BASE_URL/$POLL_ID/users/$USER_ID/intervals" -f -H Content-Type:application/json --data-raw '{"start": 1731186000, "end": 1731272000}' | jq

# get intervals
curl "$BASE_URL/$POLL_ID/users/$USER_ID/intervals" -f | jq

# check the poll again
curl "$BASE_URL/$POLL_ID" -f | jq

# close the poll
curl "$BASE_URL/$POLL_ID/finish" -f -H Content-Type:application/json --data-raw '{"adminToken": "'"$ADMIN_TOKEN"'", "start": 1731186000, "end": 1731187000}' | jq

# check the poll for the last time
curl "$BASE_URL/$POLL_ID" -f | jq
