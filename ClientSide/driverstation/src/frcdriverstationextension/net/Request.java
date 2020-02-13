package frcdriverstationextension.net;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;


import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Request implements PacketResponseListener
{

    private Packet packet;

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

    public int getID()
    {
        return ID;
    }


    public void parseResponse(byte[] buffer) throws MismatchedRequestIDException, ClassNotFoundException, RobotPropertyDoesNotExistException
    {
        int bufPointer = 0;
        
        int incomingID = ByteBuffer.wrap(buffer, 0, INT_SIZE).getInt(0);
        System.out.println(incomingID);
        bufPointer += INT_SIZE;
        //if (incomingID != ID) throw new MismatchedRequestIDException();
        
        for (int index = 0; index < propertyValues.length; index ++)
        {
            int messageSize = ByteBuffer.wrap(buffer, bufPointer, INT_SIZE).getInt(bufPointer);
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
        return propertyValues[index];
    }

    public int add(String property) //returns index of element, -1 after request is sent
    {
        reqlist.add(property);
        return reqlist.size() - 1;
    }

    public void send()
    {
        propertyValues = new Object[reqlist.size()];

        byte[] buf = new byte[1000000];

        int bufPointer = 0;//keeps track of position to write in buffer, as well as total size packet
        byte[] idbytes = ByteBuffer.allocate(INT_SIZE).putInt(ID).array();
        System.arraycopy(idbytes, 0, buf, bufPointer, INT_SIZE);
        bufPointer += INT_SIZE;
        byte[] numBytes = ByteBuffer.allocate(INT_SIZE).putInt(propertyValues.length).array();
        System.out.println(propertyValues.length);
        System.arraycopy(numBytes, 0, buf, bufPointer, INT_SIZE);
        bufPointer += INT_SIZE;

        for (int index = 0; index < reqlist.size(); index++) 
        {
            byte[] stringBytes = null;
            stringBytes = reqlist.get(index).getBytes();
            
            byte[] size = ByteBuffer.allocate(INT_SIZE).putInt(stringBytes.length).array();
            System.arraycopy(size, 0, buf, bufPointer, INT_SIZE);
            bufPointer += INT_SIZE;
            System.arraycopy(stringBytes, 0, buf, bufPointer, stringBytes.length);
            bufPointer += stringBytes.length;
        }

       try
       {
            Client.getInstance().sendData(buf, 0, bufPointer);
       }
       catch (IOException e) {e.printStackTrace();}

    }

    @Override
    public void packetResponse() 
    {
        byte[] data = packet.getResponseData();
    }
    
}