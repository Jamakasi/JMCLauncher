/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

/**
 *
 * @author DimanA90
 * Include all need global vars
 */
public class GlobalVar {
    
    //Work dir  minecraft/
    public static String WorkDir = new String();
    //Main window title
    public static String MainWndTitle = new String();
    //News url
    public static String NewsURL = new String();
    //Combo box items
    public static String[] itemsServers;
    public static String[] ClientNames;
    public static String[] ArchivesList;
    //dir name to client and updatelink creator
    public static String RegURL;
    public static String DownloadClientRootURL;
    public static String DownloadNewLauncherURL;
    public static String AuthURL;
    public static String Version;
    public static String HostUrl;
    
    
    
    //Do not touch
    //User info
    public static String userName = "User name";
    public static String password = "password";
    public static String latestVersion;
    public static String downloadTicket;
    public static String sessionId;
    
    public static String clientinfo;
    public static boolean oldminecarft; //Старый метод запуска игры
    public static String[] addons;
    public static String[] addonsinfo;
    
    public static String Ram = "512m";
    public static int ramindex = 3;
    public static boolean ForceUpdate = false;
    public static boolean isOnline;
    public static int CurrentServer = 0;
    public static boolean debug = false;
    
}
