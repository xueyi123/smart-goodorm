package com.iih5.goodorm.kit;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;

import javax.sql.DataSource;

/**
 * 载入spring插件
 */
public class SpringKit implements ApplicationContextAware {
	SqlXmlKit sqlXmlKit = null;
	public SpringKit(){
		sqlXmlKit = new SqlXmlKit();
	}
	public SpringKit(String sqlPath){
		 sqlXmlKit = new SqlXmlKit(sqlPath);
	}

	private static ApplicationContext appContext = null;
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
	public static ApplicationContext getApplicationContext() {
		return appContext;
	}
	public static JdbcTemplate getJdbcTemplateByDataSource(String dataSourceName) {
		DataSource dataSource= (DataSource)appContext.getBean(dataSourceName);
		return  new JdbcTemplate(dataSource);
	}
	public static JedisPool getJedisPool(String jedisPool) {
		return(JedisPool)appContext.getBean(jedisPool);
	}
}
