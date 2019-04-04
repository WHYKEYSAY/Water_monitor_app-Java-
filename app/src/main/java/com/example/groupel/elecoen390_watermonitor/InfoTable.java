package com.example.groupel.elecoen390_watermonitor;

public class InfoTable {
  
    String id,name,concentration,others;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id =id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPercent(){
        return concentration;
    }

    public void setPercent(String percent){
        this.concentration = percent;
    }

    public String getOthers(){
        return others;
    }

    public  void setOthers(String others){
        this.others = others;
    }
}

