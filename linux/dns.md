# 1 准备

准备一台安装有centos/fedora的机器（192.168.1.103），本例基于Centos7

# 1.1 安装bind9

`yum -y install bind bind-utils`

# 2 配置

## 2.1 配置DNS Server

`vi /etc/named.conf`


```
options {
        listen-on port 53 { 127.0.0.1; 192.168.1.103;}; ##注意此处
        listen-on-v6 port 53 { ::1; };
        directory       "/var/named";
        dump-file       "/var/named/data/cache_dump.db";
        statistics-file "/var/named/data/named_stats.txt";
        memstatistics-file "/var/named/data/named_mem_stats.txt";
        allow-query     { localhost;192.168.1.0/24; }; ##注意此处

        recursion yes;

        dnssec-enable yes;
        dnssec-validation yes;
        dnssec-lookaside auto;

        /* Path to ISC DLV key */
        bindkeys-file "/etc/named.iscdlv.key";

        managed-keys-directory "/var/named/dynamic";

        pid-file "/run/named/named.pid";
        session-keyfile "/run/named/session.key";
};

logging {
        channel default_debug {
                file "data/named.run";
                severity dynamic;
        };
};

zone "." IN {
        type hint;
        file "named.ca";
};

##注意下面

zone "example.home" IN {
        type master;
        file "forward.example";
        allow-update{ none; };
};

##注意下面

zone "1.168.192.in-addr.arpa" IN {
        type master;
        file "reverse.example";
        allow-update{ none; };
};

include "/etc/named.rfc1912.zones";
include "/etc/named.root.key";
```

## 2.2 创建Zone文件

局域网此时有2台机器，192.168.1.102（映射域名为wisely.example.home），192.168.1.106（映射域名为puppet.example.home）

### 2.2.1 创建forward zone文件

`vi /var/named/forward.example `

添加

```
$TTL 86400
@ IN  SOA     dns.example.home. root.example.home. (
        2011071001  ;Serial
        3600        ;Refresh
        1800        ;Retry
        604800      ;Expire
        86400       ;Minimum TTL
)
@       IN  NS          dns.example.home.
@       IN  A           192.168.1.103
@       IN  A           192.168.1.106
@       IN  A           192.168.1.102
dns       IN  A   192.168.1.103
puppet       IN  A   192.168.1.106
wisely   IN A   192.168.1.102
```

### 2.2.2 创建reverse zone文件

`vi /var/named/reverse.example`

添加

```
$TTL 86400
@ IN  SOA     dns.example.home. root.example.home. (
        2011071001  ;Serial
        3600        ;Refresh
        1800        ;Retry
        604800      ;Expire
        86400       ;Minimum TTL
)
@       IN  NS          dns.example.home.
@       IN  PTR         example.home.
dns          IN  A   192.168.1.103
puppet          IN  A   192.168.1.106
wisely     IN A 192.168.1.102
103     IN  PTR         dns.example.home.
106     IN  PTR         puppet.example.home.
102     In  PTR         wisely.example.home.
```

### 2.2.3检查配置

`named-checkconf /etc/named.conf`

`named-checkzone example.home /var/named/forward.example`

`named-checkzone example.home /var/named/reverse.example `

### 2.2.4 关闭防火墙

`systemctl stop firewalld && systemctl disable firewalld`

### 2.2.5 启动dns服务器并保持开机自启

`systemctl start named && systemctl enable named`

# 3 使用

在局域网里的要使用这些域名的电脑上将dns的地址设置为192.168.1.103

`ping wisely.example.home`

可得到来自103的response