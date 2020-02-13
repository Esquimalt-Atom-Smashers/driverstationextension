package frcdriverstationextension.net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Queue;

public class IOManager {
    private static IOManager instance;

    static {
        instance = new IOManager();
    }

    public static IOManager getInstance() {
        return instance;
    }

    private HashMap<Long, Packet> packetTable;
    private Queue<Packet> socketQueue;

    public IOManager() {
        packetTable = new HashMap<Long, Packet>(100);
    }

    public boolean doesPacketExist(long id) {
        return packetTable.get(id) != null;
    }

    public void addPacketToQueue(Packet packet) {
        socketQueue.add(packet);
    }

    public void putPacket(Packet packet)
    {
        packetTable.put(packet.getID(), packet);
    }

    public void removePacket(long id) {
        packetTable.remove(id);
    }

    public Packet getPacket(long id) {
        return packetTable.get(id);
    }

    public void sendAll() throws IOException
    {
        OutputStream socketOut = Client.getInstance().getSocketStream();
        
        while(!socketQueue.isEmpty())
        {
            Packet packet = socketQueue.poll();
            byte[] packetData = packet.packData();
            socketOut.write(packetData);
            
        }

        socketOut.flush();
    }
}   