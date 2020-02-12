package frcdriverstationextension.net;

import java.util.Random;

public class Packet
{
    private long packetID;
    private int errorCode;
    private byte[] data;
    private byte[] responseData;
    private int size;

    public Packet(byte[] data)
    {
       packetID = createPacketID();
       this.data = data;
       this.size = data.length;
    }

    private long createPacketID()
    {
        long temp = new Random().nextLong();

        while (IOManager.getInstance().doesPacketExist(temp))
        {
            temp = new Random().nextLong();
        }

        return temp;
    }

    public long getID()
    {
        return packetID;
    }

    public void putResponseData(byte[] data)
    {
        responseData = data;
    }


}