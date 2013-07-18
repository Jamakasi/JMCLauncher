package net.minecraft;

import java.applet.Applet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
//import java.net.JarURLConnection;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
//import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Properties;
//import java.util.StringTokenizer;
import java.util.Vector;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import SevenZip.LzmaAlone;
import static net.minecraft.Util.getWorkingDirectory;
//import com.sun.xml.internal.ws.util.StringUtils;

public class GameUpdater
  implements Runnable
{
  public static final int STATE_INIT = 1;
  public static final int STATE_DETERMINING_PACKAGES = 2;
  public static final int STATE_CHECKING_CACHE = 3;
  public static final int STATE_DOWNLOADING = 4;
  public static final int STATE_EXTRACTING_PACKAGES = 5;
  public static final int STATE_UPDATING_CLASSPATH = 6;
  public static final int STATE_SWITCHING_APPLET = 7;
  public static final int STATE_INITIALIZE_REAL_APPLET = 8;
  public static final int STATE_START_REAL_APPLET = 9;
  public static final int STATE_DONE = 10;
public int percentage;
  public int currentSizeDownload;
  public int totalSizeDownload;
  public int currentSizeExtract;
  public int totalSizeExtract;
  protected URL[] urlList;
  private static ClassLoader classLoader;
  protected Thread loaderThread;
  protected Thread animationThread;
  public boolean fatalError;
  public String fatalErrorDescription;
  protected String subtaskMessage = "";
  protected int state = 1;

  protected boolean lzmaSupported = false;
  protected boolean pack200Supported = false;

  protected String[] genericErrorMessage = { 
    "An error occured while loading the applet.", "Please contact support to resolve this issue.", "<placeholder for error message>" };
  protected boolean certificateRefused;
  protected String[] certificateRefusedMessage = { 
    "Permissions for Applet Refused.", "Please accept the permissions dialog to allow", "the applet to continue the loading process." };

  protected static boolean natives_loaded = false;
  public static boolean forceUpdate = false;
  private String latestVersion;
  private String mainGameUrl;
  public boolean pauseAskUpdate;
  public boolean shouldUpdate;
  
  
  public GameUpdater(String latestVersion, String mainGameUrl)
  {
    this.latestVersion = latestVersion;
    this.mainGameUrl = mainGameUrl;
  }

  public void init() {
    state = 1;
   /* try
    {
      Class.forName("LZMA.LzmaInputStream");
      lzmaSupported = true;
    }
    catch (Throwable localThrowable) {
    }
    try {
      Pack200.class.getSimpleName();
      pack200Supported = true;
    } catch (Throwable localThrowable1) {
    }*/
  }

  private String generateStacktrace(Exception exception) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    exception.printStackTrace(printWriter);
    return result.toString();
  }

  protected String getDescriptionForState()
  {
    switch (state) {
    case 1:
      return "Инициализация загрузчика";
    case 2:
      return "Обнаружение пакетов для скачки";
    case 3:
      return "Проверка кеш-файлов";
    case 4:
      return "Скачивание пакетов";
    case 5:
      return "Извлечение скачанных пакетов";
    case 6:
      return "Обновление путей";
    case 7:
      return "Сворачивание апплета";
    case 8:
      return "Инициализация реального апплета";
    case 9:
      return "Старт реального апплета";
    case 10:
      return "Загрузка завершена";
    }
    return "Неизвестное положение";
  }

  protected void loadJarURLs() throws Exception {
    state = 2;
    
    //#Внимание на сервере должен быть файл client.zip, даже пустой!
 //   String jarList = "client.zip, " + mainGameUrl;

    //# Откуда скачивать
    URL path = new URL((Config.CDwlUrl+Config.workFolderServers[Config.CurrentServer])+ '/');
    //System.out.println(path.toString());
     
    //Слишком топорно, переписать позже
    /*BrainUpdater BUpd = new BrainUpdater();
    int counter = 0;
    if (!BUpd.CheckMD5Matches("client.zip")){
       counter++;     
    }
    if (!BUpd.CheckMD5Matches("custom.zip")){
       counter++;     
    }
    if (!BUpd.CheckMD5Matches("minecraft.jar")){
       counter++;     
    }
    urlList = new URL[counter];  //3
    for (int i =0; i<=counter-1;i++){
     
                if (!BUpd.CheckMD5Matches("client.zip")){
                urlList[i] = new URL(path, "client.zip");     
                }
                if (!BUpd.CheckMD5Matches("custom.zip")){
                urlList[i] = new URL(path, "custom.zip");   
                }
                if (!BUpd.CheckMD5Matches("minecraft.jar")){
                urlList[i] = new URL(path, "minecraft.jar");     
                }
    }*/
    urlList = new URL[3];
    urlList[0] = new URL(path, "client.zip");     
    urlList[1] = new URL(path, "custom.zip");   
    urlList[2] = new URL(path, "minecraft.jar");   
    
    //String osName = System.getProperty("os.name");
  }

  public void run()
  {
    init();
    state = 3;

    percentage = 5;
    try
    {
      loadJarURLs();

      String path = (String)AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
        public Object run() throws Exception {
          return Util.getWorkingDirectory() + File.separator + Config.workFolderServers[Config.CurrentServer] + File.separator + "bin" + File.separator;
        }
      });
      File dir = new File(path);

      if (!dir.exists()) {
        dir.mkdirs();
      }
      
      if (latestVersion != null) {
        File versionFile = new File(dir, "version");
       
        boolean cacheAvailable = false;
        if ((!forceUpdate) && (versionFile.exists()) && (
          (latestVersion.equals("-1")) || (latestVersion.equals(readVersionFile(versionFile))))) {
          cacheAvailable = true;
          percentage = 90;
        }

        if ((forceUpdate) || (!cacheAvailable)) {
          shouldUpdate = true;
          if ((!forceUpdate) && (versionFile.exists()))
          {
            checkShouldUpdate();
          }
          if (shouldUpdate)
          {
            writeVersionFile(versionFile, "");

            downloadJars(path);
            extractJars(path);

            if (latestVersion != null) {
              percentage = 90;
              writeVersionFile(versionFile, latestVersion);
            }
          } else {
            cacheAvailable = true;
            percentage = 90;
          }
        }
      }

      updateClassPath(dir);
      state = 10;
    } catch (AccessControlException ace) {
      fatalErrorOccured(ace.getMessage(), ace);
      certificateRefused = true;
    } catch (Exception e) {
      fatalErrorOccured(e.getMessage(), e);
    } finally {
      loaderThread = null;
    }
  }



