package frcdriverstationextension.net;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Random;

public class Packet
{
    private long packetID;
    private int errorCode;
    private byte[] data;
    private byte[] responseData;
    private int size;
    private LinkedList<PacketResponseListener> packetlisteners;

    public Packet(byte[] data)
    {
       packetID = createPacketID();
       this.data = data;

       this.size = data.length;
       packetlisteners = new LinkedList<PacketResponseListener>();
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

    public byte[] getResponseData()
    {
        return responseData;
    }

    public void putResponseData(byte[] data)
    {
        responseData = data;
    }

    public void addPacketResponseListener(PacketResponseListener l)
    {
        packetlisteners.add(l);
    }

    public void dispose()
    {
        IOManager.getInstance().removePacket(this.packetID);
        packetlisteners.clear();
    }

    public byte[] packData()
    {
        byte[] packetRaw = new byte[8+4 + size];
        byte[] idRaw = ByteBuffer.allocate(8).putLong(packetID).array();
        System.arraycopy(idRaw, 0, packetRaw, 0, 8);
        byte[] sizeRaw = ByteBuffer.allocate(4).putInt(size).array();
        System.arraycopy(sizeRaw, 8, packetRaw, 0, 4);
        System.arraycopy(data, 0, packetRaw, 12, size);
        return packetRaw;
    }

}