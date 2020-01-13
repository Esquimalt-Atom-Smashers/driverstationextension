package frcdriverstationextension.viewer;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics2D;

import frcdriverstationextension.Config;

public class Window extends JFrame 
{
    private int width, height;
    private Renderer renderer;

    public Window(int width, int height) throws IOException
    {
        this.height = height;
        this.width = width;
        this.setSize(width, height);
        this.setTitle(Config.TEAM_NAME + " " + Config.TEAM_NUMBER+ " - " + Config.ROBOT_NAME);
        String logo = System.getProperty("user.dir") + "\\logo.png";
        System.out.println(logo);
        this.setIconImage(ImageIO.read(new File(logo)));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderer = new Renderer(width,height);
        getContentPane().add(renderer);
        this.setVisible(true);
        createBufferStrategy(3);
    }

    public void render()
    {
        repaint();
    }
}