package com.san.util;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

public class OrientConnection {
	private OrientDB orient;
	private ODatabaseSession orientDatabaseSession;
	public OrientConnection() {}
	public OrientConnection(String url,String databaseName,String userName,String password) {
		orient = new OrientDB(url, OrientDBConfig.defaultConfig());
		orientDatabaseSession = orient.open(databaseName, userName, password);
	}
	
	public OrientDB getOrient() {
		return orient;
	}
	public ODatabaseSession getOrientDatabaseSession() {
		
		return orientDatabaseSession;
	}
}
