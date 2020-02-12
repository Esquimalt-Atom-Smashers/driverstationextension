package frcdriverstationextension.net;

import java.util.HashMap;
import java.util.Queue;

public class IOManager
{
    private static IOManager instance;

    static
    {
        instance = new IOManager();
    }

    public static IOManager getInstance()
    {
        return instance;
    }

    private HashMap<Long, Packet> packetTable;
    private Queue<Packet> socketQueue;


    public IOManager()
    {
        packetTable = new HashMap<Long, Packet>(100);
    }

    public boolean doesPacketExist(long id)
    {
        return packetTable.get(id) != null;
    }

    public void sendPacket(Packet packet)
    {
        packetTable.put(packet.getID(), packet);
        socketQueue.add(packet);

    }

    public void removePacket(long id)
    {
        packetTable.remove(id);
    }

    public Packet getPacket(long id)
    {
        return packetTable.get(id);
    }
}   