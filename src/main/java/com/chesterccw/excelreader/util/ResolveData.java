package com.chesterccw.excelreader.util;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.chesterccw.excelreader.ExcelReaderAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ResolveData {

    private static final String[] SUFFIX_ARRAY = new String[]{"xls", "xlsx", "csv"};

    private final Vector<String> header = new Vector<>();
    private final Vector<Vector<Object>> rows = new Vector<>();
    private final VirtualFile data;
    private String title;
    private String extension;

    public ResolveData(VirtualFile data){
        this.data = data;
        if(this.data != null){
            this.extension = data.getExtension();
            this.title = data.getName();
        }
    }

    public ExcelReaderAction.Data resolve(){
        if(Arrays.asList(SUFFIX_ARRAY).contains(this.extension)){
            String filePath = data.getPath();
            if(filePath.endsWith(SUFFIX_ARRAY[2])){
                CsvReader reader = CsvUtil.getReader();
                CsvData csvData = reader.read(new File(filePath));
                resolve(header,rows,csvData);
            } else {
                ExcelReader reader = ExcelUtil.getReader(filePath);
                resolve(header,rows,reader);
            }
        }
        ExcelReaderAction.Data data = new ExcelReaderAction.Data();
        data.header = this.header;
        data.rows = this.rows;
        data.title = this.title;
        return data;
    }

    private void resolve(Vector<String> header, Vector<Vector<Object>> rows, CsvData csvData){
        List<CsvRow> allRows = csvData.getRows();
        if(allRows.size() == 0){
            return;
        }
        CsvRow headerRow = allRows.get(0);
        if(headerRow == null){
            return;
        }
        List<String> headerList = headerRow.getRawList();
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

    private void resolve(Vector<String> header, Vector<Vector<Object>> rows,ExcelReader reader){
        List<Map<String,Object>> readAll = reader.readAll();
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

    private static String getFileExtension(DataContext dataContext) {
        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }

}
