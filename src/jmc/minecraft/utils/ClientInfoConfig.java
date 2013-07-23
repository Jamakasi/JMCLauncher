/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;

import static java.lang.String.format;

/**
 *
 * @author DimanA90
 */
public class ClientInfoConfig {
    private String info;
    private String[] addons;
    private String[] addonsinfo;
 
    /*
     * Get vars
     */
    public String getInfo() {
        return info;
    }
    public String[] getAddons() {
        return addons;
    }
    public String[] getAddonsinfo() {
        return addonsinfo;
    }
    /*
     * Set vars
     */
    public void setInfo(String Info) {
        this.info = Info;
    }
    public void setAddons(String[] Addons) {
        this.addons = Addons;
    }
    public void setAddonsinfo(String[] AddonsInfo) {
        this.addonsinfo = AddonsInfo;
    }
 
    @Override
    public String toString() {
        return new StringBuilder()
            .append( format( "Info: %s\n", info ) )
            //.append( format( "Addons: %s\n", addons ) )
            //.append( format( "AddonsInfo: %s\n", addonsinfo ) )
            .toString();
    }
}
