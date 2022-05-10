## 组件版本关系


| Spring Cloud Alibaba Version                              | Sentinel Version | Nacos Version | RocketMQ Version | Dubbo Version | Seata Version |
| ----------------------------------------------------------- | ------------------ | --------------- | ------------------ | --------------- | --------------- |
| 2021.0.1.0*                                               | 1.8.3            | 1.4.2         | 4.9.2            | 2.7.15        | 1.4.2         |
| 2.2.7.RELEASE                                             | 1.8.1            | 2.0.3         | 4.6.1            | 2.7.13        | 1.3.0         |
| 2.2.6.RELEASE                                             | 1.8.1            | 1.4.2         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2021.1 or 2.2.5.RELEASE or 2.1.4.RELEASE or 2.0.4.RELEASE | 1.8.0            | 1.4.1         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2.2.3.RELEASE or 2.1.3.RELEASE or 2.0.3.RELEASE           | 1.8.0            | 1.3.3         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2.2.1.RELEASE or 2.1.2.RELEASE or 2.0.2.RELEASE           | 1.7.1            | 1.2.1         | 4.4.0            | 2.7.6         | 1.2.0         |
| 2.2.0.RELEASE                                             | 1.7.1            | 1.1.4         | 4.4.0            | 2.7.4.1       | 1.0.0         |
| 2.1.1.RELEASE or 2.0.1.RELEASE or 1.5.1.RELEASE           | 1.7.0            | 1.1.4         | 4.4.0            | 2.7.3         | 0.9.0         |
| 2.1.0.RELEASE or 2.0.0.RELEASE or 1.5.0.RELEASE           | 1.6.3            | 1.1.1         | 4.4.0            | 2.7.3         | 0.7.1         |

## 毕业版本依赖关系(推荐使用)


| Spring Cloud Alibaba Version      | Spring Cloud Version | Spring Boot Version |
| ----------------------------------- | ---------------------- | --------------------- |
| 2021.0.1.0                        | 2021.0.1             | 2.6.3               |
| 2.2.7.RELEASE                     | Hoxton.SR12          | 2.3.12.RELEASE      |
| 2021.1                            | 2020.0.1             | 2.4.2               |
| 2.2.6.RELEASE                     | Hoxton.SR9           | 2.3.2.RELEASE       |
| 2.1.4.RELEASE                     | Greenwich.SR6        | 2.1.13.RELEASE      |
| 2.2.1.RELEASE                     | Hoxton.SR3           | 2.2.5.RELEASE       |
| 2.2.0.RELEASE                     | Hoxton.RELEASE       | 2.2.X.RELEASE       |
| 2.1.2.RELEASE                     | Greenwich            | 2.1.X.RELEASE       |
| 2.0.4.RELEASE(停止维护，建议升级) | Finchley             | 2.0.X.RELEASE       |
| 1.5.1.RELEASE(停止维护，建议升级) | Edgware              | 1.5.X.RELEASE       |

## 依赖管理

Spring Cloud Alibaba BOM 包含了它所使用的所有依赖的版本。

### RELEASE 版本

#### Spring Cloud 2020

如果需要使用 Spring Cloud 2020 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2021.1</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### Spring Cloud Hoxton

如果需要使用 Spring Cloud Hoxton 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.6.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### Spring Cloud Greenwich

如果需要使用 Spring Cloud Greenwich 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.1.4.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### Spring Cloud Finchley

如果需要使用 Spring Cloud Finchley 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.0.4.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### Spring Cloud Edgware

如果需要使用 Spring Cloud Edgware 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>1.5.1.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

## 孵化器版本依赖关系(不推荐使用)


| Spring Cloud Version   | Spring Cloud Alibaba Version | Spring Boot Version |
| ------------------------ | ------------------------------ | --------------------- |
| Spring Cloud Greenwich | 0.9.0.RELEASE                | 2.1.X.RELEASE       |
| Spring Cloud Finchley  | 0.2.X.RELEASE                | 2.0.X.RELEASE       |
| Spring Cloud Edgware   | 0.1.X.RELEASE                | 1.5.X.RELEASE       |


| Note | 请注意， Spring Cloud Edgware 最低支持 Edgware.SR5 版本 |
| ------ | --------------------------------------------------------- |
|      |                                                         |

## 依赖管理

Spring Cloud Alibaba BOM 包含了它所使用的所有依赖的版本。

### RELEASE 版本

#### Spring Cloud Greenwich

如果需要使用 Spring Cloud Greenwich 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>0.9.0.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### Spring Cloud Finchley

如果需要使用 Spring Cloud Finchley 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>0.2.2.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### Spring Cloud Edgware

如果需要使用 Spring Cloud Edgware 版本，请在 dependencyManagement 中添加如下内容

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>0.1.2.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```
