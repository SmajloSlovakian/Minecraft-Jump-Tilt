package smsk.jumptilt;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    public static Cdata cfg;
    public static float cfgVersion=1.5f;
    public static boolean problemReading=false;
    public Config(){
        File cfgfile=FabricLoader.getInstance().getConfigDir().resolve("jumptilt.json").toFile();
        if(cfgfile.exists()){
            cfg=readFile(cfgfile);
            if(cfg==null){
                problemReading=true;
                JT.print("There was a problem reading the config file, using the default values.");
                cfg=new Cdata();
            }
        }else{
            JT.print("Config file not found, making a new one.");
            cfg=new Cdata();
            writeFile(cfgfile);
        }

        if(!problemReading){ //config updating system
            if(cfg.cfgVersion<cfgVersion)JT.print("Config values before updating:\n"+printify());
            if(cfg.cfgVersion<1.3f){
                cfg.speed=0.5f;
                cfg.cfgVersion=1.3f;
            }
            if(cfg.cfgVersion<1.4f){
                cfg.upperClamping = 5;
                cfg.lowerClamping = 0.25f;
            }
            if(cfg.cfgVersion<1.5f){
                cfg.upperClampBreak = 0.1f;
                cfg.lowerClampBreak = 0.1f;
            }
            cfg.cfgVersion = cfgVersion;
            cfg.note="Speed can be between 0 (instant tilting) and 1 (tilting off). Amount can be any number. Press F3+T in a world to update config.";
            writeFile(cfgfile);
        }
        JT.print("Config values:\n"+printify());
    }
    Cdata readFile(File f){
        FileReader fr=null;
        Cdata ret=null;
        try{
            var gson=new Gson();
            fr=new FileReader(f);
            ret=gson.fromJson(fr, Cdata.class);
        } catch (Exception e) {}
        try {
            fr.close();
        } catch (Exception e) {}

        return(ret);
    }
    void writeFile(File f){
        FileWriter fw=null;
        try{
            var gson=new GsonBuilder().setPrettyPrinting().create();
            fw=new FileWriter(f);
            fw.write(gson.toJson(cfg));
        }catch(Exception e){}
        try{
            fw.close();
        }catch(Exception e){}
    }
    String printify(){
        return(
            "Speed: "+cfg.speed+"\n"+
            "Amount: "+cfg.amount+"\n"+
            "Clamping: "+cfg.lowerClamping+"; "+cfg.upperClamping+"\n"+
            "ClampBreak: "+cfg.lowerClampBreak+"; "+cfg.upperClampBreak+"\n"+
            "CfgVersion: "+cfgVersion
        );
    }
    public class Cdata implements Serializable{
        @Expose
        public String note = "";
        @Expose
        public float amount = 5;
        @Expose
        public float speed = 0.5f;
        @Expose
        public float upperClamping = 5;
        @Expose
        public float lowerClamping = 0.25f;
        @Expose
        public float upperClampBreak = 0.1f;
        @Expose
        public float lowerClampBreak = 0.1f;
        @Expose
        public float cfgVersion = 1.4f;
    }
}
