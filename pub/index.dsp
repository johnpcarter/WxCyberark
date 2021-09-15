<!DOCTYPE HTML>
<html>
<head>
<title>Cyberark Password Manager</title>
    <link rel="stylesheet" type="text/css" href="/WmRoot/webMethods.css">
    <link rel="stylesheet" type="text/css" href="/WmRoot/top.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">

<SCRIPT language="JavaScript">

</SCRIPT>
</head>
<body style="overflow-y: scroll;padding: 0px" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
    <div class="tdmasthead" id="top" height="50px">
      <div style="float:right;padding: 10px">
          <img src="./resources/sag-logo-white@3x.png" height="25px"/>
      </div>
      <div class="saglogo" style="display: flex; align-items: center;">
          <img src="./resources/wm-microservice-runtime.svg" height="50px" alt="Micro Service Runtime"/>
      </div>
  </div>
    <table width="100%">
      <tr>
        <td class="menu-navigator" style="border:none" colspan=2>packages &gt; WxCyberark
          <div style="float:right">
            <a href="./api/admin">Server API</a> | 
            <a href="./api/adapter">Adapter API</a>
          </div>
        </td>
      </tr>
	  </table>
    <p style="font-size: medium; margin: 40px;">
      This package allows credentials to be replaced at runtime with values from your cyberark vault. Credentials are persisted locally and connections are pooled. Cyberark is queried of a new connection cannot be created due to an "Access Denied" error, the fetched credentials are updated locally and the existing connection pool is cleared and recreated with the new credentials.
      All dependent services calls that use this connection are blocked until the credentials have been fetched and updated locally.
    </p>
    <p style="font-weight: bold; margin-left: 40px">Setup</p>
    <p style="font-style: italic; margin-left: 40px">Edit the following properties to configure your environment.</p>
    <p style="font-weight: bold; margin-left: 40px; margin-top: 20px">Cyberark end-point</p>
    
    %invoke wm.cyberark.priv.config:getCyberarkProperties%
    %ifvar editCyberarkProps -notempty%
      <form action="/invoke/wm.cyberark.priv.dsp/saveCyberarkProperties" method="POST">
        <p style="margin-left: 40px;">End point: <input type="text" name="endpoint" value="%value endpoint%"/></p>
        <p style="margin-left: 40px;">App ID: <input type="text" name="AppID" value="%value AppID%"/></p>
        <p style="margin-left: 40px;">Safe for connections: <input type="text" name="Safe" value="%value Safe%"/></p>
        <p style="margin-left: 40px;">Safe for users: <input type="text" name="SafeForUsers" value="%value SafeForUsers%"/></p>
        <p style="margin-left: 40px;">Disable users on error: <input type="checkbox" name="suspendUsers" %ifvar suspendUsers -notempty%checked%endifvar%/>
        <p style="margin-left: 40px;">Throw Error: <input type="checkbox" name="throwError" %ifvar throwError -notempty%checked%endifvar%/>
        <p style="margin-left: 40px;">Sync interval (Users): <input type="text" name="syncInterval" value="%value syncInterval%"/></p>
        <button type="submit" style="float:right; margin-right: 60px; margin-top: 20px"> Save changes </button>
        <p style="color: red;margin-right: -80px;margin-top: 50px; float: right"> Please reload package after saving changes!</p>
      </form>
    %else%
      <p style="margin-left: 40px;">End point: <b>%value endpoint%</b></p>
      <p style="margin-left: 40px;">App ID: <b>%value AppID%</b></p>
      <p style="margin-left: 40px;">Safe for connections: <b>%value Safe%</b></p>
      <p style="margin-left: 40px;">Safe for users: <b>%value SafeForUsers%</b></p>
      <p style="margin-left: 40px;">Disable users on error: <b>%ifvar suspendUsers -equals('true')% Yes %else% No %endif%</b></p>
      <p style="margin-left: 40px;">Throw Error: <b>%ifvar throwError -equals('true')% Yes %else% No %endif%</b></p>
      <p style="margin-left: 40px;">Sync interval (Users): <b>%value syncInterval%</b> %ifvar syncInterval equals("0")%<i>(Not Scheduled)%endif%</i></p>
      <a href="/WxCyberark/index.dsp?editCyberarkProps=true" style="float:right; margin-right: 60px; margin-top: 20px"><button>edit endpoint</button></a>
    %endif%</b>
    %endinvoke%
    
    <p style="font-weight: bold; margin-left: 40px; margin-top: 60px">SSL Authentication</p>
    <p style="font-style: italic; margin-left: 40px; margin-right: 220px">
      Connections to Cyberark are secured via a certificate that you will need to register in Cyberark for the application above. Create a keystore with the name "cyberark" for the given certificate and private key. You will also need to ensure that the CA you used to sign your certificate is managed by a truststore called "cyberark". You will need to registere these stores with webMethods via Security -> Keystore.
    </p>
    <p style="font-weight: bold; margin-left: 40px;">Extended Settings</p>
    <p style="margin-left: 40px; margin-right: 40px">Set the following extended setting if cyberark does not specify a valid certificate or CA, otherwise the connection will be refused
      
      <pre>
        watt.security.ssl.client.ignoreEmptyAuthoritiesList=true
      </pre>
    </p>
    <p style="font-weight: bold; margin-left: 40px; margin-top: 40px">Connections</p>
    <p style="font-style: italic; margin-left: 40px">
      Specify list of connections for user/password injection from cyberark. id of cyberark is assumed to be the same as the connection name, unless specified after a delimiter ; e.g. 
    </p>
    <pre>
      jc.helloworld.jdbc:conn;scratchDB
    </pre>
    <p style="font-style: italic; margin-left: 40px">
      At runtime the following connections will have their credentials reloaded from Cyberark if an access denied exception is thrown.
    </p>
    %ifvar editConnections -notempty%
    %invoke wm.cyberark.priv.config:getConnectionsFile%

      <form action="/invoke/wm.cyberark.priv.dsp/saveConnections" method="POST">
        <div style="width:90%;height:420px;margin: auto; padding: 20px; background-color:lightgray; overflow-y:scroll; overflow-x: hidden">
        <textarea name="content" style="width:100%;height:400px; background-color: transparent">%value content%</textarea>
        </div>
        <button type="submit" style="float:right; margin-right: 60px; margin-top: 20px"> Save changes </button>
        <p style="color: red; float:right; margin-top: 60px; margin-right: -80px"> Please reload package after saving changes!</p>
      </form>
      
    %endinvoke%
    %else%
    %invoke wm.cyberark.pub:getConnectionsStatus%
      <div style="width:90%;height:400px;margin: auto; padding: 20px; background-color:lightgreen; overflow-y:scroll">
        <table style="width:100%; background-color: transparent">
          <tr>
            <th style="width: 400px; text-align: left; background-color: transparent">Connection</th>
            <th style="width: 200px; text-align: left; background-color: transparent">Object</th>
            <th style="width: 80px; text-align: left; background-color: transparent">User</th>
            <th style="width: 80px; text-align: left; background-color: transparent">Status</th>
            <th style="background-color: transparent;"></th>
            <th style="background-color: transparent; width: 50px"></th>
          </tr>
          %loop connections%
          <tr>
            <td style="background-color: transparent">%value Connection%</td>
            <td style="background-color: transparent">%value Object%</td>
            <td style="background-color: transparent">%value username%</td>
            <td style="background-color: transparent">%value status%</td>
            <td style="background-color: transparent; color: red">%value failure%</td>
            <td style="background-color: transparent">%ifvar failure -notempty%<a href="/invoke/wm.cyberark.priv.dsp/clearConnectionError?connectionAlias=%value Connection%"><button>reactivate</button></a>%endif%</td>
          </tr>
          %endloop%
        </table>
      </div>
        <a href="/WxCyberark/index.dsp?editConnections=true" style="float:right; margin-right: 60px; margin-top: 20px"><button>edit connections</button></a>
    %endinvoke%
    %endif%
    
    <p style="font-weight: bold; margin-left: 40px; margin-top: 40px">Users</p>
    %invoke wm.cyberark.priv.config:getUsersFile%
    <p style="font-style: italic; margin-left: 40px">
      List of local users that will have their passwords synced with cyberark every <i>%value syncInterval%</i> seconds.
    </p>
    %ifvar editUsers -notempty%
    <form action="/invoke/wm.cyberark.priv.dsp/saveUsers" method="POST">
      <div style="width:90%;height:420px;margin: auto; padding: 20px; background-color:lightgray; overflow-y:scroll; overflow-x: hidden">
      <textarea name="content" style="width:100%;height:400px; background-color: transparent">%value content%</textarea>
      </div>
      <button type="submit" style="float:right; margin-right: 60px; margin-top: 20px"> Save changes </button>
      <p style="color: red; float:right; margin-top: 60px; margin-right: -80px"> Please reload package after saving changes!</p>
    </form>
  %else%
    <div style="width:90%;height:400px;margin: auto; padding: 20px; background-color:lightgreen; overflow-y:scroll">
      <pre>
%value content%
      </pre>
    </div>
      <a href="/WxCyberark/index.dsp?editUsers=true" style="float:right; margin-right: 60px; margin-top: 20px"><button>edit users</button></a>
  %endif%
  %endinvoke%
</body>
</html>