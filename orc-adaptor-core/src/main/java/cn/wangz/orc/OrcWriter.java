package cn.wangz.orc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.common.type.HiveDecimal;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.hadoop.hive.serde2.io.HiveDecimalWritable;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public class OrcWriter<T> {

    private Path path;
    private TypeDescription schema;
    private Configuration conf;
    private Writer writer;
    private VectorizedRowBatch batch;
    private int bachSize;

    public OrcWriter(String path, String schema, Configuration conf) throws IOException {
        this(path, schema, conf, 1024);
    }

    public OrcWriter(String path, String schema, Configuration conf, int bachSize) throws IOException {
        this.path = new Path(path);
        this.schema = TypeDescription.fromString(schema);
        this.conf = conf;
        if (this.conf == null) {
            this.conf = new Configuration();
        }
        this.bachSize = bachSize;
        initWriter();
    }

    private void initWriter() throws IOException {
        this.batch = this.schema.createRowBatch(bachSize);
        this.writer = OrcFile.createWriter(this.path
                , OrcFile.writerOptions(this.conf).setSchema(this.schema));
        this.rowIndex = new AtomicInteger(0);
    }

    private AtomicInteger rowIndex;

    public void write(JSONObject jsonObject) throws IOException {
        if (batch.size >= bachSize) {
            flush();
        }
        int row = rowIndex.getAndIncrement();
        ColumnVector[] columnVectors = batch.cols;
        List<String> fieldNames = schema.getFieldNames();
        List<TypeDescription> rootTypeDescriptions = schema.getChildren();
        for (int i = 0; i < columnVectors.length; i++) {
            ColumnVector vector = columnVectors[i];
            TypeDescription typeDescription = rootTypeDescriptions.get(i);
            String fieldName = fieldNames.get(i);
            Object value = jsonObject.get(fieldName);
            writeRow(row, value, vector, typeDescription);
        }
    }

    private void writeRow(int row, Object value, ColumnVector vector, TypeDescription typeDescription) {
        TypeDescription.Category category = typeDescription.getCategory();
        switch (category) {
            case BOOLEAN:
            case DATE:
            case LONG:
            case SHORT:
            case INT:
            case BYTE:
                LongColumnVector longColumnVector = (LongColumnVector) vector;
                if (value == null) {
                    longColumnVector.isNull[row] = true;
                    break;
                }
                if (category == TypeDescription.Category.BOOLEAN) {
                    value = TypeUtils.castToBoolean(value)? 1: 0;
                }
                if (category == TypeDescription.Category.DATE) {
                    value = TypeUtils.castToDate(value).getTime();
                }
                longColumnVector.vector[row] = TypeUtils.castToLong(value);
                break;
            case FLOAT:
            case DOUBLE:
                DoubleColumnVector doubleColumnVector = (DoubleColumnVector) vector;
                if (value == null) {
                    doubleColumnVector.isNull[row] = true;
                    break;
                }
                doubleColumnVector.vector[row] = TypeUtils.castToDouble(value);
                break;
            case BINARY:
            case STRING:
            case VARCHAR:
            case CHAR:
                BytesColumnVector bytesColumnVector = (BytesColumnVector) vector;
                if (value == null) {
                    bytesColumnVector.isNull[row] = true;
                    break;
                }
                if (category == TypeDescription.Category.BINARY) {
                    bytesColumnVector.vector[row] = TypeUtils.castToBytes(value);
                } else {
                    bytesColumnVector.vector[row] = TypeUtils.castToString(value).getBytes();
                }
                break;
            case TIMESTAMP:
                TimestampColumnVector timestampColumnVector = (TimestampColumnVector) vector;
                if (value == null) {
                    timestampColumnVector.isNull[row] = true;
                    break;
                }
                Timestamp timestamp = TypeUtils.castToTimestamp(value);
                timestampColumnVector.time[row] = timestamp.getTime();
                timestampColumnVector.nanos[row] = timestamp.getNanos();
                break;
            case DECIMAL:
                DecimalColumnVector decimalColumnVector = (DecimalColumnVector) value;
                if (value == null) {
                    decimalColumnVector.isNull[row] = true;
                    break;
                }
                decimalColumnVector.vector[row] = new HiveDecimalWritable(HiveDecimal.create(TypeUtils.castToBigDecimal(value)));
                break;
            case LIST:
                break;
            case MAP:
                break;
            case STRUCT:
                break;
            case UNION:
                break;
            case TIMESTAMP_INSTANT:
                break;
        }

    }

    private void flush() throws IOException {
        if (batch.size != 0) {
            writer.addRowBatch(batch);
            batch.reset();
            rowIndex.set(0);
        }
    }

    public void close() throws IOException {
        flush();
        writer.close();
    }

}
