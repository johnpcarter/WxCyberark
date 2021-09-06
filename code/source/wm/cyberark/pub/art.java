package wm.cyberark.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.pkg.art.admin.ARTAdmin;
import com.wm.pkg.art.error.DetailedException;
import com.wm.pkg.art.error.DetailedServiceException;
import com.wm.pkg.art.ns.ARTNSNodeUtil;
import com.wm.pkg.art.ns.ConnectionDataNode;
import com.wm.pkg.art.ns.ConnectionDataNodeFactory;
import com.wm.pkg.art.ns.ConnectionDataNodeManager;
import com.wm.pkg.art.util.StringUtil;
import java.util.ArrayList;
import com.jc.invoke.credentials.Credentials;
// --- <<IS-END-IMPORTS>> ---

public final class art

{
	// ---( internal utility methods )---

	final static art _instance = new art();

	static art _newInstance() { return new art(); }

	static art _cast(Object o) { return (art)o; }

	// ---( server methods )---




	public static final void updateCredentials (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(updateCredentials)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connectionAlias
		// [i] field:0:required userName
		// [i] field:0:required password
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String connectionAlias = IDataUtil.getString( pipelineCursor, "connectionAlias");
		String userName = IDataUtil.getString(pipelineCursor, "userName");
		String password = IDataUtil.getString(pipelineCursor, "password");
		pipelineCursor.destroy();
		
		// process
				
		System.out.println("Updating connection " + connectionAlias + " with user " + userName + " and password " + password);
		
		try {
			
			IData input = IDataFactory.create();
			IDataCursor c = input.getCursor();
			IDataUtil.put(c, "connectionAlias", connectionAlias);
			c.destroy();
			
			try {
				com.wm.pkg.art.util.ExtendedConnectionUtils.disableConnection(input);
			} catch (Exception e) {
				// ignore failure assume that it is already disabled
			}
			
			ConnectionDataNode cdn = ConnectionDataNodeManager.getConnectionDataNode(connectionAlias);
			IData connectionProperties = cdn.getConnectionProperties();
						
			IDataCursor cc = connectionProperties.getCursor();
			
			String user = IDataUtil.getString(cc, "user");
			
			if (user != null) {				
				IDataUtil.put(cc, "user", userName);
			}
		
			 /* Generally Password fields is only 1, but there can be many as well.
			 * Connection Metadata will give information , which are fields are password 
			 */
			String[] passwordFields = cdn.get_xformFields();
			String packageName = cdn.getPackage().getName();
						
			ArrayList<String> encryptedPropertiesFields = new ArrayList<String>();
			for (int i = 0; i < passwordFields.length; i++) {
								
				// don't need to encrypt here as it is done in updateNode below
				//StringUtil.putInPassman(packageName, key, password);
				
				IDataUtil.put(cc, passwordFields[i], password);
				encryptedPropertiesFields.add(passwordFields[i]);
			}
			
			cc.destroy();
						
			cdn.set_NodeVersion(2);
					
			IData connectionManagerProperties = cdn.getConnectionManagerProperties();
			ConnectionDataNodeFactory.updateNode(connectionAlias, connectionProperties, encryptedPropertiesFields.toArray(new String[encryptedPropertiesFields.size()]), connectionManagerProperties);
			//ARTNSNodeUtil.saveNode(cdn); // Have to use updateNode as it caches properties in an inmemory hashmap.
			
			com.wm.pkg.art.util.ExtendedConnectionUtils.enableConnection(input);
			
		} catch (DetailedException e) {
			throw new ServiceException(e);
		}
		// --- <<IS-END>> ---

                
	}
}

