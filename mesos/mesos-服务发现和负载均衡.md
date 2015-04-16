# 配置mesos-dns


这里有编译好的mesos-dns，config.json复制到113机器上

在marathon ui上新建dns

command  `/usr/local/mesos-dns/mesos-dns --v=0 -config=/usr/local/mesos-dns/config.json`

contraints   `hostname:CLUSTER:192.168.1.113`