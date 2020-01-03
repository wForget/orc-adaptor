package cn.wangz.orc.schema;

public class Test {

    public static void main(String[] args) {
        String jsonStr = "{\n" +
                "\t\"name\": \"wz\",\n" +
                "\t\"age\": 20,\n" +
                "\t\"height\": 172.60,\n" +
                "\t\"a\": [[\"b\", \"c\"]],\n" +
                "\t\"time\": 1570619071000,\n" +
                "\t\"email\": {\n" +
                "\t\t\"number\": \"643348094.qq.com\",\n" +
                "\t\t\"pass\": 123456\n" +
                "\t}\n" +
                "}";

        SchemaParse schemaParse = new SchemaParse(jsonStr);
        System.out.println("----------StructSchema--------");
        System.out.println(schemaParse.dumpStructSchema());
        System.out.println("----------CreateTable--------");
        System.out.println(schemaParse.dumpCreateTable("custom"));

    }
}
