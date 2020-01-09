package frcdriverstationextension.net;

import java.net.InetSocketAddress;
import java.net.Socket;

public class Client 
{
    private Socket socket;

    private static final String ROBORIO = "roboRIO-7287-frc.local"; // put team number here

    public Client()
    {
        System.out.println("Attempting to Connect to Robot...");
        
        try 
        {
           InetSocketAddress sockadr = new InetSocketAddress(ROBORIO, 5800);
           socket = new Socket();
           socket.setSoTimeout(5000);
           socket.connect(sockadr);

        } catch (Exception e) 
        {
           System.out.println("Connection failed Closing-->");
           e.printStackTrace();
           System.exit(1);
        }
        
        System.out.println("Connection Succesfull");

    }

    
    
}
    
