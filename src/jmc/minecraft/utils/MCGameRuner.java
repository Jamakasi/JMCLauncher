/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmc.minecraft.RunGame;

/**
 *
 * @author DimanA90
 */
public class MCGameRuner {
private String ClientFolderPath = Utils.getWorkingDirectory() + File.separator + GlobalVar.itemsServers[GlobalVar.CurrentServer] + File.separator + ".minecraft" + File.separator;
private String AppDataPath = Utils.getWorkingDirectory() + File.separator + GlobalVar.itemsServers[GlobalVar.CurrentServer] + File.separator ;

public void LetsGame()
{
ArrayList<String> params = new ArrayList<String>();

        params.add("java");
        params.add("-Xmx"+GlobalVar.Ram);
        params.add("-cp");
        params.add(ClientFolderPath+"bin"+ File.separator+"*");
        if(GlobalVar.oldminecarft)
        {
             params.add("net.minecraft.client.Minecraft");
             params.add(GlobalVar.userName);
             params.add(GlobalVar.sessionId);
        }else
        {
             params.add("net.minecraft.client.main.Main"); 
             params.add("--assetsDir");
             params.add(ClientFolderPath+"assets");
             params.add("--username");
             params.add(GlobalVar.userName);
             params.add("--session");
             params.add(GlobalVar.sessionId);
             params.add("--version");
             params.add(GlobalVar.Version);
        }
        
        
        File log = new File(ClientFolderPath+"game.log");
        
        ProcessBuilder pb = new ProcessBuilder(params);
        Map<String, String> env = pb.environment();
        pb.environment().clear();
        if(Utils.getPlatform()==Utils.OS.windows)
        {
            pb.environment().put("PATH", ClientFolderPath+"bin"+ File.separator+"natives");
            pb.environment().put("APPDATA", AppDataPath);  //То что надо
        }else{  //Unix like
            pb.environment().put("LD_LIBRARY_PATH", ClientFolderPath+"bin"+ File.separator+"natives");
        }
        pb.directory(new File(ClientFolderPath));
        pb.redirectErrorStream(true);
        pb.redirectOutput(log);     
    try {
        Process process = pb.start();
    } catch (IOException ex) {
        Logger.getLogger(MCGameRuner.class.getName()).log(Level.SEVERE, null, ex);
    }
} 
}