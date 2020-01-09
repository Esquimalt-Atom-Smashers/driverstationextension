package frcdriverstationextension.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client 
{
    private Socket socket;
    private byte[] buffer;

    private static final String ROBORIO = "roboRIO-7287-frc.local"; // put team number here

    public Client()
    {
        buffer = new byte[1000000];
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

    public void checkAndParse(Request req)
    {
        try
        {
            if (socket.getInputStream().available() > 0)
            {
                int read = socket.getInputStream().read(buffer);
                byte[] data = new byte[read];
                System.arraycopy(buffer, 0, data, 0, read);
                req.parseResponse(data);
            }
        }
        catch (Exception e) {e.printStackTrace();} catch (MismatchedRequestIDException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RobotPropertyDoesNotExistException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public OutputStream getSocketStream()
    {
        OutputStream out = null;
        try
        {
           out = socket.getOutputStream();
        }
        catch (IOException e) {e.printStackTrace();}
        return out;
    }
    
    
}
    
