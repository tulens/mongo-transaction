#!/bin/sh
script_full_path=$(dirname "$0")
docker-compose -f ${script_full_path}/docker-compose.yml stop
docker-compose -f ${script_full_path}/docker-compose.yml up --build --remove-orphans -d
sleep 4
docker exec -i -t mongo0 mongo localhost --eval "rs.initiate();"
#docker exec -i -t mongo0 mongo localhost --eval "
#rs.initiate(
#  {
#    _id : 'rs0',
#    members: [
#      { _id : 0, host : \"127.0.0.1:4200\" },
#      { _id : 1, host : \"127.0.0.1:4201\" },
#      { _id : 2, host : \"127.0.0.1:4202\" }
#    ]
#  }
#)
#"
