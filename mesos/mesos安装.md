# 1 Apache Mesos简介

Apache Mesos据说是云计算的未来，利用Mesos可轻易实现一个PaaS云平台。

请参考 `http://mesos.apache.org/`和`http://mesosphere.com/`查看详细

# 2 Master节点安装

## 添加 mesos的yum源

```
sudo rpm -Uvh http://repos.mesosphere.io/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm
```

## 配置hosts

`vi /etc/hosts`

增加：

```
192.168.1.110 master1
192.168.1.111 master2
192.168.1.112 master3
```


## 安装mesos及相关软件

```
yum -y install mesos marathon
yum -y install mesosphere-zookeeper
```

## 配置zookeeper

```
touch /var/lib/zookeeper/myid
echo 1 > /var/lib/zookeeper/myid
```

`vi /etc/zookeeper/conf/zoo.cfg`

```
server.1=192.168.1.110:2888:3888
server.2=192.168.1.111:2888:3888
server.3=192.168.1.112:2888:3888
```

## 配置mesos


`vi /etc/mesos/zk`
增加内容:
`zk://192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181/mesos`

`vi /etc/mesos-master/quorum`

增加内容：`2`



## 启动zookeeper mesos-master marathon

```
systemctl start  zookeeper && systemctl start mesos-master && systemctl start marathon 
```

## 关闭防火墙

```
systemctl stop firewalld && systemctl disable firewalld
```

# 3 Slave节点安装

## 添加 mesos的yum源

```
sudo rpm -Uvh http://repos.mesosphere.io/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm
```

## 配置hosts

`vi /etc/hosts`

增加

```
192.168.1.110 master1
192.168.1.111 master2
192.168.1.112 master3
```

## 安装mesos

`yum -y install mesos`

## 配置master信息

`vi /etc/mesos/zk`

增加

```
zk://192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181/mesos
```

## 启动mesos-slave

```
systemctl start  mesos-slave 
```

## 关闭防火墙

```
systemctl stop firewalld && systemctl disable firewalld
```

# 4 测试

- 访问 http://192.168.1.110:5050 mesos地址
- 访问 http://192.168.1.110:8080 marathon地址，可利用其发布docker镜像并可调整应用数量





