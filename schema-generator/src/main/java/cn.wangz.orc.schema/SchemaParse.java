package cn.wangz.orc.schema;

import cn.wangz.orc.schema.model.Constants;
import cn.wangz.orc.schema.model.ParseType;
import cn.wangz.orc.schema.model.Struct;
import cn.wangz.orc.schema.utils.StructUtils;
import cn.wangz.orc.schema.utils.TypeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public class SchemaParse {

    private String json;
    private Struct struct;

    public SchemaParse(String json) {
        this.json = json;
        initStruct();
    }

    public String dumpCreateTable() {
        return StructUtils.toCreateTableString(struct);
    }

    public String dumpCreateTable(String tableName) {
        return StructUtils.toCreateTableString(struct, tableName);
    }

    public String dumpStructSchema() {
        return StructUtils.toStructSchemaString(struct);
    }


    public String getJson() {
        return json;
    }

    public Struct getStruct() {
        return struct;
    }

    private void initStruct() {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(json);
        } catch (JSONException e) {
            System.out.println("SchemaParse initSchema error, JSON String cannot parse to JSONObject!");
            e.printStackTrace();
        }
        if (jsonObject == null) {
            return;
        }
        struct = parseStruct(jsonObject);
    }

    private Struct parseStruct(JSONObject jsonObject) {
        Struct struct = new Struct();
        for (Map.Entry<String, Object> entry: jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // null 设置 NONE 类型
            if (value == null) {
                struct.putSingleMap(key, Constants.NONE);
                continue;
            }

            // is array
            if (value instanceof JSONArray) {
                int deep = 0;
                Object firstValue = value;
                while (firstValue != null && firstValue instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) firstValue;
                    jsonArray = new JSONArray(jsonArray.stream().filter(item -> item != null).collect(Collectors.toList()));
                    if (jsonArray.isEmpty()) {
                        firstValue = null;
                    } else {
                        firstValue = jsonArray.get(0);
                    }
                    deep ++;
                }
                if (firstValue == null) {
                    struct.putSingleArrayMap(key, new Pair<>(deep, Constants.NONE));
                    continue;
                }

                // is struct
                if (firstValue instanceof JSONObject) {
                    JSONObject stuctObject = (JSONObject) firstValue;
                    if (stuctObject.isEmpty()) {
                        struct.putStructArrayMap(key, new Pair<>(deep, new Struct()));
                    } else {
                        struct.putStructArrayMap(key, new Pair<>(deep, parseStruct(stuctObject)));
                    }
                }

                Pair<Boolean, String> singlePair = TypeUtils.isSingle(firstValue);
                if (singlePair.getKey()) {
                    struct.putSingleArrayMap(key, new Pair<>(deep, singlePair.getValue()));
                    continue;
                }
            }

            // is struct
            if (value instanceof JSONObject) {
                JSONObject stuctObject = (JSONObject) value;
                if (stuctObject.isEmpty()) {
                    struct.putStructMap(key, new Struct());
                } else {
                    struct.putStructMap(key, parseStruct(stuctObject));
                }
            }

            Pair<Boolean, String> singlePair = TypeUtils.isSingle(value);
            if (singlePair.getKey()) {
                struct.putSingleMap(key, singlePair.getValue());
            }
        }
        return struct;
    }
}
