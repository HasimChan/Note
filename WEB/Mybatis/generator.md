

## MBG使用

generatorConfig.xml 模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
<!-- 具体配置内容 -->
</generatorConfiguration>
```

### 1. 三个配置内容标签

#### 1. properties（最多一个，可不配）

> 用于指定外部属性文件，指定后可用`${property}`引用属性值

属性（二选一）：

* resource：类路径
* url：文件路径

#### 2. classPathEntry（可多个，可不配）

#### 3. context（至少一个！！）

> 配置 MBG 环境（上下文）

属性：

* id：必选
* defaultModelType：如何生成实体类
  * conditional：默认值
  * flat：常用，一表一类
  * hierarchical
* targetRuntime
  * Mybatis3：默认值
  * Mybatis3Simple：不会生成Example相关方法

### 2. context 子标签

#### 1. property（0个或多个）

> 用于配置分隔符等设置

属性：

* autoDelimitKeywords：自动为关键字添加分隔符
* beginningDelimiter
* endingDelimiter

```xml
<context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">
    <property name="autoDelimitKeywords" value="true"/>
    <property name="beginningDelimiter" value="`"/>
    <property name="endingDelimiter" value="`"/>
</context>
```

#### 2. plugin（0个或多个）

#### 3. commentGenerator（0个或1个）

> 用于配置如何生成注释信息

属性：

* suppressAllcomments：阻止生成注释，默认为false.
* suppressDate：阻止生成的注释包含时间戳，默认为false.
* addRemarkComments：注释是否添加数据库表的备注信息，默认为false

#### 4. jdbcConnection（1个）

> 用于指定要连接的数据库信息

#### 5. javaTypeResolver（0个或1个）

> 用于指定 JDBC 类型与 Java 类型如何转换

#### 6. javaModelGenerator（1个）

> 用于控制生成的实体类

必选属性：

* targetPackage：生成实体类存放的包名。一般就是放在该包下，实际还会受到其他配置的影响。
* targetProject：指定目标项目路径，可以使用相对路径或绝对路径。

#### 7. sqlMapGenerator（0个或1个）

> 用于配置 xml 映射文件

#### 8. javaclientGenerator（0个或1个）

> 用于配置 Mapper 接口属性

#### 9. table（1个或多个）

> 配置指定的数据库表