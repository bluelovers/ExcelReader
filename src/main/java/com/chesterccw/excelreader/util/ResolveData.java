package com.chesterccw.excelreader.util;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.chesterccw.excelreader.ExcelReaderAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

public class ResolveData {

    private static final ResolveData INSTANCE = new ResolveData();
    private ResolveData(){

    }
    public static ResolveData getInstance() {
        return INSTANCE;
    }

    private static final String[] SUFFIX_ARRAY =
            new String[]{"xls", "xlsx", "csv"};

    private final Vector<String> header = new Vector<>();
    private final Vector<Vector<Object>> rows = new Vector<>();
    private final Map<Integer,String> sheetMap = new HashMap<>();
    private VirtualFile data = null;
    private String title;
    private String extension;

    public void setValue(VirtualFile data){
        this.data = data;
        if (this.data != null) {
            this.extension = data.getExtension();
            this.title = data.getName();
        }
    }

    // resolve the xls/xlsx data.
    // default sheetIndex = 0
    public ExcelReaderAction.Data resolve(int sheetIndex){
        if(Arrays.asList(SUFFIX_ARRAY).contains(this.extension)){
            String filePath = data.getPath();
            clearSheet();
            if(filePath.endsWith(SUFFIX_ARRAY[2])){
                CsvReader reader = CsvUtil.getReader();
                CsvData csvData = reader.read(new File(filePath));
                resolve(header,rows,csvData);
            } else {
                // 默认读取第一个sheet
                ExcelReader reader = ExcelUtil.getReader(filePath,sheetIndex);
                resolve(header,rows,reader);
                getSheetInfo(reader);
            }
        }
        ExcelReaderAction.Data data = new ExcelReaderAction.Data();
        data.header = this.header;
        data.rows = this.rows;
        data.title = this.title;
        data.sheetMap = sheetMap;
        return data;
    }

    // resolve the csv data
    private void resolve(Vector<String> header, Vector<Vector<Object>> rows,
                         CsvData csvData){
        List<CsvRow> allRows = csvData.getRows();
        if(allRows.size() == 0){
            return;
        }
        rows.clear();
        CsvRow headerRow = allRows.get(0);
        if(headerRow == null){
            return;
        }
        List<String> headerList = headerRow.getRawList();
        if (headerList.size() > 0) {
            header.clear();
        }
        for(String s : headerList){
            if(StringUtils.isEmpty(s)){
                continue;
            }
            header.add(s);
        }
        for(int i = 1 ; i < allRows.size() ; i++){
            CsvRow csvRow = allRows.get(i);
            if(csvRow == null){
                continue;
            }
            List<String> list = csvRow.getRawList();
            Vector<Object> vector = new Vector<>(list);
            rows.add(vector);
        }
    }

    // if read csv file, need to clean the sheetMaps
    private void clearSheet() {
        sheetMap.clear();
    }

    private void getSheetInfo(ExcelReader reader){
       if (reader == null) {
           return;
       }
       int sheetCount = reader.getSheetCount();
       for (int i = 0 ; i < sheetCount ; i++) {
           sheetMap.put(i,reader.getSheets().get(i).getSheetName());
       }
    }

    private void resolve(Vector<String> header, Vector<Vector<Object>> rows,
                         ExcelReader reader){

        List<Map<String,Object>> readAll = reader.readAll();
        if (readAll.size() > 0) {
            header.clear();
            rows.clear();
        }
        boolean outHeader = true;
        for(Map<String,Object> map : readAll){
            for(Map.Entry<String,Object> entry : map.entrySet()){
                if(outHeader){
                    header.add(entry.getKey());
                }
            }
            outHeader = false;
            Vector<Object> vector = new Vector<>();
            for(Map.Entry<String,Object> entry : map.entrySet()){
                vector.add(entry.getValue());
            }
            rows.add(vector);
        }
    }

}
