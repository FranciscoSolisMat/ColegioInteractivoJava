/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore;


import xyz.theprogramsrc.colegiointeractivojava.superjcore.config.ConfigFile;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.logging.LogHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class JavaProject {

    private File dataFolder;
    private Class<?> mainClass;
    private ConfigFile systemSettings;
    private Set<ProjectModule> modules;
    private boolean firstStart = false;

    public JavaProject(Class<?> mainClass, String name, String version){
        this.modules = new HashSet<>();
        this.mainClass = mainClass;
        this.dataFolder = new File(name + "/");
        if(!this.dataFolder.exists()) {
            this.firstStart = true;
            this.dataFolder.mkdir();
        }
        this.systemSettings = new ConfigFile(this, "system.cfg");
        this.registerModule(new LogHandler(this));
        this.modules.forEach(ProjectModule::onModuleEnable);
        this.getSystemSettings().add("debug", false);
        this.getSystemSettings().set("name", name);
        this.getSystemSettings().set("version", version);
        this.onEnable();
    }

    public String getName(){
        return this.getSystemSettings().get("name");
    }

    public String getVersion(){
        return this.getSystemSettings().get("version");
    }

    public ConfigFile getSystemSettings() {
        return systemSettings;
    }

    public File getDataFolder() {
        return this.dataFolder;
    }

    public void stop(){
        this.stop(0);
    }

    public void stop(int status){
        this.onDisable();
        this.modules.forEach(ProjectModule::onModuleDisable);
        System.exit(status);
    }

    public void emergencyStop(){
        System.exit(-1);
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void registerModule(ProjectModule module){
        this.modules.add(module);
    }

    public boolean debug(){
        return this.getSystemSettings().getBoolean("debug");
    }

    public Class<?> getMainClass() {
        return mainClass;
    }

    public ProjectModule getModule(final String name){
        return this.modules.stream().filter(m->m.getName().equals(name) || m.getName().contains(name)).findFirst().orElse(null);
    }

    public LogHandler getLogHandler(){
        return ((LogHandler)this.getModule("LogHandler"));
    }

    public void log(String s){
        this.getLogHandler().log(s);
    }

    public void error(String s){
        this.getLogHandler().err(s);
    }

    public void warn(String s){
        this.getLogHandler().warn(s);
    }

    public void debug(String s){
        this.getLogHandler().debug(s);
    }

    public void debug(Exception ex){
        this.getLogHandler().debug(ex);
    }

    public boolean isFirstStart(){
        return this.firstStart;
    }
}
