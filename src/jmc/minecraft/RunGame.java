/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import jmc.minecraft.utils.ConfigLoaderCore;
import jmc.minecraft.utils.GlobalVar;
import jmc.minecraft.utils.MCGameRuner;
import jmc.minecraft.utils.Updater;
import jmc.minecraft.utils.Utils;

/**
 * 
 * @author DimanA90
 */
public class RunGame {
   
 public static void Init(final JProgressBar progressCurrent,final JProgressBar progressBarTotal)
 {
   if(GlobalVar.isOnline)// is Online?
   {
     if(Utils.login(GlobalVar.userName, GlobalVar.password))//Login succes ?
     {
         
        Thread myThready = new Thread(new Runnable()
        {
            public void run()
            {
         
         ConfigLoaderCore cfsaver = new ConfigLoaderCore();
         cfsaver.saveUserConfig();
         Updater gup = new Updater();
         gup.Init(progressCurrent,progressBarTotal);
                MCGameRuner grun = new MCGameRuner();
                grun.LetsGame();
                System.exit(1);
            }
        });
        myThready.start();
         
     }else
     {//Ничего не делаем
     };         
   }else  //Offline, run game offline
   {
       
   }
 }

}
