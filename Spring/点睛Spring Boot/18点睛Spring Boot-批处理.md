## 18.1 Spring Batch
- **Spring Batch**是Spring用来作为批处理的框架;
- **Spring Batch**的核心是`Job`
- `Job`由`Step`组成
- `Step`主要的流程是`Reader`->`Processor`->`Writer`

## 18.2 演示
- 本章基于[Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)启发;
- 批量导入更加使用的excel数据到数据库;
- 支持不同数据类型的excel
- 本节相当于一个小型的项目,代码较多;
- 本章使用[spring-batch-excel](https://github.com/spring-projects/spring-batch-extensions/tree/master/spring-batch-excel)来做读excel文件的`PoiItemReader`
- **spring-batch-excel**扩展模块有2个问题
  - 默认第一行是标题(标题不一定在第一行)
  - 标题名字和映射的javabean名称对应(标题可能是汉字,那和javabean里的英文字符不能匹配,使用pinyin4将汉子转换成拼音与javabean映射)
  - 所以要修改**spring-batch-excel**



### 18.2.1 准备spring-batch-excel和pinyin4j

#### 18.2.1.1 下载
- `pinyin4j`依赖

```xml
<dependency>
	<groupId>com.belerweb</groupId>
	<artifactId>pinyin4j</artifactId>
	<version>2.5.0</version>
</dependency>

```
- 打开https://github.com/spring-projects/spring-batch-extensions
- git:`git clone git://github.com/spring-projects/spring-batch-extensions.git`
- 直接下载:直接点右侧`Download ZIP`

#### 18.2.1.2 修改源码
##### 18.2.1.2.1 使支持设置第几行是标题栏
- `org.springframework.batch.item.excel.support.rowset.RowNumberColumnNameExtractor`

```java
public class RowNumberColumnNameExtractor implements ColumnNameExtractor {

    private int headerRowNumber;

    @Override
    public String[] getColumnNames(final Sheet sheet) {
        PoiSheet poiSheet = (PoiSheet)sheet;
        return poiSheet.getHeaderRow(headerRowNumber);
    }

    public void setHeaderRowNumber(int headerRowNumber) {
        this.headerRowNumber = headerRowNumber;
    }
}

```

- `org.springframework.batch.item.excel.support.rowset.DefaultRowSetFactory`

```java
public class DefaultRowSetFactory implements RowSetFactory {

    private ColumnNameExtractor columnNameExtractor;

    @Override
    public RowSet create(Sheet sheet) {
        DefaultRowSetMetaData metaData = new DefaultRowSetMetaData(sheet);
        metaData.setColumnNameExtractor(columnNameExtractor);
        return new DefaultRowSet(sheet, metaData);
    }

    public void setColumnNameExtractor(ColumnNameExtractor columnNameExtractor) {
        this.columnNameExtractor = columnNameExtractor;
    }
}

```


##### 18.2.1.2.2 使支持标题栏汉字转换成拼音和javabean映射
- 汉字转换拼音处理类

```java
package com.wisely.batchxls.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by wisely on 2015/5/29.
 */
public class Hanyu {
    private HanyuPinyinOutputFormat format = null;
    private String[] pinyin;
    public Hanyu() {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pinyin = null;
    }
    //转换单个字符
    public String getCharacterPinYin(char c) {
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
        }catch(BadHanyuPinyinOutputFormatCombination e){
            e.printStackTrace();
        }
        // 如果c不是汉字，toHanyuPinyinStringArray会返回null
        if(pinyin == null) return null;
        // 只取一个发音，如果是多音字，仅取第一个发音
        return pinyin[0];
    }
    //转换一个字符串
    public String getStringPinYin(String str){
        StringBuilder sb = new StringBuilder();
        String tempPinyin = null;
        for(int i = 0; i < str.length(); ++i){
            tempPinyin =getCharacterPinYin(str.charAt(i));
            if(tempPinyin == null){
                // 如果str.charAt(i)非汉字，则保持原样
                sb.append(str.charAt(i));
            }else{
                sb.append(tempPinyin);
            }
        }
        return sb.toString();

    }

    public  boolean isChinese(char a) {
        int v = (int)a;
        return (v >=19968 && v <= 171941);
    }
    public  boolean containsChinese(String s){
        if (null == s || "".equals(s.trim())) return false;
        for (int i = 0; i < s.length(); i++) {
            if (this.isChinese(s.charAt(i))) return true;
        }
        return false;
    }
}

```

- `org.springframework.batch.item.excel.poi.PoiSheet`
增加处理标题汉字转换成拼音
```java
 public String[] getHeaderRow(final int rowNumber) {
        final Row row = this.delegate.getRow(rowNumber);
        if (row == null) {
            return null;
        }
        final List<String> cells = new LinkedList<String>();

        for (int i = 0; i < getNumberOfColumns(); i++) {
            Cell cell = row.getCell(i);
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cells.add(String.valueOf(date.getTime()));
                    } else {
                        cells.add(String.valueOf(cell.getNumericCellValue()));
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cells.add(String.valueOf(cell.getBooleanCellValue()));
                    break;
                case Cell.CELL_TYPE_STRING:
                case Cell.CELL_TYPE_BLANK:
                    String val =cell.getStringCellValue();
                    Hanyu hanyu = new Hanyu();
                    if(hanyu.containsChinese(val)) {
                        //将汉字转换为拼音
                        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

                        val = hanyu.getStringPinYin(val);
                    }
                    cells.add(val);
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cells.add(getFormulaEvaluator().evaluate(cell).formatAsString());
                    break;
                default:
                    throw new IllegalArgumentException("Cannot handle cells of type " + cell.getCellType());
            }
        }
        return cells.toArray(new String[cells.size()]);
    }
```

#### 18.2.1.3 编译发布到maven 本地库

```cmd
cd spring-batch-extensions/spring-batch-excel
mvn clean package

```

### 18.2.2 数据准备
#### 18.2.2.1 excel
[Hotel.xls](resources/Hotel.xls)
[Bank.xls](resources/Bank.xls)
#### 18.2.2.1 数据库(oracle)

```sql
create table HOTEL
(
  id           NUMBER not null,
  mingcheng    VARCHAR2(20),
  fuzeren      VARCHAR2(20),
  minzu        VARCHAR2(20),
  shenfenzheng VARCHAR2(20),
  jingdu       NUMBER,
  weidu        NUMBER
)
```

```sql
create table BANK
(
  id           NUMBER not null,
  mingcheng    VARCHAR2(50),
  fuzeren      VARCHAR2(20),
  minzu        VARCHAR2(20),
  xingzhi      VARCHAR2(20),
  shenfenzheng VARCHAR2(20),
  jingdu       NUMBER,
  weidu        NUMBER
)
```

### 18.2.3 构建spring Boot项目并增加依赖

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.wisely</groupId>
	<artifactId>batchxls</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>batchxls</name>
	<description>batch xls project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.2.3.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<start-class>com.wisely.batchxls.BatchxlsApplication</start-class>
		<java.version>1.7</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-batch</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.hsqldb</groupId>
					<artifactId>hsqldb</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
	   </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.batch</groupId>
			<artifactId>spring-batch-excel</artifactId>
			<version>0.5.0-SNAPSHOT</version>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi</artifactId>
			<version>3.11</version>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>3.11</version>
		</dependency>
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>2.4</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-redis</artifactId>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>5.1.3.Final</version>
	   </dependency>
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc6</artifactId>
			<version>11.2.0.1.0</version>
		</dependency>

		<dependency>
			<groupId>com.belerweb</groupId>
			<artifactId>pinyin4j</artifactId>
			<version>2.5.0</version>
		</dependency>


		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```

### 18.2.4 自定义注解
- `ExcelInfo`:配置忽略不读的行数,标题行数是其减去1

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelInfo {
     int skipLines();
}

```

- 注解`ExcelInfo`的处理类

```java
package com.wisely.batchxls.utils;

import com.wisely.batchxls.annotation.ExcelInfo;
import com.wisely.batchxls.annotation.Nation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ExcelInfoUtils {
    /**
     *
     * @param clazz 当前excel对应的bean的类型
     * @return
     */
    public static Integer getExcelInfo(Class clazz){
        ExcelInfo excelInfo = (ExcelInfo) clazz.getAnnotation(ExcelInfo.class);
        Integer skipLines = excelInfo.skipLines();
        return skipLines;
    }



}

```

- `Nation`：配置自动转换民族汉字为编码存到数据库

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Nation {
}

```

### 18.2.5 准备excel和对应javabean

- 将上面2个文件拷贝到`resources`目录下
- `Hotel`

```java
package com.wisely.batchxls.beans;

import com.wisely.batchxls.annotation.ExcelInfo;
import com.wisely.batchxls.annotation.Nation;

import javax.validation.constraints.Pattern;


@ExcelInfo(skipLines = 2)
public class Hotel {
    private String mingcheng;
    private String fuzeren;
    @Nation
    private String minzu;
    @Pattern(regexp="(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")
    private String shenfenzheng;
    private Float jingdu;
    private Float weidu;
 //getter,setter
}

```

- `Bank`

```java
package com.wisely.batchxls.beans;

import com.wisely.batchxls.annotation.ExcelInfo;
import com.wisely.batchxls.annotation.Nation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ExcelInfo(skipLines = 3)
public class Bank {
    private String mingcheng;
    private String fuzeren;
    @Nation
    private String minzu;
    private String xingzhi;
    @Pattern(regexp="(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")
    private String shenfenzheng;
    private Float jingdu;
    private Float weidu;
	//getter,setter
}

```

### 18.2.6 主要编码与配置
#### 18.2.6.1 全局配置
- `application.properties`
```java
spring.datasource.driverClassName=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc\:oracle\:thin\:@192.168.1.103\:1521\:xe
spring.datasource.username=wyf
spring.datasource.password=wyf
spring.datasource.continueOnError=true
spring.batch.job.enabled=false #Job不在程序启动的时候自动运行
```
#### 18.2.6.2 编写数据转换处理器

- 这里演示将民族转换为编码,如将汉族转换为01

```java
package com.wisely.batchxls.transformer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Transformer {
    @Bean
    public Map<String, String> nationMap() {
        Map<String, String> map = new HashMap<>();
        map.put("汉族", "01");
        map.put("回族", "02");
        map.put("满族", "03");
        map.put("蒙古族", "04");
        map.put("仫佬族", "05");
        map.put("壮族", "06");
        map.put("神族", "07");
        return map;
    }
}

```

```java
package com.wisely.batchxls.config;

import com.wisely.batchxls.annotation.Nation;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;


public class WiselyItemProcessor extends ValidatingItemProcessor{

    @Resource//注意这里要用@Resource才能注入
    Map<String,String> nationMap;

    @Override
    public Object process(Object item) throws ValidationException {
        super.process(item);
        Object transformMinzu = transformMinzu(item);
        //此处添加更多转换
        return transformMinzu;
    }

    public Object transformMinzu(Object item){
        Field[] fields = item.getClass().getDeclaredFields();
        for(Field f:fields){
            if(f.isAnnotationPresent(Nation.class)){
                f.setAccessible(true);
                try {
                    String origin = (String) f.get(item);
                    f.set(item, nationMap.get(origin));
                }catch (Exception e){

                }
            }
        }
        return item;
    }
}
```

#### 18.2.6.3 编写数据校验器
- 和`hibernate-validator`(jsr 303)配合使用;
- 出现校验错误,数据将不会插入到数据库;

```java
package com.wisely.batchxls.config;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;


public class WiselyBeanValidator implements Validator,InitializingBean {
    private javax.validation.Validator validator;
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Override
    public void validate(Object value) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(value);
        if(constraintViolations.size()>0){
            buildValidationException(constraintViolations);

        }


    }

    private void buildValidationException(Set<ConstraintViolation<Object>> constraintViolations){
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            message.append(constraintViolation.getMessage() + "\n");
        }
        throw new ValidationException(message.toString());
    }
}

