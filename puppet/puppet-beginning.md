# 简单使用

现在我们已经安装好了puppet server和puppet client，我们做个最简单的示例，让client的puppet开机自启动

## server端

`vi /etc/puppet/manifests/site.pp`

添加

```
node default {
        service { 'puppet':
                ensure => running,
                enable => true,
        }
}

```

## client 端

`puppet agent -t`或`puppet agent --test`

输出：

```
Info: Retrieving pluginfacts
Info: Retrieving plugin
Info: Caching catalog for agent1.example.net
Info: Applying configuration version '1428545563'
Notice: /Stage[main]/Main/Node[default]/Service[puppet]/ensure: ensure changed 'stopped' to 'running'
Info: /Stage[main]/Main/Node[default]/Service[puppet]: Unscheduling refresh on Service[puppet]
Notice: Finished catalog run in 0.10 seconds
```

设置开机自启动成功

## 关闭puppet并取消开机启动
同上，修改如下

```
service { 'puppet':
    ensure => stopped,
    enable => false,
}
```