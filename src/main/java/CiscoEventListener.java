import org.jivesoftware.smack.XMPPException;
import xmpp.XMPPClient;

public class CiscoEventListener {
    public static void main(String[] args) throws XMPPException {


        XMPPClient msalazar = new XMPPClient();
        msalazar.login();
//	    subscriptionManager = new PubSubManager(connection, pubsubDomain);



        // This infinite loop is to make the client wait for events
        System.out.println("Waiting for event");
        while(true){
            //waiting for new events
        }

    }
}
