/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore;


import xyz.theprogramsrc.colegiointeractivojava.superjcore.config.ConfigFile;

import java.io.File;

public abstract class ProjectModule {

    private JavaProject core;

    public abstract String getName();

    public ProjectModule(JavaProject core){
        this.core = core;
    }

    public JavaProject getCore() {
        return core;
    }

    public void onModuleEnable(){

    }

    public void onModuleDisable(){

    }

    protected File getDataFolder() {
        return this.core.getDataFolder();
    }

    protected boolean debug(){
        return this.core.debug();
    }

    protected ConfigFile getSystemSettings(){
        return this.core.getSystemSettings();
    }

    protected Class<?> getMainClass(){
        return this.getCore().getMainClass();
    }
}
