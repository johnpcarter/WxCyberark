package wm.cyberark;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.jc.invoke.credentials.ConnectionManager;
import com.jc.invoke.credentials.CredentialsInjector;
// --- <<IS-END-IMPORTS>> ---

public final class pub

{
	// ---( internal utility methods )---

	final static pub _instance = new pub();

	static pub _newInstance() { return new pub(); }

	static pub _cast(Object o) { return (pub)o; }

	// ---( server methods )---




	public static final void clearConnectionError (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(clearConnectionError)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connection
		IDataCursor c = pipeline.getCursor();
		String conn = IDataUtil.getString(c, "connection");
		c.destroy();
		
		CredentialsInjector.defaultInstance.clearErrorForCredentials(conn);
			
		// --- <<IS-END>> ---

                
	}



	public static final void getConnectionsStatus (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getConnectionsStatus)>> ---
		// @sigtype java 3.5
		// [o] record:1:required connections
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "connections", CredentialsInjector.defaultInstance.status());
		c.destroy();
			
		// --- <<IS-END>> ---

                
	}
}

