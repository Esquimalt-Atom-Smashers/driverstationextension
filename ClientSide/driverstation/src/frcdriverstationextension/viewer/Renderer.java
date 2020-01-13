package frcdriverstationextension.viewer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import frcdriverstationextension.Config;
import frcdriverstationextension.net.Client;

import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer extends JPanel
{
    private static final long serialVersionUID = -4087607516746799477L;
    private int height, width;
    private Font font;
    private Color bg;
    private Color fg;
    private BufferedImage[] intakeStates;
    private int intakeImageHeight = 320;

    public Renderer(int width, int height) throws IOException
    {
        setSize(width, height);
        this.width = width;
        this.height = height;
        if (Config.THEME.equals("DARK"))
        {
            bg = Color.BLACK;
            fg = Color.WHITE;
        }
        else
        {
            bg = Color.WHITE;
            fg = Color.BLACK;
        }

        File[] images = new File(System.getProperty("user.dir")+"\\images").listFiles();
        System.out.println(System.getProperty("user.dir")+"\\images");
        intakeStates = new BufferedImage[images.length];
        for (int index = 0; index < images.length; index ++)
        {
            intakeStates[index] = (BufferedImage) ImageIO.read(images[index]);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(bg);
        g.fillRect(0, 0, width, height);
        font = new Font("Arial", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(fg);
        
        int xOffset = center(g.getFontMetrics().stringWidth(Config.ROBOT_NAME));
        int yOffset = g.getFontMetrics().getHeight() + 20;
        g.drawString(Config.ROBOT_NAME, xOffset, yOffset);
        font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);
        g.drawString("Connection Status: ", 0, g.getFontMetrics().getHeight());

        xOffset = g.getFontMetrics().stringWidth("Connection Status: ");
        boolean connected = Client.isConnected();
        font = new Font("Arial", Font.BOLD, 20);
        String text = null;
        if (connected)
        {
            g.setColor(Color.GREEN);text = "CONNECTED";

        }
        else
        {
            g.setColor(Color.RED);
            text = "DISCONNECTED";
        }
        yOffset = g.getFontMetrics().getHeight();
        g.fillRect(xOffset, 0, g.getFontMetrics().stringWidth(text)+4, yOffset+6);
        g.setColor(Color.BLACK);
        g.drawString(text, xOffset+2, yOffset+3);

        xOffset += g.getFontMetrics().stringWidth(text)+40;
        g.setColor(fg);
        g.drawString("Ping: ", xOffset, yOffset);
        String pingTime = Long.toString(Client.getPing());
        xOffset += g.getFontMetrics().stringWidth("Ping: ");
        g.drawString(pingTime + "ms", xOffset, yOffset);
        g.translate(0, 120);
        renderRobotInformation((Graphics2D)g);
    }


    public void renderRobotInformation(Graphics2D g)
    {
        int height = this.height - 120;
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Intake State: ", 0,  height-intakeImageHeight+(g.getFontMetrics().getHeight()*4));
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("EMPTY ", 0,  height-intakeImageHeight+(g.getFontMetrics().getHeight()*5));
        g.drawImage(intakeStates[4], center(intakeStates[0].getWidth()), height-intakeImageHeight, null);
        
    }

    public int center(int width) //returns offset from 0 to center component
    {
        return (this.width - width)/2;
    }
}