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
JOIN_RESPONSE=$(curl "$BASE_URL/$POLL_ID/join" -f -H Content-Type:application/json --data-raw '{"userId": "'"$USER_ID"'", "userName": "'"$USER_NAME"'"}')

# check the poll
curl "$BASE_URL/$POLL_ID" -f

# set intervals
SET_INTERVALS_RESPONSE=$(curl "$BASE_URL/$POLL_ID/users/$USER_ID/intervals" -f -H Content-Type:application/json --data-raw '[{"start": 1731186000, "end": 1731272000}, {"start": "1731359000, "end": 1731445200}]')
