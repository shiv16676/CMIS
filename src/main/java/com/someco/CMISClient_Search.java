package com.someco;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class CMISClient_Search {
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";
	private static final String ATOMPUB_URL = "http://127.0.0.1:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
	private static final String REPOSITORY_ID = "-default-";

	public static void main(String[] args) {
		
		//Create a session to alfresco
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put(SessionParameter.USER, USERNAME);
		parameter.put(SessionParameter.PASSWORD, PASSWORD);
		parameter.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		parameter.put(SessionParameter.REPOSITORY_ID, REPOSITORY_ID);
		Session session = factory.createSession(parameter);
		searchDocuments(session);
	}
	
	/**
	 * Method to search documents in alfresco via CMIS API
	 * @param session Session Object
	 */
	private static void searchDocuments(Session session) {
		ItemIterable<QueryResult> results = session.query("SELECT cmis:name FROM sc:doc", false);

		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				String queryName = property.getQueryName();
				Object value = property.getFirstValue();
				System.out.println(queryName + ": " + value);
			}
			System.out.println("--------------------------------------");
		}
	}

}
