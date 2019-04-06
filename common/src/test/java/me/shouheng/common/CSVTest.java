package me.shouheng.common;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * 读写 CSV 文件
 *
 * @author shouh, 2019/4/6-16:43
 */
public class CSVTest {

    private static final Logger logger = LoggerFactory.getLogger(CSVTest.class);

    @Test
    public void testSCVRead() {
        try {
            Reader in = new FileReader("F:\\gen\\sitka_weather_2014.csv");
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
            records.forEach(record -> logger.debug("Record {}, {}", record.get(0), record.get(1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
