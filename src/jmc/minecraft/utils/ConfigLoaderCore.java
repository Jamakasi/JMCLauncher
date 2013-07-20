/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 *
 * @author DimanA90
 */
public class ConfigLoaderCore  {
    
    public void loadConfig(){
       Yaml yaml = new Yaml(); 
       try {
          InputStream in = this.getClass().getResourceAsStream("/Configs/CoreConfig.yml");
          if (in == null){
                            System.out.println("File /Configs/CoreConfig.yml not found"); 
                            System.exit(0);
                         }
          CoreConfig config = yaml.loadAs(in, CoreConfig.class);
          GlobalVar.WorkDir = config.getWorkdir();
          GlobalVar.MainWndTitle = config.getMainwindowtitle();
          GlobalVar.ClientNames = config.getClientnames();
          GlobalVar.itemsServers = config.getClientdirnames();
          GlobalVar.NewsURL = config.getNewsurl();
          GlobalVar.Version = config.getVersion();
          GlobalVar.AuthURL = config.getAuthurl();
          GlobalVar.DownloadNewLauncherURL = config.getLauncherdownloadlink();
          GlobalVar.DownloadClientRootURL = config.getClientdownloadroot();
          
          //System.out.println(config.toString());
          
       }catch(Exception e){
           System.out.println("Error while loading core config, message: "+ e.getMessage());
           System.exit(0);
       }
    }
    
}
