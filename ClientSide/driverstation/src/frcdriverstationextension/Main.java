package frcdriverstationextension;

import java.io.IOException;

import frcdriverstationextension.net.Client;
import frcdriverstationextension.net.Request;
import frcdriverstationextension.viewer.Window;

public class Main 
{
    public static void main(String[] args) throws InterruptedException, IOException 
    {
        Client client = new Client();  
        Window window = new Window(800,900);
        
        Request req = new Request();
        int test = req.add("test");
        int test2 = req.add("number");
        
        req.send();
        while (true)
        {
            client.checkAndParse(req);
            Object prop = req.getProperty(test);
            Object prop2 = req.getProperty(test2);
            if (prop != null)System.out.println( (String) prop);
            if (prop2 != null)System.out.println( (double)prop2);
            
            window.render();
            
        }
    }
}