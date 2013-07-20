/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import jmc.minecraft.LauncherInit;

public class LogoPanel extends JPanel
{
  private Image bgImage;

  public LogoPanel()
  {
    setOpaque(true);
    try
    {
      BufferedImage src = ImageIO.read(LauncherInit.class.getResource("logo.png"));
      int w = src.getWidth();
      int h = src.getHeight();
      bgImage = src.getScaledInstance(w, h, 16);
      setPreferredSize(new Dimension(w + 32, h + 32));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void update(Graphics g) {
    paint(g);
  }

  public void paintComponent(Graphics g2) {
    g2.drawImage(bgImage, 24, 24, null);
  }
}