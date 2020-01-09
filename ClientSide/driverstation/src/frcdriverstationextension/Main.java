package frcdriverstationextension;

import frcdriverstationextension.net.Client;
import frcdriverstationextension.net.Request;

public class Main
{
    public static void main(String[] args) 
    {
        Client client = new Client();  
        
        Request req = new Request();
        int test = req.add("test");
        req.send(client.getSocketStream());
        while (true)
        {
            client.checkAndParse(req);
            System.out.println(req.getProperty(test));
        }
    }
}