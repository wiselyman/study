# 1 Apache Mesos简介

Apache Mesos据说是云计算的未来，利用Mesos可轻易实现一个PaaS云平台。

请参考 `http://mesos.apache.org/`和`http://mesosphere.com/`查看详细

# 2 Master节点安装

## 规划

- 192.168.1.110 master1
- 192.168.1.111 master2
- 192.168.1.112 master3

## 关闭防火墙

- master1,master2,master3
```
systemctl stop firewalld && systemctl disable firewalld
```

## 添加 mesos的yum源

- master1,master2,master3
```
sudo rpm -Uvh http://repos.mesosphere.io/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm
```


## 安装mesos及相关软件

- master1,master2,master3
```
yum -y install mesos marathon
yum -y install mesosphere-zookeeper
```

## 配置zookeeper

- master1
```
touch /var/lib/zookeeper/myid
echo 1 > /var/lib/zookeeper/myid
```

- master2
```
touch /var/lib/zookeeper/myid
echo 2 > /var/lib/zookeeper/myid
```

- master3
```
touch /var/lib/zookeeper/myid
echo 3 > /var/lib/zookeeper/myid
```


- master1,master2,master3

`vi /etc/zookeeper/conf/zoo.cfg`

添加

```
server.1=192.168.1.110:2888:3888
server.2=192.168.1.111:2888:3888
server.3=192.168.1.112:2888:3888
```

- master1,master2,master3
`vi /etc/mesos/zk`
增加内容:
`zk://192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181/mesos`

- master1,master2,master3
`vi /etc/mesos-master/quorum`

增加内容：`2`

## 配置mesos和marathon

- master1
```
echo 192.168.1.110 | sudo tee /etc/mesos-master/hostname
mkdir -p /etc/marathon/conf/ && touch hostname
echo 192.168.1.110 | sudo tee /etc/marathon/conf/hostname
```

- master2
```
echo 192.168.1.111 | sudo tee /etc/mesos-master/hostname
mkdir -p /etc/marathon/conf/ && touch hostname
echo 192.168.1.111 | sudo tee /etc/marathon/conf/hostname
```

- master3
```
echo 192.168.1.112 | sudo tee /etc/mesos-master/hostname
mkdir -p /etc/marathon/conf/ && touch hostname
echo 192.168.1.112 | sudo tee /etc/marathon/conf/hostname
```


## 启动zookeeper mesos-master marathon

- master1,master2,master3
```
systemctl start  zookeeper && systemctl start mesos-master && systemctl start marathon

systemctl disable mesos-slave
```



# 3 Slave节点安装

## 规划

- 192.168.1.113 client1

## 关闭防火墙

```
systemctl stop firewalld && systemctl disable firewalld

## 添加 mesos的yum源

```
sudo rpm -Uvh http://repos.mesosphere.io/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm
```


## 安装mesos

`yum -y install mesos`

## 配置master信息

`vi /etc/mesos/zk`

增加

```
zk://192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181/mesos
```

```
cd /etc/mesos-slave/
touch hostname
echo 192.168.1.113 | sudo tee /etc/mesos-slave/hostname
```

## 启动mesos-slave

```
systemctl start  mesos-slave  && systemctl enable  mesos-slave

systemctl disable mesos-master
```

```

# 4 测试

- 访问 http://192.168.1.110:5050 mesos地址
- 访问 http://192.168.1.110:8080 marathon地址，可利用其发布docker镜像并可调整应用数量





