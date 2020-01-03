package cn.wangz.orc.schema;

import org.apache.commons.cli.*;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public class Main {

    public static void main(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(COMMAND_OPTS, args);

        if (commandLine.hasOption("help")) {
            new HelpFormatter().printHelp("java -jar JsonToSchema.jar", COMMAND_OPTS, true);
            return;
        }

        String json = commandLine.getOptionValue("json");
        String type = commandLine.getOptionValue("type", "CreateTable").toUpperCase();

        SchemaParse schemaParse = new SchemaParse(json);
        if ("CREATETABLE".equals(type)) {
            System.out.println(schemaParse.dumpCreateTable());
        } else if ("STRUCTSCHEMA".equals(type)) {
            System.out.println(schemaParse.dumpStructSchema());
        }

    }

    private final static Options COMMAND_OPTS;
    static {
        COMMAND_OPTS = new Options();

        Option tOpt = new Option("t", "type", true, "parse type, CreateTable / StructSchema");
        tOpt.setRequired(false);
        COMMAND_OPTS.addOption(tOpt);

        Option jOpt = new Option("j", "json", true, "json string");
        jOpt.setRequired(false);
        COMMAND_OPTS.addOption(jOpt);

        Option hOpt = new Option("h", "help", false, "display help text");
        COMMAND_OPTS.addOption(hOpt);
    }

}
