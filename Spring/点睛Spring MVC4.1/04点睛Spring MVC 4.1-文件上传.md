## 4.1 文件上传

- 在控制器参数使用`@RequestParam("file") MultipartFile file`接受单个文件上传;
- 在控制器参数使用`@RequestParam("file") MultipartFile[] files`接受多个文件上传;
- 通过配置`MultipartResolver`来配置文件上传的一些属性;

## 4.2 示例

- 增加和上传和文件操作的依赖到maven

```
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.3</version>
</dependency>
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.3.1</version>
</dependency>
```

- 上传控制器

```
package com.wisely.web;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	//接受多个文件上传使用@RequestParam("file") MultipartFile[] files
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	public @ResponseBody String upload(@RequestParam("file") MultipartFile file) {

			try {
				FileUtils.writeByteArrayToFile(new File("e:/"+file.getOriginalFilename()), file.getBytes());
				return "ok";
			} catch (IOException e) {
				e.printStackTrace();
				return "wrong";
			}


	}

}

```

- 文件上传所需配置

```
@Configuration
@ComponentScan("com.wisely")
@EnableWebMvc
public class DemoMVCConfig extends WebMvcConfigurerAdapter {

	@Bean
	public UrlBasedViewResolver viewResolver(){
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/views/")
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
	}
	//注册拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(demoInteceptor());
	}
    //自定义拦截器
	@Bean
	public DemoInteceptor demoInteceptor(){
		return new DemoInteceptor();
	}

	//静态资源映射
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }
	//文件上传设置
	@Bean
	public MultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(1000000);
	    return multipartResolver;
	}
}

```

- 页面代码
```
<form action="upload" enctype="multipart/form-data" method="post">
		<input type="file" name="file"/><br/>
		<input type="submit" value="上传">
	</form>
```