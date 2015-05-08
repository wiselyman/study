## 1.1 声明bean
- spring利用**@Component,@Service,@Repository,@Controller**注解在一个java类上声明是spring容器的bean;
- 使用**@Component,@Service,@Repository,@Controller**任意一个在类上效果是等同的,不同的名称是为了更好的标志类的角色和功能,避免代码维护者混淆代码作用;
- 那我们使用这四个注解的准则是什么呢?
 - @Component 没有明确的角色,少用;
 - @Service 在业务逻辑层(service层)使用;
 - @Repository 在数据访问层(dao层)使用;
 - @Controller 在展现层(MVC->Spring MVC)使用;

## 1.2 注入bean
- 可以使用**@Autowired,@Inject(JSR-330),@Resource(JSR-250)**注入用**@Component,@Service,@Repository,@Controller**(当然包括xml声明的或者用@Bean声明的bean)声明的bean;
- **@Autowired,@Inject,@Resource**在一般情况下是通用的;
- **@Autowired,@Inject,@Resource**可注解在set方法上或者属性上;我习惯注解在属性上,代码更少层次更清晰;

## 1.3 示例