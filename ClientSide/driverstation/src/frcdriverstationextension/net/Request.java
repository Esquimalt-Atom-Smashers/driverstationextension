package frcdriverstationextension.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Request
{
    LinkedList<String> reqlist;
    private static final int INT_SIZE = 4;
    private static final String PARSE_ERROR = "PARSE ERROR";
    private int ID;
    private Object[] propertyValues;

    public Request()
    {
        reqlist = new LinkedList<String>();
        ID = (int)(Math.random() * Integer.MAX_VALUE);
    }


    public void parseResponse(byte[] buffer) throws MismatchedRequestIDException, ClassNotFoundException, RobotPropertyDoesNotExistException
    {
        int bufPointer = 0;
        int incomingID = ByteBuffer.allocate(INT_SIZE).put(buffer, 0, INT_SIZE).getInt();
        bufPointer += INT_SIZE;
        if (incomingID != ID) throw new MismatchedRequestIDException();
        
        for (int index = 0; index < propertyValues.length; index ++)
        {
            int messageSize = ByteBuffer.allocate(INT_SIZE).put(buffer, bufPointer, INT_SIZE).getInt();
            bufPointer += INT_SIZE;
            byte[]  objectBytes = new byte[messageSize];
            System.arraycopy(buffer, bufPointer, objectBytes, 0, messageSize);
            bufPointer += messageSize;
            if (new String(objectBytes).equals(PARSE_ERROR)) throw new RobotPropertyDoesNotExistException(reqlist.get(index));
            try
            {
                ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
                ObjectInputStream in = new ObjectInputStream(bis);
                propertyValues[index] = in.readObject();

            }
            catch (IOException e) {e.printStackTrace();}
        }

    }


    
    public Object getProperty(int index)
    {
        return propertyValues[index][index];
    }

    public int add(String property) //returns index of element, -1 after request is sent
    {
        reqlist.add(property);
        return reqlist.size() - 1;
    }

    public void send(OutputStream stream)
    {
        String[] reqarr = (String[]) reqlist.toArray();
        propertyValues = new Object[reqarr.length];

        byte[] buf = new byte[1000000];

        int bufPointer = 0;//keeps track of position to write in buffer, as well as total size packet
        byte[] idbytes = ByteBuffer.allocate(INT_SIZE).putInt(ID).array();
        System.arraycopy(idbytes, 0, buf, bufPointer, INT_SIZE);
        bufPointer += INT_SIZE;

        for (int index = 0; index < reqarr.length; index++) 
        {
            byte[] stringBytes = reqarr[index].getBytes(); //convert string to bytes
            
            byte[] size = ByteBuffer.allocate(INT_SIZE).putInt(reqarr.length).array();
            System.arraycopy(size, 0, buf, bufPointer, INT_SIZE);
            bufPointer += INT_SIZE;
            System.arraycopy(stringBytes, 0, buf, bufPointer, stringBytes.length);
            bufPointer += stringBytes.length;
        }

        try
        {
            stream.write(buf, 0, bufPointer);
            stream.flush();
        }
        catch (IOException e) {e.printStackTrace();}

    }
}