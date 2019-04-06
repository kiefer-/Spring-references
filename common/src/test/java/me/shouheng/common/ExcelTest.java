package me.shouheng.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Excel 读写示例程序
 *
 * @author shouh, 2019/4/6-17:23
 */
@Slf4j
public class ExcelTest {

    @Test
    public void testExcelRead() throws IOException {
        String originFile = "F:\\gen\\Translate.xlsx";
        String newFile = "F:\\gen\\Translate_new.xlsx";

        Workbook writeWorkbook = new SXSSFWorkbook();
        Sheet writeSheet = writeWorkbook.createSheet("new");

        // 将指定的 excel 读取到内存对象中
//        Workbook readWorkBook = WorkbookFactory.create(new File(originFile));
        Workbook readWorkBook = WorkbookFactory.create(new File(originFile));
        // 读取 excel 第一个sheet
        Sheet readSheet = readWorkBook.getSheetAt(0);

        log.info("excel rows : {}", readSheet.getLastRowNum());
        // 遍历excel的每行
        for (int index = 0; index < readSheet.getLastRowNum(); index++) {
            try {
                // 根据行号取出excel的每一行
                Row readRow = readSheet.getRow(index);
                if (readRow == null) {
                    break;
                }
                Iterator<Cell> cellIterator = readRow.cellIterator();
                // 在新的excel文件中添加一行
                Row writeRow = writeSheet.createRow(index);
                int temp = 0;
                // 迭代遍历excel每行的每一列
                while (cellIterator.hasNext()) {
                    Cell curCell = cellIterator.next();
                    // 处理可能存在的脏数据,这里假设第一列为行号
                    if (temp == 0 && curCell == null) {
                        break;
                    }

                    // 在新excel的当前行中添加一个cell
                    Cell writeRowCell = writeRow.createCell(temp);
                    writeRowCell.setCellType(curCell.getCellType());
                    writeRowCell.setCellValue(curCell.getStringCellValue());
                    temp++;
                }
            } catch (Exception e) {
                log.error("parse excel exception, row: {}", index, e);
                throw e;
            }
        }

        log.info("generate new excel");
        FileOutputStream newExcel = new FileOutputStream(newFile);
        try {
            writeWorkbook.write(newExcel);
        } finally {
            newExcel.flush();
            newExcel.close();
        }
    }
}
