/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

//import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.yaml.snakeyaml.Yaml;


import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
          GlobalVar.HostUrl = config.getHost();
          
          //System.out.println(config.toString());
          in.close();
       }catch(Exception e){
           System.out.println("Error while loading core config, message: "+ e.getMessage());
           System.exit(0);
       }
    }
public void saveUserConfig()
{     
       LauncherConfig uoconfig = new LauncherConfig();
          
          uoconfig.setUsername(GlobalVar.userName);
          uoconfig.setPassword(GlobalVar.password);
          uoconfig.setRam(GlobalVar.Ram);
          uoconfig.setRamindex(GlobalVar.ramindex);
          uoconfig.setCurrentclient(GlobalVar.CurrentServer);
          uoconfig.setRamperm(GlobalVar.RamPerm);
          uoconfig.setRampermindex(GlobalVar.ramPermindex);

          String output = new Yaml().dumpAsMap(uoconfig);
        try 
        {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(Utils.getWorkingDirectory(), "config.yml")));
            dos.writeBytes(output);
            dos.close();
        } catch (IOException ex) 
        {
            Logger.getLogger(ConfigLoaderCore.class.getName()).log(Level.SEVERE, null, ex);
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
                                if (uconfig.getUsername()!=null)  GlobalVar.userName = uconfig.getUsername();
                                if (uconfig.getPassword()!=null)  GlobalVar.password = uconfig.getPassword();
                                if (uconfig.getRam()!=null)       GlobalVar.Ram = uconfig.getRam();
                                if (uconfig.getRamperm()!=null)   GlobalVar.RamPerm = uconfig.getRamperm();
                                if (uconfig.getRamindex()!=-1)    GlobalVar.ramindex = uconfig.getRamindex();
                                if (uconfig.getRampermindex()!=-1)GlobalVar.ramPermindex = uconfig.getRampermindex();
                                GlobalVar.CurrentServer = uconfig.getCurrentclient();
            }
          //System.out.println(uconfig.toString()); 
            in.close();
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
                                GlobalVar.oldminecarft =cconfig.getOldminecraft();
                                GlobalVar.addons = cconfig.getAddons();
                                GlobalVar.addonsinfo = cconfig.getAddonsinfo();
                                GlobalVar.ArchivesList = cconfig.getArchivesList();
                                 }
          //System.out.println(uconfig.toString()); 
            in.close();
       }catch(Exception e){
           System.out.println("Error while loading client info config, message: "+ e.getMessage());
           //System.exit(0);
       }
    }
}
