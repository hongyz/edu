package com.keyu.edu.core;

/**
 * 数据库更新脚本生成工具
 * @author hongyz
 *
 */
public class SchemaUpdater {

	public static void main(String[] args) throws Exception {
		SchemaGenerator gen = new SchemaGenerator();
		String file = "e:\\temp\\edu-core.schema.update.sql";
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/edu-core?useUnicode=true&characterEncoding=utf-8&user=root&password=root";
		gen.update(file, driverName, url);
	}
}
