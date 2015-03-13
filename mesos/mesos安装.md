# Master Nodes Setup
```
sudo rpm -Uvh http://repos.mesosphere.io/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm
```

```
yum -y install mesos marathon
yum -y install mesosphere-zookeeper
```

```
touch /var/lib/zookeeper/myid
echo 1 > /var/lib/zookeeper/myid
```

```
vi /etc/zookeeper/conf/zoo.cfg
server.1=192.168.1.110:2888:3888
server.2=192.168.1.111:2888:3888
server.3=192.168.1.112:2888:3888
```
```
sudo systemctl start zookeeper
```

```
vi /etc/mesos/zk
zk://192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181/mesos
```

```
vi /etc/mesos-master/quorum
2
```

```
systemctl stop mesos-slave.service
systemctl disable mesos-slave.service
```
```
vi /etc/hosts
192.168.1.110 master1
192.168.1.111 master2
192.168.1.112 master3
```
```
systemctl restart zookeeper && systemctl restart mesos-master && systemctl restart marathon 
```

# Slave Node Setup
```
vi /etc/mesos/zk
zk://192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181/mesos
```

```
systemctl stop mesos-master.service
systemctl disable mesos-master.service
```

```
systemctl restart mesos-slave 
```

```
vi /etc/hosts
192.168.1.113               localhost localhost.localdomain localhost4 localhost4.localdomain4
::1                     localhost localhost.localdomain localhost6 localhost6.localdomain6
192.168.1.110 master1
192.168.1.111 master2
192.168.1.112 master3
```

```
systemctl stop firewalld && systemctl disable firewalld
```


