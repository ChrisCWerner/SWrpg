package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Weapons{
    Weapon[] w;
    public Weapons(){
        w = new Weapon[0];
    }
    public void add(Weapon weap){
        w = Arrays.copyOf(w,w.length+1);
        w[w.length-1] = weap;
    }
    public int remove(Weapon wn){
        int i = -1;
        for (int j = 0;j<w.length;j++){
            if (w[j].equals(wn)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            Weapon[] newW = new Weapon[w.length - 1];
            for (int j = 0; j < i; j++)
                newW[j] = w[j];
            for (int j = i + 1; j < w.length; j++)
                newW[j - 1] = w[j];
            w = newW;
        }
        return i;
    }
    public Weapon get(int i){
        return w[i];
    }
    public int size(){
        return w.length;
    }
    public void set(Weapon old,Weapon newW){
        int i = -1;
        for (int j = 0;j<w.length;j++){
            if (w[j].equals(old)){
                i = j;
                break;
            }
        }
        if (i != -1) {
            w[i] = newW;
        }
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Weapon wn: w)
            tmp.add(wn.serialObject());
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Weapon> out = new ArrayList<>();
        for (Object o : tmp) {
            Weapon wn = new Weapon();
            wn.loadFromObject(o);
            out.add(wn);
        }
        w = out.toArray(w);
    }
}