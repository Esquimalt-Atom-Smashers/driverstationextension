package frcdriverstationextension.net;

public class RobotPropertyDoesNotExistException extends Throwable
{
    private static final long serialVersionUID = 1L;
    private String property;
    public RobotPropertyDoesNotExistException(String property)
    {
        this.property = property;
    }

    @Override
    public void printStackTrace() 
    {
        System.out.println("The Requested Property --> " + property + " does not exist, or is not set");
        super.printStackTrace();
    }
}