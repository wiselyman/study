A SpringApplication will attempt to create the right type of ApplicationContext
on your behalf. By default, an AnnotationConfigApplicationContext or
AnnotationConfigEmbeddedWebApplicationContext will be used, depending on whether you
are developing a web application or not.