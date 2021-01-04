package com.bjfu.li.odour.utils;

import com.bjfu.li.odour.po.Measured;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    public static List<Measured> readXls(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<Measured> mrList = new ArrayList<>();
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                Measured mr = new Measured();
                XSSFCell measured = xssfRow.getCell(0);
                XSSFCell relativeAbundance = xssfRow.getCell(1);
                mr.setMeasured(BigDecimal.valueOf(measured.getNumericCellValue()));
                mr.setRelativeAbundance((int)Math.round(relativeAbundance.getNumericCellValue()));
                mrList.add(mr);
            }
        }
        return mrList;
    }

}