/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava;

import xyz.theprogramsrc.colegiointeractivojava.guis.Bienvenida;
import xyz.theprogramsrc.colegiointeractivojava.guis.LoginView;
import xyz.theprogramsrc.colegiointeractivojava.guis.MainView;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.JavaProject;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.config.ConfigFile;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.StringUtils;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.VirtualBrowser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class Main extends JavaProject {

    public static void main(String[] args) {
        new Main();
    }

    private static Main instance;
    public static Main getInstance() { return instance; }
    public String APP_INFORMATION;
    private File assetsFolder;
    private ConfigFile cacheFile;
    private VirtualBrowser browser;
    private List<String> imageFiles;

    public Main(){
        super(Main.class, "ColegioInteractivo", "1.0-SNAPSHOT");
    }

    public void onEnable() {
        instance = this;
        this.debug("Instancia cargada");
        this.browser = new VirtualBrowser();
        this.debug("Buscador Virtual creado");
        this.APP_INFORMATION = "ColegioInteractivo - 1.0.0";
        this.imageFiles = Arrays.asList("Next.png", "Donate.png", "Login.png", "Ok.png");
        this.debug("Archivos de Imagenes encontrados");
        this.cacheFile = new ConfigFile(this, this.getDataFolder(), "cache.cfg");
        this.debug("Cache creado");
        this.assetsFolder = new File(this.getDataFolder(), "assets/");
        if(!assetsFolder.exists()) assetsFolder.mkdir();
        if(assetsFolder.exists()){
            this.debug("Creada carpeta 'assets'");
            this.loadAssets();
        }else{
            this.error("Error al crear carpeta 'assets'");
            this.stop(-1);
        }
        this.log("Activado!");
        if(this.isFirstStart()){
            new Bienvenida();
        }else{
            if(!(this.getSystemSettings().contains("username") && this.getSystemSettings().contains("password"))){
                new LoginView();
            }else{
                new MainView();
            }
        }
    }

    public void onDisable() {
        this.warn("Si crees que estas obteniendo un error, recuerda enviarnos tu archivo latest.log");
        this.log("Disabled!");
    }

    public File getAssetsFolder() {
        return assetsFolder;
    }

    private void loadAssets(){
        try{
            for(final String path : this.imageFiles){
                InputStream in = this.getClass().getResourceAsStream("/assets/" + path);
                if(in == null){
                    this.error("No se encontró el archivo '/assets/" + path + "'");
                    this.warn("Si crees que es un error contacta al soporte");
                    this.emergencyStop();
                }else{
                    File out = new File(this.getAssetsFolder(), path);
                    if(!out.exists()) out.createNewFile();
                    Files.copy(in, Paths.get(out.toURI()), StandardCopyOption.REPLACE_EXISTING);
                    this.debug("Archivo '/assets/" + path + "' copiado.");
                }
            }
        }catch (Exception ex){
            this.debug(ex);
            this.emergencyStop();
        }
    }

    public Image getImage(final String path){
        try{
            String imagePath = this.imageFiles.stream().filter(s->s.contains(path)).findFirst().orElse(path.endsWith(".png") ? path : path.endsWith(".gif") ? path : (path + ".gif"));
            File file = new File(this.getAssetsFolder(), imagePath);
            return ImageIO.read(file);
        }catch (Exception ex){
            this.debug(ex);
            return null;
        }
    }

    public boolean canResize(String menuName, boolean defVal){
        String path = "resizable-" + menuName;
        if(!this.getSystemSettings().contains(path)){
            this.getSystemSettings().set(path, defVal);
        }

        return this.getSystemSettings().getBoolean(path);
    }

    public void invalidateCache(){
        this.cacheFile.clear();
        this.restart();
    }

    public void restart(){
        try{
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if(!currentJar.getName().endsWith(".jar")) return;
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            stop();
        }catch (Exception ex){
            this.debug(ex);
            this.emergencyStop();
        }
    }

    public static String LOGIN_URL = "http://app.colegiointeractivo.cl/sae3.0/session/chkUser.php?txtNOMBRE={USERNAME}&txtPW={PASSWORD}";

    public ConfigFile getCacheFile() {
        return cacheFile;
    }


    public String getUsername(){
        String path = "username";
        if(this.getSystemSettings().contains(path)){
            return this.getSystemSettings().get(path);
        }else if(this.getCacheFile().contains(path)){
            return this.getCacheFile().get(path);
        }else{
            return null;
        }
    }

    public String getPassword(){
        String path = "password";
        if(this.getSystemSettings().contains(path)){
            return this.getSystemSettings().get(path);
        }else if(this.getCacheFile().contains(path)){
            return this.getCacheFile().get(path);
        }else{
            return null;
        }
    }

    public String getLoginCookieName(){
        return "PHPSESSID";
    }

    public String getLoginCookieValue(){
        return StringUtils.decode(StringUtils.decode(this.getSystemSettings().get("LoginCookie")));
    }

    public VirtualBrowser getBrowser() {
        return browser;
    }
}

