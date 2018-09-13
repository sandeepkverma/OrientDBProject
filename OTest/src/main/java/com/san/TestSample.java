package com.san;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.san.util.OrientConnection;

public class TestSample {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		OrientConnection orientConnection = ctx.getBean(OrientConnection.class);
		ODatabaseSession db = orientConnection.getOrientDatabaseSession();
//		createSchema(db);
//		createPeople(db);
		
		//  finds friends of friends (FoaF) of a person
		executeAQuery(db);
		//		let's find all the people that are friends of both uday singh and yogesh
		executeAnoterQuery(db);
		
	}
	
	public static void createSchema(ODatabaseSession db) {
		OClass person = db.getClass("Person");
		if(person == null) {
			person = db.createVertexClass("Person");
		}
		
		if(person.getProperty("name") == null) {
			person.createProperty("name", OType.STRING);
			person.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}
		
		if(db.getClass("FriendOf") == null) {
			db.createEdgeClass("FriendOf");
		}
	}
	
	
	public static void createPeople(ODatabaseSession db) {
		OVertex uday = createPerson(db, "uday singh", "verma");
		OVertex sandeep = createPerson(db, "sandeep", "verma");
		OVertex yogesh = createPerson(db, "yogesh", "gupta");
		
		OEdge edge1 = uday.addEdge(sandeep, "FriendOf");
		edge1.save();
		OEdge edge2 = sandeep.addEdge(yogesh, "FriendOf");
		edge2.save();
	}
	
	public static OVertex createPerson(ODatabaseSession db,String name,String surname) {
			OVertex result = db.newVertex("Person");
			result.setProperty("name", name);
			result.setProperty("surname", surname);
			result.save();
			return result;
	}
	
	public static void executeAQuery(ODatabaseSession db) {
		String query = "select expand(out('FriendOf').out('FriendOf')) from Person where name=?";
		OResultSet rs = db.query(query, "uday singh");
		while(rs.hasNext()) {
			OResult friend = rs.next();
			System.out.println("friend name is ==>"+friend.getProperty("name"));
		}
		rs.close();
	}
	
	public static void executeAnoterQuery(ODatabaseSession db) {
		   
		String query = "MATCH "+
						"{class:Person,as:a,where:(name=:name1)}, "+
						"{class:Person,as:b,where:(name=:name2)}, "+
						"{as:a} -FriendOf-> {as:x} -FriendOf-> {as:b}"+
						" return x.name as friend";
		
		Map<String,Object> params = new HashMap<>();
		params.put("name1", "uday singh");
		params.put("name2", "yogesh");
		
		OResultSet rs = db.query(query, params);
		while(rs.hasNext()) {
			OResult friend = rs.next();
			System.out.println("friend is==>"+friend.getProperty("friend"));
		}
		rs.close();
				

	}
	
	
	
	

}