```
#### 18.2.6.4 编写Job执行监听器

```java
package com.wisely.batchxls.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;


public class WiselyJobListener implements JobExecutionListener{
    long startTime;
    long endTime;
    @Override
    public void beforeJob(JobExecution jobExecution) {
        String fileName = jobExecution.getJobParameters().getString("input.file.name");
        startTime = System.currentTimeMillis();
        System.out.println(fileName + "任务处理开始");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        String fileName = jobExecution.getJobParameters().getString("input.file.name");
        System.out.println(fileName+"任务处理结束");
        System.out.println("耗时:" + (endTime - startTime) + "ms");
    }
}

```
#### 18.2.6.5 配置Job,Step,Reader,Writer,Processor

- 任务处理的参数需后置绑定
- 在处理开始时将参数保存于`jobParameters`中
- 在使用时候应注意
  - Bean的scope设置为`@StepScope`才能回去值；
  - 通过`@Value("#{jobParameters['skipLines']}" )Long skipLines`形式获取;

```java
package com.wisely.batchxls.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.excel.RowCallbackHandler;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.excel.support.rowset.*;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class ExcelBatchConfig {

    /**
     *
     * @param pathToFile excel文件位置
     * @param targetType excel映射的javabean类型
     * @param skipLines 忽略几行之后,开始是真正的数据
     * @return
     * @throws Exception
     */

    @Bean
    @StepScope
    public PoiItemReader reader(@Value("#{jobParameters['input.file.name']}") String pathToFile,
                                @Value("#{jobParameters['targetType']}" )String targetType,
                                @Value("#{jobParameters['skipLines']}" )Long skipLines
                                ) throws Exception{
        PoiItemReader reader = new PoiItemReader();
        reader.setLinesToSkip(skipLines.intValue());
        reader.setResource(new ClassPathResource(pathToFile));
        reader.setRowMapper(rowMapper(targetType));
        reader.setRowSetFactory(rowSetFactory(skipLines));
        return reader;
    }



    @Bean
    @StepScope
    public RowSetFactory rowSetFactory(@Value("#{jobParameters['skipLines']}" )Long skipLines){
        Long titleLine = skipLines-1;
        DefaultRowSetFactory rowSetFactory =  new DefaultRowSetFactory();
        RowNumberColumnNameExtractor columnNameExtractor = new RowNumberColumnNameExtractor();
        columnNameExtractor.setHeaderRowNumber(titleLine.intValue());
        rowSetFactory.setColumnNameExtractor(columnNameExtractor);
        return rowSetFactory;
    }



    @Bean
    @StepScope
    public RowMapper rowMapper(@Value("#{jobParameters['targetType']}" )String targetType) throws Exception{
        BeanWrapperRowMapper rowMapper = new BeanWrapperRowMapper();
        rowMapper.setTargetType(Class.forName(targetType));
        return rowMapper;
    }



        @Bean
    public ItemProcessor processor() {
        WiselyItemProcessor processor =  new WiselyItemProcessor();
        processor.setValidator(beanValidator());
        return processor;
    }
    @Bean
    public Validator beanValidator(){
        return new WiselyBeanValidator();
    }

    @Bean
    @StepScope
    public ItemWriter writer(DataSource dataSource,
                                     @Value("#{jobParameters['sql']}") String sql) {
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JobRepository jobRepository(DataSource dataSource,
                                       PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType("oracle");
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(DataSource dataSource,
                                         PlatformTransactionManager transactionManager) throws Exception{
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository(dataSource,transactionManager));
        return jobLauncher;
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .listener(new WiselyJobListener())
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader reader,
                      ItemWriter writer, ItemProcessor processor) {
        return stepBuilderFactory.get("step1")
                .chunk(65000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}

```

### 18.2.7 演示控制器

-`SqlUtils`:根据javabean自动生成批量插入的sql语句

```java
package com.wisely.batchxls.utils;

import java.lang.reflect.Field;

public class SqlUtils {

    public static String generate(Class clazz,String table){
        Field[] fields = clazz.getDeclaredFields();
        String prefix = "insert into "+ table;
        String cols = " (id,";
        String vals = " values("+table+"_SEQ.nextval,";

        for(int i =0;i<fields.length;i++){
            if(i==fields.length-1){
                cols = cols + fields[i].getName() + ")";
                vals = vals + ":" + fields[i].getName() + ")";
            }else {
                cols = cols + fields[i].getName() + ",";
                vals = vals + ":" + fields[i].getName() + ",";
            }
        }


        return prefix + cols + vals;

    }
}

```

- 演示控制器

```java
package com.wisely.batchxls.web;

import com.wisely.batchxls.beans.Bank;
import com.wisely.batchxls.utils.ExcelInfoUtils;
import com.wisely.batchxls.utils.SqlUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;

/**
 * Created by wisely on 2015/5/27.
 */
@Controller
public class DemoController {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importUserJob;
    public JobParameters   jobParameters;
    @RequestMapping("/read")
    @ResponseBody
    public String export(@RequestParam String xls) throws Exception{
        String path = xls+".xls";
        String targetType = "com.wisely.batchxls.beans."+xls;
        Class clazz = Class.forName(targetType);
        String sql = SqlUtils.generate(clazz,xls);
        Integer skipLines = ExcelInfoUtils.getExcelInfo(clazz);


        jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("input.file.name", path)
                .addString("sql",sql)
                .addString("targetType",clazz.getName())
                .addLong("skipLines",skipLines.longValue())

                .toJobParameters();
        jobLauncher.run(importUserJob,jobParameters);
        return "ok";
    }

}

```



