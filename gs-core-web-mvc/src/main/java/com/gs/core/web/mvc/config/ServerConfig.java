package com.gs.core.web.mvc.config;




import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;




@Slf4j
public class ServerConfig {
	private final static String SYSTEM_KEY = "systemId";
	private final static String ENV_KEY = "env";

	final static String SYSTEM_ID;
	private static Properties properties = new Properties();

	static {
		// 顺序检查加载
		// config.properties
		// config.xml
		// config-{evn}.properties
		// config-{env}.xml
		String configFilePrefix = "config";
		InputStream propertiesEnv = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(
						configFilePrefix + ".properties");
		if (propertiesEnv != null) {
			try {
				properties.load(propertiesEnv);
				log.info("Load SystemConfig[" + configFilePrefix
						+ ".properties] Success");
			}
			catch (IOException e) {
				log.error("Error When Load SystemConfig[" + configFilePrefix
						+ ".properties]", e);
			}
		}
		else {
			log.info("SystemConfig[" + configFilePrefix
					+ ".properties] Not Found!");
		}

		InputStream xmlIS = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(configFilePrefix + ".xml");
		if (xmlIS != null) {
			try {
				properties.loadFromXML(xmlIS);
				log.info("Load SystemConfig[" + configFilePrefix
						+ ".xml] Success");
			}
			catch (IOException e) {
				log.error("Error When Load SystemConfig[" + configFilePrefix
						+ ".xml]", e);
			}
		}
		else {
			log
					.info("SystemConfig[" + configFilePrefix
							+ ".xml] Not Found!");
		}

		/**
		 * env优先级 properties > System.getenv() > System.getProperty()
		 */
		String env = properties.getProperty(ENV_KEY);
		if (env==null || env.length()==0) {
			log.info("not find "+ENV_KEY + "in properties!");
			env = System.getenv(ENV_KEY);
		}
		if (env == null || env.length() == 0) {
			log.info("not find "+ENV_KEY + "in System.getenv!");
			env = System.getProperty(ENV_KEY);
		}
		if (env == null || env.length() == 0) {
			log.info("not find "+ENV_KEY + "in System.getProperty,use default!");
			env = "local";
			System.setProperty(ENV_KEY, env);
		}

		configFilePrefix = "config-" + env;
		//put env to System.Properties
		System.setProperty(ENV_KEY, env);

		InputStream propertiesEnvIS = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(
						configFilePrefix + ".properties");
		if (propertiesEnvIS != null) {
			try {
				properties.load(propertiesEnvIS);
				log.info("Load SystemConfig[" + configFilePrefix
						+ ".properties] Success");
			}
			catch (IOException e) {
				log.error("Error When Load SystemConfig[" + configFilePrefix
						+ ".properties]", e);
			}
		}
		else {
			log.info("SystemConfig[" + configFilePrefix
					+ ".properties] Not Found!");
		}

		InputStream xmlEnvIS = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(configFilePrefix + ".xml");
		if (xmlEnvIS != null) {
			try {
				properties.loadFromXML(xmlEnvIS);
				log.info("Load SystemConfig[" + configFilePrefix
						+ ".xml] Success");
			}
			catch (IOException e) {
				log.error("Error When Load SystemConfig[" + configFilePrefix
						+ ".xml]", e);
			}
		}
		else {
			log
					.info("SystemConfig[" + configFilePrefix
							+ ".xml] Not Found!");
		}

		String systemId = properties.getProperty(SYSTEM_KEY);
		if (systemId == null || systemId.length() == 0) {
			systemId = System.getenv(SYSTEM_KEY);
		}

		if (systemId == null || systemId.length() == 0) {
			systemId = System.getProperty(SYSTEM_KEY);
		}

		SYSTEM_ID = systemId;

		log.info("SystemId: " + SYSTEM_ID);
	}

	public String getConfigSystemId() {
		return SYSTEM_ID;
	}

	public String getConfigProperty(String name) {
		return getConfigProperty(name, null);
	}
	
	/**
	 * 获得int型的系统配置属性，如果没有，则返回默认值
	 * 
	 * @param name：属性名称
	 * @param defaultValue：默认值
	 * @return int
	 * @see 参考的JavaDoc
	 */
	public int getConfigIntProperty(String name, int defaultValue) {
		String value = getConfigProperty(name);
		if (value == null) {
			return defaultValue;
		}
		else {
			try {
				return Integer.parseInt(value);
			}
			catch (NumberFormatException e) {
				log.error("Value [" + value + "] of Property [" + name
						+ "] must be integer;Use defaultValue[" + defaultValue
						+ "]");
				return defaultValue;
			}
		}
	}

	
	/**
	 * 获得系统配置属性，如果没有，则返回默认值
	 * 
	 * @param name：属性名称
	 * @param defaultValue：默认值
	 * @return String
	 * @see 参考的JavaDoc
	 */
	public String getConfigProperty(String name, String defaultValue) {
		String value = System.getProperty(name);
		if (value == null) {
			value = System.getenv(name);
		}

		if (value == null) {
			value =  properties.getProperty(name);
		}
		
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		else {
			return value;
		}
	}
}
