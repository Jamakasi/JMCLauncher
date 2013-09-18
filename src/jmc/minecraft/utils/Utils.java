/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @Copy from notch launcher
 */
public class Utils {
 
  private static File workDir = null;
    
    
  public static enum OS
  {
    linux, solaris, windows, macos, unknown;
  }
  public static OS getPlatform() {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) return OS.windows;
    if (osName.contains("mac")) return OS.macos;
    if (osName.contains("solaris")) return OS.solaris;
    if (osName.contains("sunos")) return OS.solaris;
    if (osName.contains("linux")) return OS.linux;
    if (osName.contains("unix")) return OS.linux;
    return OS.unknown;
  }
    public static File getWorkingDirectory() {
    if (workDir == null) workDir = getWorkingDirectory(GlobalVar.WorkDir);
    return workDir;
  }
    /*
     * Get working dir to current OS
     */
    public static File getWorkingDirectory(String applicationName) {
    String userHome = System.getProperty("user.home", ".");
    File workingDirectory;
    switch (getPlatform().ordinal()) {
    case 0:
    case 1:
      workingDirectory = new File(userHome, '.' + applicationName + '/');
      break;
    case 2:
      String applicationData = System.getenv("APPDATA");
      if (applicationData != null)
    	  workingDirectory = new File(applicationData, "." + applicationName + '/');
      else
        workingDirectory = new File(userHome, '.' + applicationName + '/');
      break;
    case 3:
      workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
      break;
    default:
      workingDirectory = new File(userHome, applicationName + '/');
    }
    if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) throw new RuntimeException("The working directory could not be created: " + workingDirectory);
    return workingDirectory;
  }
    
 /*
  * @Open link in desktop web browser
  */
  public static void openLink(URI uri) {
    try {
      Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
      o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { uri });
    } catch (Throwable e) {
      System.out.println("Failed to open link " + uri.toString());
    }
  }
 /*
  * Execute post and wait answer
  */ 
  public static String excutePost(String targetURL, String urlParameters)
  {
    HttpURLConnection connection = null;
    try
    {
      URL url = new URL(targetURL);
      
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
      connection.setRequestProperty("Content-Language", "en-US");

      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      connection.connect();

      /*byte[] bytes = new byte[294];
      DataInputStream dis = new DataInputStream(Utils.class.getResourceAsStream("minecraft.key"));
      dis.readFully(bytes);
      dis.close();*/


      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.flush();
      wr.close();

      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));

      StringBuffer response = new StringBuffer();
      String line;
      while ((line = rd.readLine()) != null)
      {
        response.append(line);
        response.append('\r');
      }
      rd.close();

      String str1 = response.toString();
      return str1;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
    finally
    {
      if (connection != null)
        connection.disconnect();
    }
  }

  public static boolean isEmpty(String str) {
    return (str == null) || (str.length() == 0);
  }
  
  public static boolean isOnline() {
    Boolean result = false;
    HttpURLConnection con = null;
    try {
        // HttpURLConnection.setFollowRedirects(false);
        // HttpURLConnection.setInstanceFollowRedirects(false)
        con = (HttpURLConnection) new URL(GlobalVar.HostUrl).openConnection();
        con.setRequestMethod("HEAD");
        result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (con != null) {
            try {
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    return result;
}
 
public static long downloadFilesSize(String strURL) {
        try {
            URL connection = new URL(strURL);
            HttpURLConnection urlconn;
            urlconn = (HttpURLConnection) connection.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            
            return urlconn.getContentLength();
        } catch (IOException e) {
            System.out.println(e);
            return 0;
        }
    }  
  
  public static boolean login(String userName, String password) 
{
    try {
      String parameters = "user=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + URLEncoder.encode(GlobalVar.Version, "UTF-8");
      String result = Utils.excutePost(GlobalVar.AuthURL, parameters); 
      if (!result.contains(":")) {
        if (result.trim().equals("Bad login")) {
          JOptionPane.showMessageDialog( null,
                    "Неправильный логин или пароль!",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
                 return false;
        } else if (result.trim().equals("Old version")) {
                    JOptionPane.showMessageDialog( null,
                    "Нужно обновить лаунчер!",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
                        openLink(new URI(GlobalVar.DownloadNewLauncherURL));
                        System.exit(0); //Close launcher and open download launcher link
                 return false;
        } else {
                    JOptionPane.showMessageDialog( null,
                    result,
                    "Неизвестная ошибка",
                    JOptionPane.ERROR_MESSAGE);
                 return false;
        }
      }
      String[] values = result.split(":");

      GlobalVar.userName = values[2].trim();
      GlobalVar.latestVersion = values[0].trim();
      GlobalVar.downloadTicket = values[1].trim();
      GlobalVar.sessionId = values[3].trim();
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
}
  

  


}
