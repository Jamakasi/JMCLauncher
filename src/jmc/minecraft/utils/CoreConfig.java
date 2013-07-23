/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.minecraft.utils;


import static java.lang.String.format;

import java.util.Date;

/**
 * @author DimanA90
 * @Bean standart
 * @Get all core parametrs
 */
public class CoreConfig {
    private Date released;
    private String version;
    private String workdir;
    private String mainwindowtitle;
    private String newsurl;
    private String authurl;
    private String regurl;
    private String launcherdownloadlink;
    private String clientdownloadroot;
    private String[] clientnames;
    private String[] clientdirnames;
 
    /*
     * Get
     */
    public Date getReleased() {
        return released;
    }
    public String getVersion() {
        return version;
    }
    public String getWorkdir() {
        return workdir;
    }
    public String getMainwindowtitle() {
        return mainwindowtitle;
    }
    public String[] getClientnames() {
        return clientnames;
    }
    public String[] getClientdirnames() {
        return clientdirnames;
    }
    public String getNewsurl() {
        return newsurl;
    }
    public String getAuthurl() {
        return authurl;
    }
    public String getRegurl() {
        return regurl;
    }
    public String getLauncherdownloadlink() {
        return launcherdownloadlink;
    }
    public String getClientdownloadroot() {
        return clientdownloadroot;
    }
    
    /*
     * Set
     */
    public void setReleased(Date released) {
        this.released = released;
    }
 
    public void setVersion(String version) {
        this.version = version;
    }
    public void setWorkdir(String workdir) {
        this.workdir = workdir;
    }
    public void setMainwindowtitle(String Mainwindowtitle) {
        this.mainwindowtitle = Mainwindowtitle;
    }
    public void setNewsurl(String newsurl) {
        this.newsurl = newsurl;
    }

    public void setClientnames(String[] Clientnames) {
        this.clientnames = Clientnames;
    }
    public void setClientdirnames(String[] Clientdirnames) {
        this.clientdirnames = Clientdirnames;
    }
     public void setAuthurl(String Authurl) {
        this.authurl = Authurl;
    }
     public void setRegurl(String Regurl) {
        this.regurl = Regurl;
    }
    public void setLauncherdownloadlink(String Launcherdownloadlink) {
        this.launcherdownloadlink = Launcherdownloadlink;
    }
    public void setClientdownloadroot(String Clientdownloadroot) {
        this.clientdownloadroot = Clientdownloadroot;
    }
 
 
    @Override
    public String toString() {
        return new StringBuilder()
            .append( format( "Version: %s\n", version ) )
            .append( format( "Released: %s\n", released ) )
            .append( format( "WorkDir: %s\n", workdir ) )
            .append( format( "NewsUrl: %s\n", newsurl ) )
            .append( format( "AuthUrl: %s\n", authurl ) )
            .append( format( "Reg url: %s\n", regurl ) )
            .append( format( "launcher download link: %s\n", launcherdownloadlink ) )
            .append( format( "client download root: %s\n", clientdownloadroot ) )
            .toString();
    }
}
