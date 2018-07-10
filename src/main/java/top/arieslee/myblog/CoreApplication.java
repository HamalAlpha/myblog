package top.arieslee.myblog;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @ClassName CoreApplication
 * @Description spring-boot项目主入口，放在根包下，SpringBoot主配置类只会扫描自己所在的包及其子包下面
 * @Author Aries
 * @Date 2018/7/5 17:31
 * @Version 1.0
 **/
@MapperScan(value = "top.arieslee.myblog.dao")
@SpringBootApplication
@EnableTransactionManagement
public class CoreApplication extends SpringBootServletInitializer {
    //Spring上下文创建
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    // 使用druid数据源
    // 自定义初始化后和销毁后执行方法，这里调用DruidDataSource中的init()和close()
    // 如果不想执行初始化或销毁方法，将属性值置为""
    @Bean
    //将application-jdbc.properties配置文件中前缀为spring.datasource的属性绑定到DruidDataSource实例对象属性
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    //配置SqlSessionFactory
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception{
        //搜索资源对象
        PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        //注入dataSource依赖
        sqlSessionFactoryBean.setDataSource(dataSource());
        //注入Mapper配置文件
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    //事务管理
    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    //启动入口
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}
