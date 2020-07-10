package com.insigma.util;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class NewExcelUtils {
    public static void main(String[] args) throws Exception {

    }

    /**
     * 获取excel数据
     *
     * @param beginRow       从第几行开始读
     * @param beginColumn    从第几列开始读
     * @param lastColumn     截止读到第几列
     * @param exceptColumn   第几列数据不要
     * @param lastRowCount   最后几行数据不要
     * @param type           模板类型 1央企 其它
     * @param file           excel 文件
     * @param rowOfPlace     地区行 目的是为了当地区计划下达数不为0的时候就将此地区保存
     * @param beginCellCount 从第几行开始需要作空判断
     * @return
     * @throws Exception
     */
    public static List<List<String>> getExcelData(int beginRow, int beginColumn, int lastColumn, int lastRowCount, int exceptColumn, int rowOfPlace, int beginCellCount, MultipartFile file, String type) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        List<List<String>> dataList = null;
        //2007excel
        if (fileName.endsWith("xlsx")) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            //其它模板
            if (!type.equals("1")) {
                dataList = readExcelOfprovince(beginRow, beginColumn, lastColumn, lastRowCount, exceptColumn, rowOfPlace, beginCellCount, xssfWorkbook);
                return dataList;
            }
            dataList = readExcelOfCountry(beginRow, beginColumn, lastColumn, exceptColumn, xssfWorkbook);
        }
        if (fileName.endsWith("xls")) {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            //其它模板
            if (!type.equals("1")) {
                dataList = readExcelOfprovince(beginRow, beginColumn, lastColumn, lastRowCount, exceptColumn, rowOfPlace, beginCellCount, hssfWorkbook);
                return dataList;
            }
            dataList = readExcelOfCountry(beginRow, beginColumn, lastColumn, beginColumn, hssfWorkbook);
        }
        return dataList;
    }

    /**
     * 读取excel 模板1
     *
     * @param beginRow     从第几行开始读
     * @param beginColumn  从第几列开始读
     * @param lastColumn   截止到第几列
     * @param exceptColumn 第几列数据不要
     * @return
     * @throws Exception
     */
    public static List<List<String>> readExcelOfCountry(int beginRow, int beginColumn, int lastColumn, int exceptColumn, Workbook workbook) throws Exception {
        //用来存取结果
        List<List<String>> resultList = new ArrayList<>();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            //获取该sheet所有合并单元格
            List<CellRangeAddress> combineCellList = getCombineCellList(sheet);
            //记录当前行的数据
            int nRow = 1;
            //获取此页最后行数
            int lastRowNum = sheet.getLastRowNum();
            if (beginRow > sheet.getLastRowNum()) {
                beginRow = sheet.getLastRowNum();
            }
            for (int rowCount = beginRow - 1; rowCount < sheet.getLastRowNum(); rowCount++) {
                //存储每行的数据
                List<String> rowDataList = new ArrayList<>();
                Row row = sheet.getRow(rowCount);
                //这行不为空才是有意义的数据
                if (row != null) {
                    for (int cellCount = beginColumn; cellCount < lastColumn; cellCount++) {
                        Map<String, String> combineCellMap = isCombineCell(combineCellList, row.getCell(cellCount), sheet);
                        //获取当前单元格数据
                        String nCell = getCellValueNoCareType(row.getCell(cellCount));
                        if (cellCount != exceptColumn - 1) {
                            //判断是否是合并单元格
                            if (combineCellMap.get("flag").equals("true")) {
                                //获取合并单元格数据
                                String mergedRegionValue = getMergedRegionValue(sheet, rowCount, cellCount);
                                rowDataList.add(mergedRegionValue);
                                continue;
                            }
                            rowDataList.add(nCell);
                        }
                    }
                    resultList.add(rowDataList);
                    nRow++;
                }
            }
        }
        return resultList;
    }

    /**
     * 判断是否是合并单元格并获取其所占行数
     *
     * @return
     */
    public static Map<String, String> isCombineCell(List<CellRangeAddress> cellRangeAddressList, Cell cell, Sheet sheet) {
        Map<String, String> map = new HashMap<>();
        map.put("flag", "false");
        map.put("row", "-1");
        map.put("column", "-1");
        for (CellRangeAddress cellRange : cellRangeAddressList) {
            //起始行
            int firstRow = cellRange.getFirstRow();
            //截止行
            int lastRow = cellRange.getLastRow();
            //起始列
            int firstColumn = cellRange.getFirstColumn();
            //截止列
            int lastColumn = cellRange.getLastColumn();
            //判断当前单元格位置是否再合并单元格区域内
            if (cell.getRowIndex() >= firstRow && cell.getRowIndex() <= lastRow) {
                if (cell.getColumnIndex() >= firstColumn && cell.getColumnIndex() <= lastColumn) {
                    int rowCount = lastRow - firstRow + 1;
                    int columnCount = lastColumn - firstColumn + 1;
                    map.put("flag", "true");
                    map.put("row", String.valueOf(rowCount));
                    map.put("column", String.valueOf(columnCount));
                    break;
                }
            }
        }
        return map;
    }

    /**
     * 获取所有单元格数
     *
     * @param sheet
     * @return
     */
    public static List<CellRangeAddress> getCombineCellList(Sheet sheet) {
        List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
        //获取合并单元个数
        int numMergedRegions = sheet.getNumMergedRegions();
        //遍历合并单元格
        for (int i = 0; i < numMergedRegions; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            cellRangeAddressList.add(mergedRegion);
        }
        return cellRangeAddressList;
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValueNoCareType(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 根据单元格内容返回具体类型的数据值
     *
     * @param cell
     * @return
     */
    public static String getCellValueNoCareType(Cell cell) {
        //如果单元格内容为空则返回空字符串
        if (cell == null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType = cell.getCellType();
        switch (cellType) {
            //String 类型
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue().trim();
                cellValue=cellValue.replace("\n","");
                //将2（1） 2后面的数据去除不要（这里的括号是中文）
                if (cellValue.contains("（")) {
                    int i = cellValue.indexOf('（');
                    cellValue = cellValue.substring(0, i);
                }
                //将2（1） 2后面的数据去除不要（这里的括号是英文）
                if (cellValue.contains("(")) {
                    int i = cellValue.indexOf('(');
                    cellValue = cellValue.substring(1, i);
                }
                //有些数据值是整数但是判断类型还是字符串 所以还是要去.0处理
                cellValue = StringUtils.isEmpty(cell) ? "" : cellValue.replace(".0", "");
                break;
            //数值类型且进一步判断是否为日期类型
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date dateCellValue = cell.getDateCellValue();
                    cellValue = new SimpleDateFormat("yyyy-MM-dd").format(dateCellValue);
                } else {
                    //防止将1弄成1.0
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = cell.getStringCellValue().replace(".0", "");
                }
                break;
            //布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            //其他类型就为空
            default:
                cellValue = "编码问题";
                break;
        }
        return cellValue;
    }

    /**
     * 获取指定行的所有数据
     *
     * @param file excel文件
     * @param  rowIndex 从第几行开始读
     * @param  endColumn 截止到第几列
     * @param  endRowCount 截止到第几行 等于-1时标题不是合并单元格不用往下读 不等于-1时时合并单元格必须把范围内标题全部读取出来
     * @return
     * @throws Exception
     */
    public static List<String> getHeader(MultipartFile file, int rowIndex,int endRowCount, int endColumn) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        List<String> headList = null;
        //2007excel
        if (fileName.endsWith("xlsx")) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            headList = getDataOfHead(xssfWorkbook, rowIndex,endRowCount, endColumn);
        }
        if (fileName.endsWith("xls")) {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            headList = getDataOfHead(hssfWorkbook, rowIndex,endRowCount, endColumn);
        }
        return headList;
    }

    public static List<String> getDataOfHead(Workbook workbook, int rowIndex, int endRowCount, int endColumn) {
        List<String> headerList = new ArrayList<>();
        if (endRowCount == -1) {
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Row row = sheet.getRow(rowIndex - 1);
                for (int j = 0; j < endColumn; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                       if(j==endColumn-1){
                           headerList.add(cell.getStringCellValue());
                           continue;
                       }
                        headerList.add(getCellValueNoCareType(cell));
                    }
                }
            }
        } else {
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                List<CellRangeAddress> combineCellList = getCombineCellList(sheet);
                for (int rowCount = rowIndex - 1; rowCount < endRowCount; rowCount++) {
                    Row row = sheet.getRow(rowCount);
                    for (int j = 0; j < endColumn; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            Map<String, String> combineCell = isCombineCell(combineCellList, cell, sheet);
                            if (combineCell.get("flag").equals("true")) {
                                String mergedRegionValue = getMergedRegionValue(sheet, rowCount, j);
                                headerList.add(mergedRegionValue);
                                continue;
                            }
                            headerList.add(getCellValueNoCareType(cell));
                        }
                    }
                }
            }
        }
        return headerList;
    }

    /**
     * 模板2（目前用于读取除央企外模板数据）
     *
     * @param beginRow       从第几行开始
     * @param beginColumn    从第几列开始
     * @param lastColumn     截止到第几列
     * @param lastRowCount   最后几行不要
     * @param exceptColumn   第几列不要
     * @param rowOfPlace     地区行 目的是为了当地区计划下达数不为0的时候就将此地区保存
     * @param beginCellCount 从第几列就要做为空判断
     * @return
     * @throws Exception
     */
    public static List<List<String>> readExcelOfprovince(int beginRow, int beginColumn, int lastColumn, int lastRowCount, int exceptColumn, int rowOfPlace, int beginCellCount, Workbook workbook) throws Exception {
        //存储整excel表数据
        List<List<String>> resultList = new ArrayList<>();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            //地区行 目的是为了当地区计划下达数不为0的时候就将此地区保存
            Row rowPlace = sheet.getRow(rowOfPlace - 1);
            List<CellRangeAddress> combineCellList = getCombineCellList(sheet);
            int lastRowNum = sheet.getLastRowNum();
            for (int rowCount = beginRow - 1; rowCount < lastRowNum - 2; rowCount++) {
                Row row = sheet.getRow(rowCount);
                if (row != null) {
                    List<String> rowDataList = new ArrayList<>();
                    for (int cellCount = beginColumn; cellCount < lastColumn; cellCount++) {
                        Cell cell = row.getCell(cellCount);
                        if (cell != null) {
                            Map<String, String> combineCell = isCombineCell(combineCellList, cell, sheet);
                            if (cellCount < beginCellCount - 1) {
                                if (combineCell.get("flag").equals("true")) {
                                    String mergedRegionValue = getMergedRegionValue(sheet, rowCount, cellCount);
                                    rowDataList.add(mergedRegionValue);
                                    continue;
                                }
                                rowDataList.add(getCellValueNoCareType(cell));
                            } else {
                                if (getCellValueNoCareType(cell) != null && getCellValueNoCareType(cell) != "") {
                                    String cellValue = getCellValueNoCareType(cell);
                                    rowDataList.add(cellValue);
                                    //不为空的列对应的地区
                                    Cell cellPlace = rowPlace.getCell(cellCount);
                                    String cellOfPlace = getCellValueNoCareType(cellPlace);
                                    rowDataList.add(cellOfPlace);
                                }
                            }
                        }
                    }
                    resultList.add(rowDataList);
                }
            }
        }

        return resultList;
    }

}
