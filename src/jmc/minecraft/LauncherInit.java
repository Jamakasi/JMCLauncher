/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft;

import jmc.minecraft.utils.ConfigLoaderCore;

/**
 *
 * @author DimanA90
 */
public class LauncherInit /*extends Frame */
{
    //public Map<String, String> CoreParameters = new HashMap<String, String>();
    private static final long serialVersionUID = 1L;
    
    public static void main(String[] args) {
        ConfigLoaderCore cf = new ConfigLoaderCore();
        cf.loadConfig();  //Load core properties
        
        GUIMainFrame gui = new GUIMainFrame();
        gui.setVisible(true);
        LauncherInit LInit= new LauncherInit();
        

        System.out.println("Отработал");
    }
}
