CentOS7和fedora21使用firewalld作为防火墙。

# 查看防火墙状态

`firewall-cmd --state`

输出
```
running
```

# 查看活动区域并附带网络接口

`firewall-cmd --get-active-zones`

输出
```
FedoraWorkstation
  interfaces: enp3s0
```

# 通过网络接口查看对应的区域

`firewall-cmd --get-zone-of-interface=enp3s0`

输出
```
FedoraWorkstation
```

# 通过区域名获得所有网络接口

`firewall-cmd --zone=FedoraWorkstation --list-interfaces`

输出
```
enp3s0
```

# 通过区域名获得此区域的信息

`firewall-cmd --zone=FedoraWorkstation --list-all`

输出
```
FedoraWorkstation (default, active)
  interfaces: enp3s0
  sources:
  services: dhcpv6-client mdns samba-client ssh
  ports: 1025-65535/udp 1025-65535/tcp
  masquerade: no
  forward-ports:
  icmp-blocks:
  rich rules:
```

# 查看当前区域活动的services

`firewall-cmd --get-service`

输出
```
amanda-client amanda-k5-client bacula bacula-client dhcp dhcpv6 dhcpv6-client dns freeipa-ldap freeipa-ldaps freeipa-replication ftp high-availability http https imaps ipp ipp-client ipsec kadmin kerberos kpasswd ldap ldaps libvirt libvirt-tls mdns mountd ms-wbt mysql nfs ntp openvpn pmcd pmproxy pmwebapi pmwebapis pop3s postgresql privoxy proxy-dhcp puppetmaster radius rpc-bind samba samba-client sane smtp squid ssh synergy telnet tftp tftp-client tor-socks transmission-client vnc-server wbem-https xmpp-bosh xmpp-client xmpp-local xmpp-server
```

# 增加端口到防火墙中

`firewall-cmd --zone=FedoraWorkstation --add-port=8080/tcp --permanent`

`firewall-cmd --zone=FedoraWorkstation --add-port=5060-5061/udp --permanent`

删除

`firewall-cmd --zone=FedoraWorkstation --remove-port=8080/tcp`

`firewall-cmd --zone=FedoraWorkstation --remove-port=5060-5061/udp`




# 增加服务到防火墙中

`firewall-cmd --zone=FedoraWorkstation --add-service=smtp --permanent`

删除

`firewall-cmd --zone=FedoraWorkstation --remove-service=smtp`

