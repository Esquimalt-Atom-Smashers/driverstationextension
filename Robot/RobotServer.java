package frc.robot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/*
    As per game manual: -> ports UDP/TCP 5800-5810: Team Use, bi-directional

    This can relay any information to the driverstation extenstion, put the property be on the driver station in the RobotMap class
    to send integers make sure to use wrapper so that the file is updated.
*/
public class RobotServer 
{
    private static final int INT_SIZE = 4;
    private ServerSocket server;
    private Socket connection;
    private byte[] buffer;
    private byte[] outBuffer;

    public RobotServer() throws IOException
    {
        server = new ServerSocket(5800); //use port 5800
        outBuffer = new byte[1000000]; //1MB
        buffer = new byte[1000000];
    }

    public void acceptConnection(int timeoutMillis, boolean newThread) throws IOException
    {
        server.setSoTimeout(timeoutMillis);

        if (newThread)
        {
            new Thread(()->{
                try {
                    connection = server.accept();
                } catch (IOException e) {e.printStackTrace();}
            }).start();
        }
        else
        {
            connection = server.accept();
        }

    }

    public boolean hasActiveConnection()
    {
        if (connection != null)
        {
            synchronized (connection)
            {
                return connection.isConnected();
            }
        }
        return false;
    }

    public void checkForRequests() throws IOException
    {
        InputStream stream = null;

         stream = connection.getInputStream();    
        
        if (stream == null) return;

        int bufPointer = 0;
        int reqID = 0;
        String[] reqs = null;

        if (stream.available() > 0)
        {
            try
            {
                int avail = stream.read(buffer); //returns size of incoming request
                reqID = ByteBuffer.allocate(INT_SIZE).put(buffer, 0, INT_SIZE).getInt();
                bufPointer += INT_SIZE;
                avail -= INT_SIZE;
                int numProperties = ByteBuffer.allocate(INT_SIZE).put(buffer, 0, INT_SIZE).getInt();
                bufPointer += INT_SIZE;
                avail -= INT_SIZE;
                reqs = new String[numProperties];

                for (int index = 0; avail > 0; index ++)
                {
                    int size = ByteBuffer.allocate(4).put(buffer, bufPointer, 4).getInt();
                    avail -= INT_SIZE;
                    bufPointer += INT_SIZE;
                    byte[] stringBytes = new byte[size];
                    System.arraycopy(buffer, bufPointer, stringBytes, 0, size); //copy bytes from buffer to string
                    avail -= size;
                    bufPointer += size;
                    reqs[index] = new String(stringBytes);

                }

            }
            catch (IOException e) {e.printStackTrace();}
            
            parseRequest(reqs, reqID);
        }

    }

    public void parseRequest(String[] req, int reqID)
    {
        if (req == null) return;
        int lastWrittenIndex = 0;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream out = null;
        for (int index = 0; index < req.length; index++)
        {
            String prop = req[index];
            Object value = RobotMap.getProperty(prop);
            if (value == null) //nothing corresponding to that key
            {
                value = "PARSE ERROR"; //driverstation will throw exception when reading this, be sure not put this in robot map.
            }
            try
            {
                bos = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bos);
                out.writeObject(value);
                out.flush();
                byte[] temp = bos.toByteArray();
                int size = temp.length;
                byte[] sizeArr = ByteBuffer.allocate(4).putInt(size).array();
                System.arraycopy(sizeArr, 0, outBuffer, lastWrittenIndex, 4);
                lastWrittenIndex += 4;
                System.arraycopy(temp, 0, outBuffer, lastWrittenIndex, temp.length);
                lastWrittenIndex += temp.length;
            }
            catch (Exception e){e.printStackTrace();}
        }
        
        try
        {
            connection.getOutputStream().write(outBuffer, 0, lastWrittenIndex);
            connection.getOutputStream().flush();
        }
        catch (Exception e) {}
    }



}