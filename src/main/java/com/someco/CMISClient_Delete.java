package com.someco;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class CMISClient_Delete {
	
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
		deleteDocuments(session);
	}

	/**
	 * Method to delete all documents under particular folder in a site
	 * @param session Session Object.
	 */
	private static void deleteDocuments(Session session){
		
		// look for document library under marketing site
		String path = "/Sites/marketing/documentLibrary";
		Folder documentLibrary = (Folder) session.getObjectByPath(path);
		
		// locate the marketing folder
		Folder marketingFolder = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if ("Marketing".equals(child.getName())) {
				marketingFolder = (Folder) child;
			}
		}
		
		// Finally delete all the documents under marketing folder.
		if(marketingFolder!=null){
			for(CmisObject child : marketingFolder.getChildren()){
				session.delete(child);
			}
		}
		System.out.println("All the documents are deleted successfully.................");
	}
}
