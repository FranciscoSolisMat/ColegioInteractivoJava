/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.config;


import xyz.theprogramsrc.colegiointeractivojava.superjcore.JavaProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

public class ConfigFile {

    private JavaProject core;
    private Properties props;
    private File file;

    public ConfigFile(JavaProject core, String name){
        this(core, new File(core.getDataFolder(), name));
    }

    public ConfigFile(JavaProject core, File folder, String name){
        this(core, new File(folder, name));
    }

    public ConfigFile(JavaProject core, File file){
        this.core = core;
        this.file = file;
        this.load();
    }

    private void load() {
        this.props = new Properties();
        if(!this.file.exists()){
            try{
                this.file.createNewFile();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            try (FileInputStream inputStream = new FileInputStream(this.file)){
                this.props.load(inputStream);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void save(){
        try(FileOutputStream out = new FileOutputStream(this.file)){
            this.props.store(out, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String get(String key){
        return this.props.getProperty(key);
    }

    public boolean getBoolean(String key) {
        return this.get(key).toLowerCase().equals("true");
    }

    public int getInt(String key){
        return Integer.parseInt(this.get(key));
    }

    public void set(String key, String value){
        this.props.setProperty(key,value);
        this.save();
    }

    public void add(String key, String value){
        if(!this.contains(key)){
            this.set(key,value);
        }
    }

    public void add(String key, boolean value){
        this.add(key, Boolean.toString(value));
    }

    public void set(String key, boolean value){
        this.set(key, Boolean.toString(value));
    }

    public void add(String key, int value){
        this.add(key, Integer.toString(value));
    }

    public void set(String key, int value){
        this.set(key, Integer.toString(value));
    }

    public Set<String> getKeys(){
        return this.getProperties().stringPropertyNames();
    }

    public boolean contains(String key){
        return this.props.containsKey(key);
    }

    public Properties getProperties(){
        return this.props;
    }

    public void clear(){
        this.props = new Properties();
        this.save();
    }
}
