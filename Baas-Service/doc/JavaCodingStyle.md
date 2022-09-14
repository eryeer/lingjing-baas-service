**《Java开发手册》**
[TOC]

# 1. 编程规约
## 1.1. 命名规约
### 1.1.1. 【强制】代码中的命名
+ 类名使用UpperCamelCase风格，必须遵从驼峰形式。
+ 方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
+ 常量命名全部大写，单词间用下划线隔开，力求予以表达完整清楚，不要嫌名字长

### 1.1.2. 【推荐】包命名规则
包的命名全部以com.onchain为开头，并根据存放类的功能指定末位字段的命名，以下列出常用包命名：
```
com.onchain.controller  
com.onchain.rest
com.onchain.service
com.onchain.mapper
com.onchain.pojo
com.onchain.util
com.onchain.config
com.onchain.listener
com.onchain.const
```

## 1.2. OOP规约
### 1.2.1. 【强制】关于基本数据类型（如int）与包装数据类型(如Integer)的使用标准如下：
+ 所有的Pojo类属性必须使用包装数据类型。
+ Rest方法的返回值和参数必须使用包装数据类型。
+ 所有的局部变量【推荐】使用基本数据类型。

### 1.2.2. Pojo类的toString方法规范如下：
+ 【强制】所有pojo类必须添加lombok @Data注解。


### 1.2.3.  
【推荐】Object的equals方法容易出现空指针异常，推荐使用org.apache.commons.lang3.StringUtils的equals方法。
## 1.3. 常量规约
### 1.3.1.  
【强制】除了controller方法上@RequestMapping的url，不允许任何未经定义（魔鬼字符）的常量直接出现在代码中,如：
```
// 禁止
String key = “ID#taotao_”+tradeId;
```
### 1.3.2.  
【强制】每一个微服务模块应有自己的常量类、负责维护自己模块的常量引用，不能把非模块之间均引用常量都写到base模块的常量类中。
【强制】 通用的入参pojo类需要写到base模块中。

## 1.4. Controller层规约
### 1.4.1.  
【强制】Controller层的工作主要为参数校验、异常处理以及返回参数装填。
### 1.4.2.  
【强制】如果Controller用于接收来自Gateway的开放请求，则需要将该Controller归类到controller包下；如果Controller用于接收来自内部其他微服务的请求，则需要将该Controller归类到rest包下。
### 1.4.3.  
【强制】开放接口（controller接口）和内部接口（rest接口）的异常必须在controller处理，并返回封装了相应的返回码和参数的ResponseFormat对象。可使用ControllerAdvice进行全局异常抓取。
### 1.4.4.  
【强制】处理来自客户端或第三方的请求之前，需要校验请求参数对象中的token的合法性。
### 1.4.5.  
【强制】接收、返回的参数中如果包含时间，必须使用时间戳形式传递。
### 1.4.6.  
【强制】所有Controller方法都要添加swaggerAPI注解，value和notes属性描述保持一致。 
例如：@ApiOperation(value = CommonConst.modifySCEInfo, notes = CommonConst.modifySCEInfo)
【强制】所有入参pojo类属性必须添加swaggerAPI注解，详细描述入参说明。
### 1.4.7. .
【强制】所有Controller方法都要添加操作日志拦截器注解。（上传文件除外）  
例如：@OperLogAnnotation(description = CommonConst.modifySCEInfo)
### 1.4.8.  
【强制】调用本地其他微服务的请求应为POST请求，传递参数应为对象。
### 1.4.9.  
【强制】考虑到URL共用问题，Controller中不能在类上声明根路径，全路径在方法上声明，根路径名称需和Controller的类名相对应，如Controller类名为UserController，则根路径为```/user```。
### 1.4.10. .
【强制】Controller接收的入参bean类应尽量不出现前端不传递的参数字段，如果出现，需要添加swagger注解 hidden = true。
【强制】Controller接收的入参bean类命名格式为“Request”+方法名。
### 1.4.11.  
【推荐】推荐使用@RestController、@GetMapping、@PostMapping注解。

