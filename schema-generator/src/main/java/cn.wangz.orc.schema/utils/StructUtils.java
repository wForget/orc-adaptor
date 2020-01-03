package cn.wangz.orc.schema.utils;

import cn.wangz.orc.schema.model.Struct;
import javafx.util.Pair;

import java.util.Map;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public class StructUtils {

    public static String toCreateTableString(Struct struct) {
        return toCreateTableString(struct, "TABLE_NAME");
    }

    public static String toCreateTableString(Struct struct, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table " + tableName + " (\n");

        Map<String, String> singleMap = struct.getSingleMap();
        for (Map.Entry<String, String> entry: singleMap.entrySet()) {
            sb.append("\t");
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(entry.getValue());
            sb.append(",\n");
        }

        Map<String, Struct> structMap = struct.getStructMap();
        for (Map.Entry<String, Struct> entry: structMap.entrySet()) {
            sb.append("\t");
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(toStructSchemaString(entry.getValue()));
            sb.append(",\n");
        }

        Map<String, Pair<Integer, String>> singleArrayMap = struct.getSingleArrayMap();
        for (Map.Entry<String, Pair<Integer, String>> entry: singleArrayMap.entrySet()) {
            sb.append("\t");
            sb.append(entry.getKey());
            sb.append(" ");
            Pair<Integer, String> pair = entry.getValue();
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append("ARRAY<");
            }
            sb.append(pair.getValue());
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append(">");
            }
            sb.append(",\n");
        }

        Map<String, Pair<Integer, Struct>> structArrayMap = struct.getStructArrayMap();
        for (Map.Entry<String, Pair<Integer, Struct>> entry: structArrayMap.entrySet()) {
            sb.append("\t");
            sb.append(entry.getKey());
            sb.append(" ");
            Pair<Integer, Struct> pair = entry.getValue();
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append("ARRAY<");
            }
            sb.append(toStructSchemaString(pair.getValue()));
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append(">");
            }
            sb.append(",\n");
        }

        if (sb.lastIndexOf(",\n") == sb.length() - 2) {
            sb.deleteCharAt(sb.length() - 2);
        }

        sb.append(")");

        return sb.toString();
    }

    public static String toStructSchemaString(Struct struct) {
        StringBuilder sb = new StringBuilder();
        sb.append("STRUCT<");

        Map<String, String> singleMap = struct.getSingleMap();
        for (Map.Entry<String, String> entry: singleMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(",");
        }

        Map<String, Struct> structMap = struct.getStructMap();
        for (Map.Entry<String, Struct> entry: structMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(toStructSchemaString(entry.getValue()));
            sb.append(",");
        }

        Map<String, Pair<Integer, String>> singleArrayMap = struct.getSingleArrayMap();
        for (Map.Entry<String, Pair<Integer, String>> entry: singleArrayMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            Pair<Integer, String> pair = entry.getValue();
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append("ARRAY<");
            }
            sb.append(pair.getValue());
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append(">");
            }
            sb.append(",");
        }

        Map<String, Pair<Integer, Struct>> structArrayMap = struct.getStructArrayMap();
        for (Map.Entry<String, Pair<Integer, Struct>> entry: structArrayMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            Pair<Integer, Struct> pair = entry.getValue();
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append("ARRAY<");
            }
            sb.append(toStructSchemaString(pair.getValue()));
            for (int i = 0; i < pair.getKey(); i++) {
                sb.append(">");
            }
            sb.append(",");
        }

        if (sb.lastIndexOf(",") == sb.length() - 1) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(">");

        return sb.toString();
    }
}
