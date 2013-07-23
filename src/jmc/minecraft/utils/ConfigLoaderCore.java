/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

import java.io.File;
import java.io.FileInputStream;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 *
 * @author DimanA90
 */
/*
 * Load core config for launcher
 */
public class ConfigLoaderCore  {
    
    public void loadCoreConfig(){
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
          GlobalVar.RegURL = config.getRegurl();
          
          //System.out.println(config.toString());
          
       }catch(Exception e){
           System.out.println("Error while loading core config, message: "+ e.getMessage());
           System.exit(0);
       }
    }
    
    
/*
 * Load user config for launcher
 */    
public void LoadUserConfig(){
       Yaml yamlu = new Yaml(); 
       try {
           InputStream in = new FileInputStream(new File(Utils.getWorkingDirectory()+File.separator+"config.yml"));
            if (in == null) {
                                System.out.println("File config.yml not found, used default");                        
                            }else{
                                LauncherConfig uconfig = yamlu.loadAs(in, LauncherConfig.class);
                                GlobalVar.userName = uconfig.getUsername();
                                GlobalVar.password = uconfig.getPassword();
                                GlobalVar.Ram = uconfig.getRam();
                                GlobalVar.CurrentServer = uconfig.getCurrentclient();
                                 }
          //System.out.println(uconfig.toString()); 
       }catch(Exception e){
           System.out.println("Error while loading user config, message: "+ e.getMessage());
           //System.exit(0);
       }
    }
   /*
    * Load current client info
    */ 
public void LoadClientConfig(String clientname){
       Yaml yamlc = new Yaml(); 
       try {
           InputStream in = this.getClass().getResourceAsStream("/Configs/clientinfo/"+clientname+".yml");
            if (in == null) {
                                System.out.println("Error: "+File.separator+"Configs"+File.separator+"clientinfo"+File.separator+clientname+".yml not found");                        
                            }else{
                                ClientInfoConfig cconfig = yamlc.loadAs(in, ClientInfoConfig.class);
                                GlobalVar.clientinfo = cconfig.getInfo();
                                GlobalVar.addons = cconfig.getAddons();
                                GlobalVar.addonsinfo = cconfig.getAddonsinfo();
                                 }
          //System.out.println(uconfig.toString()); 
       }catch(Exception e){
           System.out.println("Error while loading client info config, message: "+ e.getMessage());
           //System.exit(0);
       }
    }
}
