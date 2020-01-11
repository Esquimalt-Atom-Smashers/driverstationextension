package frcdriverstationextension;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import frcdriverstationextension.net.Client;
import frcdriverstationextension.net.Request;

public class Main
{
    public static void main(String[] args) throws InterruptedException 
    {
        Client client = new Client();  
        
        Request req = new Request();
        int test = req.add("test");
        int test2 = req.add("number");
        req.send(client.getSocketStream());
        while (true)
        {
            client.checkAndParse(req);
            Object prop = req.getProperty(test);
            Object prop2 = req.getProperty(test2);
            if (prop != null)System.out.println( (String) prop);
            if (prop2 != null)System.out.println( (double)prop2);
            Thread.sleep(1000);
            
        }
    }
}