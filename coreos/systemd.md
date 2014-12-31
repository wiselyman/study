systemd是一个init系统，提供了启动、停止和管理进程。现在新的Linux发行版都支持systemd（Ubuntu不支持）

systemd包含两个主要概念：unit和target

- unit：描述进程如何处理的配置文件；
- target：让一组进程开始运行的分组机制。