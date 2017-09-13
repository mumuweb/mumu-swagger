# mumu-swagger spring集成swagger2
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/mumudemo/mumu-kafka/blob/master/LICENSE) 
[![Maven Central](https://img.shields.io/maven-central/v/com.weibo/motan.svg?label=Maven%20Central)](https://github.com/mumuweb/mumu-swagger) 
[![Build Status](https://travis-ci.org/mumuweb/mumu-swagger.svg?branch=master)](https://travis-ci.org/mumuweb/mumu-swagger)
[![codecov](https://codecov.io/gh/mumuweb/mumu-swagger/branch/master/graph/badge.svg)](https://codecov.io/gh/mumuweb/mumu-swagger)
[![OpenTracing-1.0 Badge](https://img.shields.io/badge/OpenTracing--1.0-enabled-blue.svg)](http://opentracing.io)

***Swagger is the world’s largest framework of API developer tools for the OpenAPI Specification(OAS), enabling development across the entire API lifecycle, from design and documentation, to test and deployment.***

## swagger简介
> Swagger 是一款RESTFUL接口的文档在线自动生成+功能测试功能软件。本文简单介绍了在项目中集成swagger的方法和一些常见问题。如果想深入分析项目源码，了解更多内容，见参考资料。  
> Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。总体目标是使客户端和文件系统作为服务器以同样的速度来更新。文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。Swagger 让部署管理和使用功能强大的API从未如此简单。

## swagger 集成
**引入maven**
```
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.7.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```
**springMVC配置**
```
<!-- swagger 映射地址 -->
<mvc:resources location="classpath:/META-INF/resources/" mapping="swagger-ui.html"/>
<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
<!-- 引用Swagger 默认配置 -->
<bean class="springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration" id="swagger2Config"/>
```

**swagger配置类**
```
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = {"com.lovecws.mumu.swagger.controller"})
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("用户管理")
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lovecws.mumu.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring 中使用Swagger2构建RESTful APIs")
                .description("更多babymm相关文章请关注：http://www.chuasi.com/")
                .termsOfServiceUrl("http://www.chuasi.com/")
                .contact("baby慕慕")
                .version("1.0")
                .build();
    }
}
```
**如果要是springBoot集成swagger2的时候 可以不要@EnableWebMvc注解，但是当spring集成swagger2的时候需要该配置，要不然会报错**

**controller编写**
```
@RestController
@RequestMapping(value = "/users")
@Api(value = "用户controller", tags = {"用户操作接口"}, description = "用户操作接口")
public class UserController {
    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @ApiOperation(value = "获取用户列表", notes = "")
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public List<User> getUserList() {
        List<User> r = new ArrayList<User>(users.values());
        return r;
    }

    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return "success";
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return users.get(id);
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @RequestBody User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }

    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        users.remove(id);
        return "success";
    }

    public static class User {
        private String name;
        private int age;
        private Long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
```

**运行效果**  
![运行效果图1](https://github.com/mumuweb/mumu-swagger/blob/master/docs/img/swagger1.png)  
![运行效果图2](https://github.com/mumuweb/mumu-swagger/blob/master/docs/img/swagger1.png)

查看文档详情，请点击[https://mumuweb.github.io/mumu-swagger/](https://mumuweb.github.io/mumu-swagger/)
## 相关阅读  
[swagger源码](https://github.com/swagger-api)   
[swagger官网地址](https://swagger.io/)   

## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**
