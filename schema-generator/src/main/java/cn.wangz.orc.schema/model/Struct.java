package cn.wangz.orc.schema.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public class Struct {

    // String、Number、Date
    private Map<String, String> singleMap;

    // struct
    private Map<String, Struct> structMap;

    // Array<String、Number、Date>
    private Map<String, Pair<Integer, String>> singleArrayMap;

    // Array<struct>
    private Map<String, Pair<Integer, Struct>> structArrayMap;

    public Struct() {
        this.singleMap = new HashMap<String, String>();
        this.structMap = new HashMap<String, Struct>();
        this.singleArrayMap = new HashMap<String, Pair<Integer, String>>();
        this.structArrayMap = new HashMap<String, Pair<Integer, Struct>>();
    }

    public void putSingleMap(String key, String value) {
        this.singleMap.put(key, value);
    }

    public void putStructMap(String key, Struct value) {
        this.structMap.put(key, value);
    }

    public void putSingleArrayMap(String key, Pair<Integer, String> value) {
        this.singleArrayMap.put(key, value);
    }

    public void putStructArrayMap(String key, Pair<Integer, Struct> value) {
        this.structArrayMap.put(key, value);
    }

    public Map<String, String> getSingleMap() {
        return singleMap;
    }

    public void setSingleMap(Map<String, String> singleMap) {
        this.singleMap = singleMap;
    }

    public Map<String, Struct> getStructMap() {
        return structMap;
    }

    public void setStructMap(Map<String, Struct> structMap) {
        this.structMap = structMap;
    }

    public Map<String, Pair<Integer, String>> getSingleArrayMap() {
        return singleArrayMap;
    }

    public void setSingleArrayMap(Map<String, Pair<Integer, String>> singleArrayMap) {
        this.singleArrayMap = singleArrayMap;
    }

    public Map<String, Pair<Integer, Struct>> getStructArrayMap() {
        return structArrayMap;
    }

    public void setStructArrayMap(Map<String, Pair<Integer, Struct>> structArrayMap) {
        this.structArrayMap = structArrayMap;
    }
}
