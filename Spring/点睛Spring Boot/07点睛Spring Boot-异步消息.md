## 7.1 异步消息
- 请求者不是将信息直接发给被请求者,请求者无需等待被请求者的响应;

- Spring支持JMS(Java Message Service)和AMQP(Advanced Message Queuing Protocol)

- 消息发送支持点对点(point-to-point)和订阅发布式(publish-suscribe)

## 7.2 演示
### 7.2.1 JMS
- 使用程序内置的ActiveMQ server

![](resources/7-2.jpg)

- 使用`JmsTemplate`发送消息

- 使用`@JmsListener`接受消息

- 新建空的spring boot项目

- 新增jms及activemq的依赖

```
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jms</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-broker</artifactId>
</dependency>
```

- 编写接受消息方法

```
package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Receiver {
    @Autowired
    ConfigurableApplicationContext context;

    //从目的地mailbox-destination监听接受消息
    @JmsListener(destination = "mailbox-destination",
    				containerFactory = "wiselyJmsContainerFactory")
    public void receiveMessage(String message){
        System.out.println("接受到: <" + message + ">");
    }
}

```

- 在入口类中发送信息

```
package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@SpringBootApplication
public class JmsApplication {

    @Bean
    JmsListenerContainerFactory<?> wiselyJmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory =
        						new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    public static void main(String[] args) {
        // 启动程序
        ConfigurableApplicationContext context = 															SpringApplication.run(JmsApplication.class, args);
        // 发送消息
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("ping!");
            }
        };
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        System.out.println("发送信息:");
        //发送消息的目的地是mailbox-destination
        jmsTemplate.send("mailbox-destination", messageCreator);
    }
}

```

- 运行测试

![](resources/7-1.jpg)

### 7.2.2 AMQP
### 7.2.1 redis