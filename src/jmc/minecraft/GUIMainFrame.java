/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
//import javax.swing.UIManager;
import jmc.minecraft.utils.GlobalVar;
import jmc.minecraft.utils.Utils;

import jmc.minecraft.gui.BuildMainWindow;
//import sun.security.krb5.Config;
import jmc.minecraft.GameLaunch;

/**
 *
 * @author DimanA90
 * Create main form
 */
public class GUIMainFrame extends Frame {
    
    BuildMainWindow MainWindow;
    
    public GUIMainFrame()
    {
        super(GlobalVar.MainWndTitle);
        setBackground(Color.BLACK);
        
        MainWindow = new BuildMainWindow(this);
        
        JPanel panelBackground = new JPanel();
        panelBackground.setLayout(new BorderLayout());
        panelBackground.add(MainWindow, "Center");  
        panelBackground.setPreferredSize(new Dimension(854, 480));

        setLayout(new BorderLayout());
        add(panelBackground, "Center");

        pack();
        setLocationRelativeTo(null);
        try
        {
            setIconImage(ImageIO.read(GUIMainFrame.class.getResource("favicon.png")));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("GUIMainFrame() start");
        /*
         * Add close event
         */
        addWindowListener(new WindowAdapter() 
        {
           public void windowClosing(WindowEvent arg0) 
           {
               new Thread() 
               {
                    public void run() 
                        {
                           try 
                             {
                                Thread.sleep(30000L);
                             }catch (InterruptedException e) 
                             {
                                e.printStackTrace();
                             }
                         System.out.println("FORCING EXIT!");
                         System.exit(0);
                        }
                }.start();
               System.exit(0);
            }
        });
    }


/*
 * Send login info
 */
public void login(String userName, String password) {
    try {
      String parameters = "user=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + URLEncoder.encode(GlobalVar.Version, "UTF-8");
      String result = Utils.excutePost(GlobalVar.AuthURL, parameters); 
      //System.out.println("Auth result"+ result);
      if (result == null) {
          showError("Невозможно подключится к серверу!");
        MainWindow.setNoNetwork();
        return;
      }
      if (!result.contains(":")) {
        if (result.trim().equals("Bad login")) {
          showError("Неправильный логин или пароль!");
        } else if (result.trim().equals("Old version")) {
          MainWindow.outdated = true;
          showError("Нужно обновить лаунчер!");
        } else {
          showError(result);
        }
        MainWindow.setNoNetwork();
        return;
      }
      String[] values = result.split(":");

      
      GlobalVar.userName = values[2].trim();
      GlobalVar.latestVersion = values[0].trim();
      GlobalVar.downloadTicket = values[1].trim();
      GlobalVar.sessionId = values[3].trim();
      GameLaunch GameL = new GameLaunch();
      GameL.Run();
      

      removeAll();
      //add(launcher, "Center");
      validate();
      //launcher.start();
      //MainWindow.loginOk();
      MainWindow = null;
      setTitle(GlobalVar.MainWndTitle);
    } catch (Exception e) {
      e.printStackTrace();
      showError(e.toString());
      MainWindow.setNoNetwork();
    }
  }
/*
 * START GAME?????????????????????
 */
  public boolean canPlayOffline(String userName) {
    GameLaunch GameL = new GameLaunch();
      //Launcher launcher = new Launcher();
    //launcher.customParameters.putAll(customParameters);
    //launcher.init(userName, null, null, null);
    return true;//launcher.canPlayOffline();
  }
private void showError(String error) {
    removeAll();
    add(MainWindow);
    MainWindow.setError(error);
    validate();
  }
}