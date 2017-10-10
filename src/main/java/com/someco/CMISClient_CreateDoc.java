package com.someco;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;

public class CMISClient_CreateDoc {
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";
	private static final String ATOMPUB_URL = "http://127.0.0.1:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
	private static final String REPOSITORY_ID = "-default-";
	private static final String FILE_NAME = "Third Doc.txt";

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		//Create a session to alfresco
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put(SessionParameter.USER, USERNAME);
		parameter.put(SessionParameter.PASSWORD, PASSWORD);
		parameter.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		parameter.put(SessionParameter.REPOSITORY_ID, REPOSITORY_ID);
		
		Session session = factory.createSession(parameter);
		
		//locate the document library folder of the Marketing Site that we created previously
		String path = "/Sites/marketing/documentLibrary";;
		Folder documentLibrary = (Folder) session.getObjectByPath(path);
		
		// Lets locate Marketing folder in doc library
		Folder marketingFolder = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if ("Marketing".equals(child.getName())) {
				marketingFolder = (Folder) child;
			}
		}
		
		//We have to consider that this folder may not exist. So, we need to create it on the fly
		if(marketingFolder == null){
			Map<String, Object> properties = new HashMap<String,Object>();
			properties.put(PropertyIds.NAME, "Marketing");
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			marketingFolder = documentLibrary.createFolder(properties); 
		} 
		
		//prepare the properties for new document.
		Map<String, Object> properties = new HashMap<String,Object>();
		properties.put(PropertyIds.NAME, FILE_NAME);
		properties.put(PropertyIds.OBJECT_TYPE_ID, "D:sc:marketingDoc");
		
		// Now lets prepare the content.
		String content = "This is my third document via CMIS api";
		String mimetype = "text/plain; charset=UTF-8";
		byte[] contentBytes = content.getBytes("UTF-8");
		ByteArrayInputStream stream = new ByteArrayInputStream(contentBytes);
		ContentStream contentStream = session.getObjectFactory().createContentStream(FILE_NAME,contentBytes.length, mimetype, stream);
		
		//finally we are ready to create document:)
		Document marketingDocument = marketingFolder.createDocument(properties, contentStream, VersioningState.MAJOR);
		
		System.out.println("Document created successfully.......");
		
	}
}
