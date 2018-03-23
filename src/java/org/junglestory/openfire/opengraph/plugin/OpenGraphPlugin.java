package org.junglestory.openfire.opengraph.plugin;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

import org.dom4j.Element;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Message;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenGraphPlugin implements Plugin, PacketInterceptor  {
	private static final Logger Log = LoggerFactory.getLogger(OpenGraphPlugin.class);
	private InterceptorManager interceptorManager;	
	private long MAX_CACHE_DATE = 1L;
    private Map<String, OpenGraphTag> map = new ConcurrentHashMap<String, OpenGraphTag>();
    
	public OpenGraphPlugin() {
        interceptorManager = InterceptorManager.getInstance();
	}
	
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
    	Log.info("Open graph parser plugin initialize ...");
    	
    	// Register a message interceptor manager
    	interceptorManager.addInterceptor(this);        
    }

    public void destroyPlugin() {
    	Log.info("Open graph parser plugin destory ...");
    	
    	// Unregister a message interceptor manager
    	interceptorManager.removeInterceptor(this);
    }

	@Override
	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
			throws PacketRejectedException {

		if(isValidTargetPacket(packet,incoming,processed)) {
			Packet original = packet;			
						
			if(original instanceof Message) {
				Message receivedMessage = (Message)original;
				
				String url = receivedMessage.getBody();

		        if (isUrl(url)) {
		        	if (url.indexOf("http") < 0 && url.indexOf("https") < 0) {
		        		url = "http://" + url;
		        	}
		        	
		        	OpenGraphTag tag = map.get(url);

			        if (tag == null || (MAX_CACHE_DATE < DateUtils.getDiffDate(tag.getCreateDate()))) {
			            OpenGraphParser parser = new OpenGraphParser();
			            tag = parser.parser(url);
			            map.put(url, tag);
			        }
			        				
					Element sendFileElement = receivedMessage.addChildElement("x", "jabber:x:og");
					sendFileElement.addElement("title").setText(tag.getTitle());
					sendFileElement.addElement("image").setText(tag.getImage());
					sendFileElement.addElement("url").setText(tag.getUrl());
					sendFileElement.addElement("description").setText(tag.getDescription());	
		        }
			}		
		}	
	}
	
	private boolean isUrl(String str) {
        String regex = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        if (m.find()) {
        	return true;
        } 
        
        return false;
	}
	
	private boolean isValidTargetPacket(Packet packet, boolean incoming, boolean processed) {
        return  !processed && incoming && packet instanceof Message;
	}
}