## 1.5. Service层规约
### 1.5.1.  
【强制】Service层的工作主要为业务逻辑处理和事务管理。
### 1.5.2.  
【强制】任何校验、检查不通过的情形，都应以异常抛出的形式结束方法。
### 1.5.3.  
【强制】无特殊情况不要在Service层处理异常，向上抛出即可，异常处理统一由Controller层执行。
### 1.5.4. 【强制】事务处理规范如下：
+ 所有的事务处理必须在service层执行。
+ @Transactional注解默认只回滚RuntimeException，如果希望抛出声明式异常时也进行回滚，则注解通用写法为@Transactional(rollbackFor = Exception.class)。这是一个大坑，要小心！
### 1.5.5.  
【推荐】 当DAO层查询结果为一个List对象时，如果对此对象进行空值或非空判断，必须同时判断是否为null和size大小。
例如
List<User> users = userDao.getUsers();
if(users != null){...} //错误
if(users == null){...} //错误
if(users != null && users.size() != 0){...}  //正确
if(users == null || users.size() == 0){...}  //正确



## 1.6. Spring Cloud组件使用规约
### 1.6.1.  
【强制】每一个微服务必须注册到Eureka注册中心，注册中心将统一管理所有微服务地址。
### 1.6.2.  
【强制】每个rest接口调用必须使用Feign风格书写，每一个被调用的微服务模块都应对应一个FeignClient接口。
### 1.6.3.  
【强制】每个FeignClient接口必须指定fallback类，fallback类必须implement对应的FeignClient，并且override所有Feignclient方法。
### 1.6.4. 【强制】关于fallback方法返回值标准如下：
+ 如果fallback方法返回值类型为必须为FeignResponseFormat，并装填fallback的状态描述。  
### 1.6.5.  
【强制】使用FeignClinet调用完接口后，必须检查返回值是否为Hystrix回滚值，如果是，必须手动抛出异常。
### 1.6.6.  
【推荐】fallback类的命名应为feignClient接口名+```Hystrix```，如名为```UserFeignClient```的接口，其fallback类为```UserFeignClientHystrix```。

## 1.7. 日志规约
### 1.7.1.  
【强制】使用lombok注解@Slf4j 在类级别上使用。
### 1.7.2. .
【强制】打印warn和error级别日志时，描述中需要加上所属方法的方法名。
 例如：方法名为updateUserStatus的方法中的错误级别日志打印格式如下
 ```
 log.error("updateUserStatus:", e);
 ```
# 2. Mysql规约
## 2.1. 建表规约
### 2.1.1.  
【强制】表名、字段名必须使用小写字母或数字，禁止出现数字开头，大写字母开头，禁止两个下划线中间只出现数字。  
正例：getter_admin,task_config,level3_name
反例：GetterAdmin， taskConfig， level_3_name
### 2.1.2.  
【推荐】表名不使用复数名词。
### 2.1.3.  
【强制】表达是与否概念的字段必须使用is_xxx的方式命名，数据类型是tinyint（1表示是，0表示否） ，对应java的数据类型为Boolean。
### 2.1.4.  
【强制】禁用保留字，如desc、range、match、delayed等，请参考MySql官方保留字。
### 2.1.5.  
【强制】唯一索引名以_uk为结尾，普通索引名以_idx为结尾。
### 2.1.6.  
【强制】表必备字段：id，create_date，update_date，status。
说明：id为int类型的自增主键，create_date、update_date均为date_time类型，
status为varchar(32)类型。
## 2.2. 索引规约
### 2.2.1.  
【强制】业务上具有唯一特性的字段，即使是组合字段，也必须建成唯一索引。

## 2.3. 一般原则
除tmp表，不允许记录的硬删除。

# 3. 工程规约
## 3.1. 应用分层
### 3.1.1.  
【强制】应用遵从传统三层架构模型：控制层（Controller层）、服务层（service层）、持久层（DAO层），控制层可分为开放接口层（controller）和内部接口层（rest）。
