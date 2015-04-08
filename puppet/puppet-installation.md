# 1 准备

- 2台centos7(master:192.168.1.103 client:192.168.1.106)
- 分别执行`http://yum.puppetlabs.com/el/7/products/x86_64/puppetlabs-release-7-11.noarch.rpm`

# 2 Master

## 设置hostname

`nmtui`->`set system hostname`->`puppet.example.net`

## 设置hosts

`vi /etc/hosts`

增加

`192.168.1.103 puppet.example.net`

## 安装puppet server

`yum -y install puppet puppet-server`

## 配置puppet master

`/etc/puppet/puppet.conf`
添加
```
[master]
certname = puppet.example.net
```

## 启动puppetmaster

`systemctl start puppetmaster`

`systemctl enable puppetmaster`

# 3 Client

## 设置hostname

`nmtui`->`set system hostname`->`agent1.example.net`

## 设置hosts

`vi /etc/hosts`

增加

```
192.168.1.106 agent1.example.net
192.168.1.103 puppet.example.net
```

## 安装puppet agent

`yum -y install puppet puppet-server`


# 测试

## client执行：`puppet agent --test`

输出

```
Info: Creating a new SSL key for agent1.example.net
Info: Caching certificate for ca
Info: csr_attributes file loading from /etc/puppet/csr_attributes.yaml
Info: Creating a new SSL certificate request for agent1.example.net
Info: Certificate Request fingerprint (SHA256): DB:21:15:C8:90:E4:2D:54:53:4C:A5:9A:4A:00:50:E7:99:5B:73:EE:0C:23:F9:7B:36:99:34:CD:FE:E6:DF:DA
Info: Caching certificate for ca
Exiting; no certificate found and waitforcert is disabled

```

最后一行不是错误

## master执行:
- `puppet cert list` 输出:

```
"agent1.example.net" (SHA256) 14:7D:AA:34:C8:F1:70:28:B9:51:A6:7D:94:3F:69:92:8F:61:94:17:7D:4A:EF:F2:44:CC:4A:BC:6B:D5:C3:EC
```
- `puppet cert sign agent1.example.net`或者`puppet cert sign --all`

  输出:
  ```
Notice: Signed certificate request for agent1.example.net
Notice: Removing file Puppet::SSL::CertificateRequest agent1.example.net at '/var/lib/puppet/ssl/ca/requests/agent1.example.net.pem'

## client再次执行

`puppet agent --test`

输出：

```
Info: Caching certificate for agent1.example.net
Info: Caching certificate_revocation_list for ca
Info: Caching certificate for agent1.example.net
Info: Retrieving pluginfacts
Info: Retrieving plugin
Info: Caching catalog for agent1.example.net
Info: Applying configuration version '1428474782'
Info: Creating state file /var/lib/puppet/state/state.yaml
Notice: Finished catalog run in 0.05 seconds
```
 


