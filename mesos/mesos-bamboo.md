# 1 使用bamboo来实现负载均衡和服务发现

github地址:https://github.com/QubitProducts/bamboo

## 1.1 下载bamboo镜像并放置私有registry

```
docker pull gregory90/bamboo:0.2.11
docker tag 192.168.1.103:5000/bamboo
docker push 192.168.1.103:5000/bamboo
```

## 1.2 在任意局域网机器上


`docker pull 192.168.1.103：5000/bamboo`

运行此镜像

```
docker run -t -i -d -p 8000:8000 -p 80:80 \
    -e MARATHON_ENDPOINT=http://192.168.1.110:8080,http://192.168.1.111:8080,http://192.168.1.112:8080 \
    -e BAMBOO_ENDPOINT=http://192.168.1.113:8000 \
	-e BAMBOO_ZK_HOST=192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181 \
    -e CONFIG_PATH="config/production.example.json" \
    -e BAMBOO_DOCKER_AUTO_HOST=true \
    192.168.1.103:5000/bamboo
```

## 1.3 运行效果

访问发布baboo的机器:`http://ip:8000`

自动检测我们在marathon发布的docker程序
![](https://raw.githubusercontent.com/wiselyman/study/master/mesos/resources/baboo1.jpg)

## 1.4 修改程序映射地址

- 映射tomcat地址为`path_beg -i /`
  ![](https://raw.githubusercontent.com/wiselyman/study/master/mesos/resources/bamboo2.jpg)
- 映射spring boot 可执行web jar(demo)的地址为`path_beg -i /xx`
  ![](https://raw.githubusercontent.com/wiselyman/study/master/mesos/resources/bamboo3.jpg)


## 1.5 测试访问
- tomcat `http://ip`
![](https://raw.githubusercontent.com/wiselyman/study/master/mesos/resources/re1.jpg)
- demo `http://ip/xx`
![](https://raw.githubusercontent.com/wiselyman/study/master/mesos/resources/re2.jpg)
