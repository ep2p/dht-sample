# dht-sample
Distributed Hash Table sample using Spring Boot and ROW 

This repository contains a sample DHT using [Eleuth Kademlia Api](https://github.com/ep2p/kademlia-api), and [Rest Over Websocket](https://github.com/idioglossia/spring-rest-over-ws)

## RUN

First run 2 nodes:

**Node 1**

`mvn spring-boot:run -Dspring-boot.run.arguments="--nodeId=1 --server.port=8001 --server.address=127.0.0.1"`

**Node 2**

`mvn spring-boot:run -Dspring-boot.run.arguments="--nodeId=2 --server.port=8002 --server.address=127.0.0.1"`

---

Start node 1:

```
curl --request GET \
  --url http://localhost:8001/manager/start
```

Bootstrap node 2 using node 1 address:

```
curl --request POST \
  --url http://localhost:8002/manager/bootstrap \
  --header 'content-type: application/json' \
  --data '{
	"id": 1,
	"connectionInfo": {
		"address": "127.0.0.1",
		"port": 8001
	}
}'
```

Store data on nodes.

```
curl --request POST \
  --url http://localhost:8002/manager/store \
  --header 'content-type: application/json' \
  --data '{
	"key": 1,
	"value": "Value for key 1"
}'
```

response: `Node #1 STORED DATA`

Store another data:

```
curl --request POST \
  --url http://localhost:8002/manager/store \
  --header 'content-type: application/json' \
  --data '{
	"key": 2,
	"value": "Value for key 2"
}'
```

response: `Node #2 STORED DATA`

Both store calls were sent to second node, but as you can see, node#1 stored data for key 1.

Get data of key 1:

```
curl --request GET \
  --url http://localhost:8002/manager/get/1
```

Response: `Node #1 GOT DATA: Value for key 1`

Get data of key 2:

```
curl --request GET \
  --url http://localhost:8002/manager/get/2
```

Response: `Node #2 GOT DATA: Value for key 2`

So data is distributed. Now shutdown node 1 by pressing `cntl+c`.
After shutdown, try to get data of key 1, which previously was stored in node 1.

```
curl --request GET \
  --url http://localhost:8002/manager/get/1
```

Response: `Node #2 GOT DATA: Value for key 1`

Which means data was moved to closest node (from 1 to 2) before shutdown.

You can run this on more nodes, and results would have same logic :)