/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft;

import java.net.URL;
/**
 *
 * @author DimanA90
 */
public class Config {
    //Версия лаунчера (хранится в БД!!!!)
 public static int Version = 13;   
    //Название папки где хранятся клиенты в фс
  public static String localworkdir = "pantheria";
  //Ссылка до новостей
 public static String NewsURL = "http://pantheria.ru/MineCraft/news.php"; 
 //Ссылка до регистрации
 public static String RegURL = "http://pantheria.ru/index.php?mode=start";
 //Ссылка до страницы скачивания лаунчера
 public static String LDwlURL = "http://pantheria.ru/index.php?mode=start";
 //Ссылка до скрипта авторизации, для webmcr auth.php
 public static String AuthURL = "http://pantheria.ru/MineCraft/auth.php";
 //Директория на сервере где располагаются папки загрузок для каждого клиента
 public static String CDwlUrl = "http://pantheria.ru/MineCraft/MinecraftDownload/NewLauncher/";
 //Название окна после запуска игры
 public static String WndTitle = "Pantheria Minecraft";
 //Список клиентов для выбора 
 public static  String[] itemsServers = {
    "Techno 1.4.7",
    "Vanila 1.6.2"
};
 //Название папки в фс для каждого клиента и папки с дистрибутивом на сервере
  public static  String[] workFolderServers = {
    "techno147",
    "vanila162"
};

  
  //do not touch
  public static int CurrentServer;
  
}
