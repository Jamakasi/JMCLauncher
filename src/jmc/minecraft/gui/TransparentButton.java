package jmc.minecraft.gui;

import javax.swing.JButton;

public class TransparentButton extends JButton
{

  public TransparentButton(String string)
  {
    super(string);
  }

  public boolean isOpaque() {
    return false;
  }
}