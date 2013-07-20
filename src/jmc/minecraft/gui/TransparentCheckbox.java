package jmc.minecraft.gui;

import java.awt.Color;
import javax.swing.JCheckBox;

public class TransparentCheckbox extends JCheckBox
{

  public TransparentCheckbox(String string)
  {
    super(string);
    setForeground(Color.WHITE);
  }

  public boolean isOpaque() {
    return false;
  }
}