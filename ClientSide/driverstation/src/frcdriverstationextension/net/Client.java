package frcdriverstationextension.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import frcdriverstationextension.Config;

public class Client 
{
    private Socket socket;
    private byte[] buffer;
    private static Client instance;
    private long sentTime = 0;
    private long receiveTime = 0;

    public static boolean isConnected() 
    {
        return instance.socket.isConnected();
    }

    public static Client getInstance()
    {
        return instance;
    }

    public static long getPing()
    {
        return instance.receiveTime - instance.sentTime;
    }

    private static final String ROBORIO = "roboRIO-"+Config.TEAM_NUMBER+"-frc.local"; // put team number here

    public Client()
    {
        buffer = new byte[1000000];
        System.out.println("Attempting to Connect to Robot...");
        instance = this;
        
        try 
        {
           InetSocketAddress sockadr = new InetSocketAddress(ROBORIO, 5800);
           socket = new Socket();
           socket.setSoTimeout(5000);
           socket.connect(sockadr);
           System.out.println("Connection Succesfull");

        } catch (Exception e) 
        {
           System.out.println("Connection failed Closing-->");
           e.printStackTrace();
        }

    }

    public void checkAndParse(Request req)
    {

        try
        {
            
            if (socket.getInputStream().available() > 0)
            {
                receiveTime = System.currentTimeMillis();
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
    

    public void sendData(byte[] buffer, int offset, int length) throws IOException
    {
        sentTime = System.currentTimeMillis();
        OutputStream out = socket.getOutputStream();
        out.write(buffer, offset, length);
        out.flush();
    }
    
}
    