private void checkShouldUpdate() {
    pauseAskUpdate = true;
    while (pauseAskUpdate)
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
  }

  protected String readVersionFile(File file) throws Exception
  {
    DataInputStream dis = new DataInputStream(new FileInputStream(file));
    String version = dis.readUTF();
    dis.close();
    return version;
  }

  protected void writeVersionFile(File file, String version) throws Exception {
    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
    dos.writeUTF(version);
    dos.close();
  }

  protected void updateClassPath(File dir)
    throws Exception
  {
    state = 6;

    percentage = 95;

    URL[] urls = new URL[urlList.length];
    for (int i = 0; i < urlList.length; i++) {
      urls[i] = new File(dir, getJarName(urlList[i])).toURI().toURL();
      //System.out.println("JARURL " + urls[i].toString());
    }

    if (classLoader == null) {
      classLoader = new URLClassLoader(urls) {
        protected PermissionCollection getPermissions(CodeSource codesource) {
          PermissionCollection perms = null;
          try
          {
            Method method = SecureClassLoader.class.getDeclaredMethod("getPermissions", new Class[] { 
              CodeSource.class });

            method.setAccessible(true);
            perms = (PermissionCollection)method.invoke(getClass().getClassLoader(), new Object[] { 
              codesource });

            String host = Config.host;

            if ((host != null) && (host.length() > 0))
            {
              perms.add(new SocketPermission(host, "connect,accept"));
            } else codesource.getLocation().getProtocol().equals("file");

            perms.add(new FilePermission("<<ALL FILES>>", "read"));
          }
          catch (Exception e) {
            e.printStackTrace();
          }

          return perms;
        }
      };
    }
    String path = dir.getAbsolutePath();
    if (!path.endsWith(File.separator)) path = path + File.separator;
    unloadNatives(path);
System.out.println("ПУТЬ К "+ path);
    System.setProperty("org.lwjgl.librarypath", path + "natives");
    System.setProperty("net.java.games.input.librarypath", path + "natives");

    natives_loaded = true;
  }

  private void unloadNatives(String nativePath)
  {
    if (!natives_loaded) {
      return;
    }
    try
    {
      Field field = ClassLoader.class.getDeclaredField("loadedLibraryNames");
      field.setAccessible(true);
      Vector<?> libs = (Vector<?>)field.get(getClass().getClassLoader());

      String path = new File(nativePath).getCanonicalPath();

      for (int i = 0; i < libs.size(); i++) {
        String s = (String)libs.get(i);

        if (s.startsWith(path)) {
          libs.remove(i);
          i--;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
//При чтении следующего метода у вас может возникнуть приступ паники.
  private void RunMC16(){
      try{
    
    String jarpath = getWorkingDirectory()+ File.separator + Config.workFolderServers[Config.CurrentServer] + File.separator + "bin" + File.separator;
    String workdir = getWorkingDirectory()+ File.separator + Config.workFolderServers[Config.CurrentServer] + File.separator;
    //String runparam = "java -cp "+jarpath+"*"+" -Djava.library.path="+jarpath+"natives"+ File.separator +" net.minecraft.client.main.Main --username "+Config.Uname+" --session "+Config.Usession+" --version "+Config.Uversion ;     
    String runparam = "java -cp "+jarpath+"*"+" -Djava.library.path="+jarpath+"natives"+ File.separator +" net.minecraft.client.main.Main --username "+Config.Uname+" --session "+Config.Usession+" --version "+Config.Uversion ;     
    
    
    Runtime r = Runtime.getRuntime();
    r.exec(runparam, null, new File(workdir));
    System.out.println(runparam);
    System.exit(1);
      }catch(Exception e){
      
      
      }  
  }
  
  public Applet createApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    try{
      Class<?> appletClass = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
    return (Applet)appletClass.newInstance();
  }catch(Exception e){
        RunMC16();
      Class<?> appletClass = classLoader.loadClass("net.minecraft.client.main.Main");
    return (Applet)appletClass.newInstance();
    }
  }  
  
  protected void downloadJars(String path)
    throws Exception
  {
    File versionFile = new File(path, "md5s");
    Properties md5s = new Properties();
    
    BrainUpdater BUpd = new BrainUpdater();
    boolean[] skipdwl = new boolean[3];
    String tempstr = new String();
    String tempstrt = new String();
    
    
    if (versionFile.exists()) {
      try {
        FileInputStream fis = new FileInputStream(versionFile);
        md5s.load(fis);
        fis.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    state = 4;

    int[] fileSizes = new int[urlList.length];
    boolean[] skip = new boolean[urlList.length];

    for (int i = 0; i < urlList.length; i++) {
      URLConnection urlconnection = urlList[i].openConnection();
      urlconnection.setDefaultUseCaches(false);
      skip[i] = false;
      if ((urlconnection instanceof HttpURLConnection)) {
        ((HttpURLConnection)urlconnection).setRequestMethod("HEAD");

        int code = ((HttpURLConnection)urlconnection).getResponseCode();
        if (code / 100 == 3) {
          skip[i] = true;
        }
        //brainupdater
        //skipdwl[i] = false;
        tempstr =urlList[i].getFile();
        tempstrt = tempstr.substring( tempstr.lastIndexOf('/')+1, tempstr.length() );
        if (BUpd.CheckMD5Matches(tempstrt)!= false) {
            
          skip[i] = true;
          //System.out.println("Пропущено из списка загрузок" + i);
        }
        //System.out.println("ИЗВЛЕЧЕННОЕ ИМЯ ФАЙЛА ИЗ ССЫЛКИ ="+tempstrt+"|");
      }
      fileSizes[i] = urlconnection.getContentLength();
      totalSizeDownload += fileSizes[i];
    }
    int initialPercentage = this.percentage = 10;

    byte[] buffer = new byte[65536];
    for (int i = 0; i < urlList.length; i++) {
        boolean downloadFile = true;
      if (skip[i] != false) {
        percentage = (initialPercentage + fileSizes[i] * 45 / totalSizeDownload);
      //}
      
        downloadFile = false;
      }
        while (downloadFile) {
          downloadFile = false;

          URLConnection urlconnection = urlList[i].openConnection();

          String etag = "";

          if ((urlconnection instanceof HttpURLConnection)) {
            urlconnection.setRequestProperty("Cache-Control", "no-cache");

            urlconnection.connect();

            etag = urlconnection.getHeaderField("ETag");
          }

          String currentFile = getFileName(urlList[i]);
          
          
          InputStream inputstream = getJarInputStream(currentFile, urlconnection);
          
          FileOutputStream fos = new FileOutputStream(path + currentFile);

          long downloadStartTime = System.currentTimeMillis();
          int downloadedAmount = 0;
          int fileSize = 0;
          String downloadSpeedMessage = "";

          MessageDigest m = MessageDigest.getInstance("MD5");
          int bufferSize;
          while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1)
          {

            fos.write(buffer, 0, bufferSize);

            m.update(buffer, 0, bufferSize);
            currentSizeDownload += bufferSize;
            fileSize += bufferSize;
            percentage = (initialPercentage + currentSizeDownload * 45 / totalSizeDownload);
            subtaskMessage = ("Загрузка: " + currentFile + " " + currentSizeDownload * 100 / totalSizeDownload + "%");

            downloadedAmount += bufferSize;
            long timeLapse = System.currentTimeMillis() - downloadStartTime;

            if (timeLapse >= 1000L) {
              float downloadSpeed = downloadedAmount / (float)timeLapse;
              downloadSpeed = (int)(downloadSpeed * 100.0F) / 100.0F;
              downloadSpeedMessage = " @ " + downloadSpeed + " KB/sec";
              downloadedAmount = 0;
              downloadStartTime += 1000L;
            }

            subtaskMessage += downloadSpeedMessage;
          }

          inputstream.close();
          fos.close();

          }
          }
    
    }

  protected InputStream getJarInputStream(String currentFile, final URLConnection urlconnection)
    throws Exception
  {
    final InputStream[] is = new InputStream[1];

    for (int j = 0; (j < 3) && (is[0] == null); j++) {
      Thread t = new Thread() {
        public void run() {
          try {
            is[0] = urlconnection.getInputStream();
          }
          catch (IOException localIOException)
          {
          }
        }
      };
      t.setName("JarInputStreamThread");
      t.start();

      int iterationCount = 0;
      while ((is[0] == null) && (iterationCount++ < 5)) {
        try {
          t.join(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
      if (is[0] != null) continue;
      try {
        t.interrupt();
        t.join();
      }
      catch (InterruptedException localInterruptedException1)
      {
      }
    }

    if (is[0] == null) {
      if (currentFile.equals("minecraft.jar")) {
        throw new Exception("Unable to download " + currentFile);
      }
      throw new Exception("Unable to download " + currentFile);
    }
    return is[0];
  }

// from AnjoCaido launcher  
  protected void extractLZMA(String in, String out) throws Exception
  {
    File f = new File(in);
    File fout = new File(out);
    LzmaAlone.decompress(f, fout);
    f.delete();
  }

  protected void extractPack(String in, String out)
    throws Exception
  {
    File f = new File(in);
    if (!f.exists()) return;

    FileOutputStream fostream = new FileOutputStream(out);
    JarOutputStream jostream = new JarOutputStream(fostream);

    Pack200.Unpacker unpacker = Pack200.newUnpacker();
    unpacker.unpack(f, jostream);
    jostream.close();

    f.delete();
  }

  protected void extractJars(String path)
    throws Exception
  {
    state = 5;
    
    float increment = 10.0F / urlList.length;

          percentage = (55 + (int)(increment * 1));
          subtaskMessage = ("Extracting: Client files");
    UnZip("client.zip");
          percentage = (55 + (int)(increment * 2));
          subtaskMessage = ("Extracting: custom content");
    UnZip("custom.zip");
  }


 protected String getJarName(URL url)
  {
    String fileName = url.getFile();

    if (fileName.contains("?")) {
      fileName = fileName.substring(0, fileName.indexOf("?"));
    }
    if (fileName.endsWith(".pack.lzma"))
      fileName = fileName.replaceAll(".pack.lzma", "");
    else if (fileName.endsWith(".pack"))
      fileName = fileName.replaceAll(".pack", "");
    else if (fileName.endsWith(".lzma")) {
      fileName = fileName.replaceAll(".lzma", "");
    }

    return fileName.substring(fileName.lastIndexOf('/') + 1);
  }

  protected String getFileName(URL url) {
    String fileName = url.getFile();
    if (fileName.contains("?")) {
      fileName = fileName.substring(0, fileName.indexOf("?"));
    }
    return fileName.substring(fileName.lastIndexOf('/') + 1);
  }

  protected void fatalErrorOccured(String error, Exception e) {
    e.printStackTrace();
    fatalError = true;
    fatalErrorDescription = ("Fatal error occured (" + state + "): " + error);
    System.out.println(fatalErrorDescription);

    System.out.println(generateStacktrace(e));
  }

  public boolean canPlayOffline()
  {
    try
    {
      String path = (String)AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
        public Object run() throws Exception {
          return Util.getWorkingDirectory() + File.separator + Config.workFolderServers[Config.CurrentServer] + File.separator + "bin" + File.separator;
        }
      });
      File dir = new File(path);
      if (!dir.exists()) return false;

      dir = new File(dir, "version");
      if (!dir.exists()) return false;

      if (dir.exists()) {
        String version = readVersionFile(dir);
        if ((version != null) && (version.length() > 0))
          return true;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }
/**
 * Разархивирует файл client.zip из папки bin в .minecraft
 * @author ddark008
 * @throws PrivilegedActionException
 */
protected void UnZip(String ZipName) throws PrivilegedActionException
  {
    String szZipFilePath;
    String szExtractPath;
    String path = (String)AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
        public Object run() throws Exception {
          return Util.getWorkingDirectory() + File.separator + Config.workFolderServers[Config.CurrentServer] + File.separator;
        }
      }); 
    int i;
    
    szZipFilePath = path + "bin" + File.separator + ZipName;
      
    File f = new File(szZipFilePath);
    if(!f.exists())
    {
      System.out.println(
	"\nNot found: " + szZipFilePath);
    }
      
    if(f.isDirectory())
    {
      System.out.println(
	"\nNot file: " + szZipFilePath);
    }
    
    //System.out.println("Enter path to extract files: ");
    szExtractPath = path;
    
    File f1 = new File(szExtractPath);
    if(!f1.exists())
    {
      System.out.println(
	"\nNot found: " + szExtractPath);
    }
      
    if(!f1.isDirectory())
    {
      System.out.println(
	"\nNot directory: " + szExtractPath);
    }
    
    ZipFile zf; 
    Vector zipEntries = new Vector();
       
    try
    {  
      zf = new ZipFile(szZipFilePath);    
      Enumeration en = zf.entries();
      
      while(en.hasMoreElements())
      {
        zipEntries.addElement(
	  (ZipEntry)en.nextElement());
      }
      
      for (i = 0; i < zipEntries.size(); i++)
      {
        ZipEntry ze = 
	  (ZipEntry)zipEntries.elementAt(i);
	  
        extractFromZip(szZipFilePath, szExtractPath,
	  ze.getName(), zf, ze);
      }
      
      zf.close();
      //System.out.println("Done!");
    }
    catch(Exception ex)
    {
      System.out.println(ex.toString());
    }
    f.delete();
  }
  
  // ============================================
  // extractFromZip
  // ============================================
  static void extractFromZip(
    String szZipFilePath, String szExtractPath,
    String szName,
    ZipFile zf, ZipEntry ze)
  {
    if(ze.isDirectory())
      return;
      
    String szDstName = slash2sep(szName);
    
    String szEntryDir;
    
    if(szDstName.lastIndexOf(File.separator) != -1)
    {
      szEntryDir =
        szDstName.substring(
	  0, szDstName.lastIndexOf(File.separator));
    }
    else	  
      szEntryDir = "";
    
    //System.out.print(szDstName);
    long nSize = ze.getSize();
    long nCompressedSize = 
      ze.getCompressedSize();
    
    //System.out.println(" " + nSize + " (" +      nCompressedSize + ")");  
  
    try
    {
       File newDir = new File(szExtractPath +
	 File.separator + szEntryDir);
	 
       newDir.mkdirs();	 
       
       FileOutputStream fos = 
	 new FileOutputStream(szExtractPath +
	 File.separator + szDstName);

       InputStream is = zf.getInputStream(ze);
       byte[] buf = new byte[1024];

       int nLength;
       
       while(true)
       {
         try
         {
	   nLength = is.read(buf);
         }	 
         catch (EOFException ex)
         {
	   break;
	 }  
	 
         if(nLength < 0) 
	   break;
         fos.write(buf, 0, nLength);
       }
       
       is.close();
       fos.close();
    }   
    catch(Exception ex)
    {
      System.out.println(ex.toString());
      //System.exit(0);
    }
  }  
  // ============================================
  // slash2sep
  // ============================================
  static String slash2sep(String src)
  {
    int i;
    char[] chDst = new char[src.length()];
    String dst;
    
    for(i = 0; i < src.length(); i++)
    {
      if(src.charAt(i) == '/')
        chDst[i] = File.separatorChar;
      else
        chDst[i] = src.charAt(i);
    }
    dst = new String(chDst);
    return dst;
  }
  

}