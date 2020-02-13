package frcdriverstationextension.net;

public class NetworkTableRequest
{
    private Packet packet;
    private String key;
    private Object ret;

    public NetworkTableRequest()  
    {
        packet = new Packet(null);
    }
}