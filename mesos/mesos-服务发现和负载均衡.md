# 1 发布docker程序到marathon

## 1.1 搭建私有docker registry

### 1.1.1 安装docker regisry

```
docker pull docker-registry
docker run -t -p 5000:5000 docker-registry
```
### 下载docker镜像并发布到私有registry

```
docker pull consol/tomcat-8.0
docker tag consol/tomcat-8.0 192.168.1.103:5000/tomcat
docker push 192.168.1.103:5000/tomcat

```

### 所有需从私有regisry下载docker镜像的客户端作以下配置(主要是mesos slave机器)

`vi /usr/lib/systemd/system/docker.service`

修改如下：即在`/usr/bin/docker -d`后增加`--insecure-registry 192.168.1.103:5000`

```
[Unit]
Description=Docker Application Container Engine
Documentation=http://docs.docker.com
After=network.target

[Service]
Type=notify
EnvironmentFile=-/etc/sysconfig/docker
EnvironmentFile=-/etc/sysconfig/docker-storage
EnvironmentFile=-/etc/sysconfig/docker-network
ExecStart=/usr/bin/docker -d --insecure-registry 192.168.1.103:5000 $OPTIONS \
          $DOCKER_STORAGE_OPTIONS \
          $DOCKER_NETWORK_OPTIONS \
          $ADD_REGISTRY \
          $BLOCK_REGISTRY \
          $INSECURE_REGISTRY
LimitNOFILE=1048576
LimitNPROC=1048576
LimitCORE=infinity
MountFlags=slave

[Install]
WantedBy=multi-user.target
```

## 1.2 发布docker镜像到marathon平台
### 1.2.1 修改mesos client配置

所有的mesos slave机器:

```
echo 'docker,mesos' > /etc/mesos-slave/containerizers
echo '5mins' > /etc/mesos-slave/executor_registration_timeout
systemctl restart mesos-slave

```

### 1.2.2 编写Docker.json

```
{
  "container": {
    "type": "DOCKER",
    "docker": {
      "image": "192.168.1.103:5000/tomcat",
      "network": "BRIDGE",
      "portMappings": [
         { "containerPort": 8080, "hostPort": 0, "protocol": "tcp" }
      ]
    }
  },
  "id": "tomcat",
  "instances": 3,
  "cpus": 0.5,
  "mem": 512,
  "uris": [],
  "cmd":"/opt/tomcat/bin/deploy-and-run.sh"
}
```

### 1.2.3 通过marathon api发布

`curl -X POST -H "Content-Type: application/json" http://192.168.1.110:8080/v2/apps -d@Docker.json`

### 1.1.3 说明

本例发布了2个docker images,另外是一个spring boot的可执行jar包。

