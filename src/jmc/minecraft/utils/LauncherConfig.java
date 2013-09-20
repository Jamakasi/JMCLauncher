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
public class LauncherConfig {

    private String username;
    private String password;
    private String ram;
    private int ramindex;
    private String ramperm;
    private int rampermindex;
    private int currentclient;
 
    /*
     * Get vars
     */
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRam() {
        return ram;
    }
    public String getRamperm() {
        return ramperm;
    }
    public int getCurrentclient() {
        return currentclient;
    }
    public int getRamindex() {
        return ramindex;
    }
    public int getRampermindex() {
        return rampermindex;
    }
    /*
     * Set vars
     */
    public void setUsername(String Username) {
        this.username = Username;
    }
    public void setPassword(String Password) {
        this.password = Password;
    }
    public void setRam(String Ram) {
        this.ram = Ram;
    }
    public void setRamperm(String Ramperm) {
        this.ramperm = Ramperm;
    }
    public void setCurrentclient(int Currentclient) {
        this.currentclient = Currentclient;
    }
    public void setRamindex(int Ramindex) {
        this.ramindex = Ramindex;
    }
    public void setRampermindex(int Rampermindex) {
        this.rampermindex = Rampermindex;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append( format( "Version: %s\n", username ) )
            .append( format( "WorkDir: %s\n", password ) )
            .append( format( "NewsUrl: %s\n", ram ) )
            .toString();
    }
}
