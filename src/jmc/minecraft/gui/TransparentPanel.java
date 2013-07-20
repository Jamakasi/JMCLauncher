/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.gui;

import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public class TransparentPanel extends JPanel
{
  private Insets insets;

  public TransparentPanel()
  {
  }

  public TransparentPanel(LayoutManager layout)
  {
    setLayout(layout);
  }

  public boolean isOpaque() {
    return false;
  }

  public void setInsets(int a, int b, int c, int d) {
    insets = new Insets(a, b, c, d);
  }

  public Insets getInsets() {
    if (insets == null) return super.getInsets();
    return insets;
  }
}