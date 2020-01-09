package frc.robot;

import java.util.HashMap;

public class RobotMap
{
    private static HashMap<String, Object> properties;

    static
    {
        properties = new HashMap<String,Object>(100);
    }

    public static void addProperty(String key, Object property)
    {
        properties.put(key, property);
    }

    public static Object getProperty(String key)
    {
        return properties.get(key);
    }

    public static void updateProperty(String key, Object newVal)
    {
        properties.put(key, newVal);
    }

    public static void remove(String key)
    {
        properties.remove(key);
    }
}