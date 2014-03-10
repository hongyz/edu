package com.keyu.edu.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * 数据库表结构生成工具
 * @author hongyz
 *
 */
@SuppressWarnings("rawtypes")
public class SchemaGenerator {
	
	private static final String PACKAGES_TO_SCAN = "com.keyu.edu.core.entity";
	private static final String ENTITY_CLASS_RESOURCE_PATTERN = "/**/*.class";
	private static final String DEFAULT_DIALECT = MySQL5InnoDBDialect.class.getName();
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	Configuration cfg;
	
	public static void main(String[] args) throws Exception {
		SchemaGenerator gen = new SchemaGenerator();
		String file = "e:\\temp\\edu-core.schema.sql";
		gen.generate(file);
	}
	
	public SchemaGenerator() throws Exception {
		this(PACKAGES_TO_SCAN);
	}
	
	public SchemaGenerator(String packageToScan) throws Exception {
		cfg = new Configuration();
//		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		cfg.setProperty("hibernate.dialect", DEFAULT_DIALECT);
		cfg.setNamingStrategy(new ImprovedNamingStrategy()); // 命名规则 My_NAME->MyName
		for (Class clazz : getClasses(packageToScan)) {
			cfg.addAnnotatedClass(clazz);
		}
	}
	
	public void generate(String file) throws Exception {
		SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
//		export.setOutputFile(file);
		export.setFormat(true);
		export.execute(true, false, false, false);
	}
	
	public void update(String file, String driverName, String url) {
		cfg.setProperty(AvailableSettings.DRIVER, driverName);
		cfg.setProperty(AvailableSettings.URL, url);
		SchemaUpdate update = new SchemaUpdate(cfg);
		update.setDelimiter(";");
//		update.setOutputFile(file);
		update.setFormat(true);
		update.execute(true, false);
	}

	private List<Class> getClasses(String pkg) throws Exception {
		List<Class> classes = new ArrayList<Class>();
		
		String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
				ClassUtils.convertClassNameToResourcePath(pkg) + ENTITY_CLASS_RESOURCE_PATTERN;
		Resource[] resources = this.resourcePatternResolver.getResources(pattern);
		MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader reader = readerFactory.getMetadataReader(resource);
				String className = reader.getClassMetadata().getClassName();
				Class clazz = Class.forName(className);
				classes.add(clazz);
			}
		}
		return classes;
	}
	
}
