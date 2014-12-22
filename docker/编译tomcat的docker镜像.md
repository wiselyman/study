docker官方的registry上已有很多tomcat的image了，但是从它们的Dockerfile来看都是基于网络形式的。

本例使用本地文件编译，这样可以更大的定制tomcat，也练习下自己编译docker镜像。

# 1 文件结构
![](https://raw.githubusercontent.com/wiselyman/study/master/docker/resources/file.jpg)

# 2 Dockerfile

```
FROM ubuntu

#install jdk
ADD jdk1.6.0_32 /opt/jdk1.6.0_32
ENV JAVA_HOME /opt/jdk1.6.0_32
ENV PATH $PATH:$JAVA_HOME/bin
RUN mkdir -p "$JAVA_HOME"

#install tomcat
ADD apache-tomcat-6.0.41 /opt/apache-tomcat
ENV CATALINA_HOME /opt/apache-tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN mkdir -p "$CATALINA_HOME"
WORKDIR $CATALINA_HOME

EXPOSE 8080

CMD ["catalina.sh", "run"]
```

# 编译

`docker build -t tomcat .`

# 运行

`docker run -d -p 27945:8080 tomcat`

# 访问

http://ip:27945

![](https://raw.githubusercontent.com/wiselyman/study/master/docker/resources/result.jpg)