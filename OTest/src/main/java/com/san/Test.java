package com.san;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.san.util.OrientConnection;

public class Test {

	public static void main(String[] args) {
		System.out.println("sout");
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		System.out.println("connecting...........");
		OrientConnection orientConnection = ctx.getBean(OrientConnection.class);
		
		
		try(ODatabaseSession db = orientConnection.getOrientDatabaseSession();){ 
			String query = "SELECT from User";
		    OResultSet rs = db.query(query);

		    while (rs.hasNext()) {
		      OResult item = rs.next();
		      System.out.println("name: " + item.getProperty("name"));
		    }

		}
	}

}
