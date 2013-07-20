/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft;

import java.io.File;
import jmc.minecraft.utils.GlobalVar;
import jmc.minecraft.utils.Utils;


/**
 *
 * @author DimanA90
 * Check and run update game if available then run game
 */
public class GameLaunch {
    
public void Run()
    {
        //System.out.println("Определение метода запуска");

        //GameUpdater gu = new GameUpdater();
        if (isNewLaunchMethod()){
            RunNewMethod();
        } else 
        {
            RunOldMethod();
        }
    }


/*
 * Check launch method, false if old method
 */
private boolean isNewLaunchMethod() 
  {
    try{
        
        File file = new File(Utils.getWorkingDirectory()+ File.separator + GlobalVar.itemsServers[GlobalVar.CurrentServer] + File.separator+"asserts");  //Remove me
        
        if (file.exists()){
            System.out.println("New launch method");
        return true;
        }else {
            System.out.println("Old launch method");
            return true;
        }
    
    }catch (Exception e) {
        System.out.println("Error check launch method, set to old");    
        return false;
    }  
  }

private void RunOldMethod()
    {
        System.out.println("Заглушка запуска старых версий");
    }
private void RunNewMethod()
    {
        String WorkDir = Utils.getWorkingDirectory()+ File.separator + GlobalVar.itemsServers[GlobalVar.CurrentServer] + File.separator;  //Path to %appdata%/.serv_name/cur_client/
        String LibPath = "bin"+ File.separator; //Get all jar lib  //String LibPath = "bin"+ File.separator+"*"; //Get all jar lib
        String LibPatht = "bin"+ File.separator; //Get all jar lib
        /*String LibPath = LibPatht+"argo.jar;"+LibPatht+"bcprov.jar;"+LibPatht+"codecjorbis.jar;"+LibPatht+"codecwav.jar;"+LibPatht+"commons-io.jar;"
                +LibPatht+"commons-lang.jar;"+LibPatht+"gson.jar;"+LibPatht+"guava.jar;"+LibPatht+"jinput.jar;"
                +LibPatht+"jopt-simple.jar;"+LibPatht+"jutils.jar;"+LibPatht+"libraryjavasound.jar;"
                +LibPatht+"librarylwjglopenal.jar;"+LibPatht+"lwjgl.jar;"+LibPatht+"lwjgl_util.jar;"+LibPatht+"soundsystem.jar;"; */
        String NativePath = "bin"+ File.separator+"native";
        
        ProcessBuilder pb = new ProcessBuilder("java", "-cp",LibPath, 
        "-Djava.library.path="+NativePath,
        "net.minecraft.client.main.Main",
        "--username", GlobalVar.userName,
        "--session", GlobalVar.sessionId,
        "--version", GlobalVar.Version,
        "--assetsDir", "assets");
        
        pb.directory(new File(WorkDir));
             File log = new File(WorkDir+"log");

             System.out.println(pb.command().toString());
        try{
        pb.redirectErrorStream(true);
        pb.redirectOutput(log);     
  
        Process pr = pb.start();
     
        assert pb.redirectOutput().file() == log;
        assert pr.getInputStream().read() == -1;
        System.out.println("Запущено новым методом");
    //System.exit(1);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
