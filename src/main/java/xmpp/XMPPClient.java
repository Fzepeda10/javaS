package xmpp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.Node;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.xml.sax.SAXException;
import xml.XMLItem;

import javax.xml.crypto.dsig.XMLObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class XMPPClient {

    private XMPPConnection connection = null;
    private volatile PubSubManager subscriptionManager = null;
    private String username = "USERNAME";
    private String passwd = "PASSWORD";
    private String resource = "RESOURCE";
    private String nodeId = "/finesse/api/User/" + username;
    private String hostname = "HOSTNAME";

    public void login() throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration(hostname, 2222);
        config.setSASLAuthenticationEnabled(true);
        config.setReconnectionAllowed(false);
        connection = new XMPPConnection(config);
        subscriptionManager = new PubSubManager(connection, "pubsub."+hostname);

        try {
            connection.connect();
            connection.login(username, passwd, resource);
            System.out.println("Successfully Connected");
            subscribeImpl();
            System.out.println("Successfully Obtained the node");
        } catch (XMPPException e) {
            System.out.println("Error during xmpp connection"+e.getMessage());
            throw e;
        }

    }
    private void subscribeImpl() throws XMPPException
    {
        String logPrefix = "XMPPClient.subscribe on nodeId = " + nodeId + " for user = " + username;
        Node node = subscriptionManager.getNode(nodeId);

        // ItemEventListener required by Smack library
        ItemEventListener<Item> listener = new ItemEventListener<Item>() {
            @Override
            public void handlePublishedItems(ItemPublishEvent<Item> itemsPublished) {

                List<Item> items = itemsPublished.getItems();
                XmlMapper xmlMapper = new XmlMapper();

                for(Item item:items)
                {
                    try {
                        XMLItem value
                                = xmlMapper.readValue(item.toString(), XMLItem.class);

                        System.out.println(value.Notification.Update.Data.User.State.toString());

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }

            }
        };

        // Add the event listener for this node
        node.addItemEventListener(listener);

    }

    public void logout(){

        connection.disconnect();
        System.out.println("Successfully disconnected");
    }
}
