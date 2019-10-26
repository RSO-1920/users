# RSO: Users Microservice

## Prerequirements

* ```docker run -d -p 2379:2379 --name etcd --volume=/tmp/etcd-data:/etcd-data quay.io/coreos/etcd:latest /usr/local/bin/etcd --name my-etcd-1 --data-dir /etcd-data --listen-client-urls http://0.0.0.0:2379 --advertise-client-urls http://0.0.0.0:2379 --listen-peer-urls http://0.0.0.0:2380 --initial-advertise-peer-urls http://0.0.0.0:2380 --initial-cluster my-etcd-1=http://0.0.0.0:2380 --initial-cluster-token my-etcd-token --initial-cluster-state new --auto-compaction-retention 1 -cors="*"```

* `` docker run -d -p 9080:8080 nikfoundas/etcd-viewer ``  // setup env for channel API


## RUN

*  ```docker run -d --name rso1920-users-api -e KUMULUZEE_CONFIG_ETCD_HOSTS=<your_ip>:2379 -p 8083:8083 rso1920/users:latest```