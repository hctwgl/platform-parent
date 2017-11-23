package fid.platform.core.common.commonutil;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

public class PoiUtil {

    /**
     * 读取某个文件夹下所有excel标签及句子 形式Map<文件名(大大类,Map<s,m<s,m<s,s>>>) 默认读第一个sheet
     *
     * @param excelDir
     * @return
     */
    public static Map<String, Map<String, Map<String, String>>> getLabelsMapFromExcel(
            File excelDir) {
        Map<String, Map<String, Map<String, String>>> resultMap = Maps
                .newLinkedHashMap();
        // 获取文件夹下所有excel文件
        File[] files = excelDir.listFiles(pathname -> {
            if (pathname.getName().toLowerCase().endsWith(".xlsx")) {
                return true;
            }
            return false;
        });
        for (File file : files) {
            Map<String, Map<String, String>> topicMap = new HashMap<>();

            String fileName = file.getName().split("\\.")[0];
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                XSSFWorkbook xwb = new XSSFWorkbook(bis);
                // 读取第一章表格内容
                int sheetNum = xwb.getNumberOfSheets();
                for (int i = 0; i < sheetNum; i++) {
                    XSSFSheet sheet = xwb.getSheetAt(i);
                    String pName = sheet.getSheetName();
                    String topic = "";
                    String secondTopic = "";
                    Iterator<Row> rows = sheet.iterator();
                    int sheetIndex = 1;

                    Map<String, String> secondMap = new HashMap<>();

                    while (rows.hasNext()) {

                        Row r = rows.next();

                        Iterator<Cell> cellIterator = r.cellIterator();
                        int sellSize = 0;
                        while (cellIterator.hasNext()) {
                            sellSize++;
                            cellIterator.next();
                        }
                        if (sellSize == 0) {
                            break;
                        }

                        if (r.getLastCellNum() - r.getFirstCellNum() >= 3) {
                            String topicTxt = r.getCell(0).getRichStringCellValue().toString();
                            if (org.apache.commons.lang3.StringUtils.isNotBlank(topicTxt)) {
                                topic = topicTxt;
                                secondMap = new HashMap<>();
                            }
                        }
                        if (sellSize >= 2) {
                            Cell secondTopicCell = r.getCell(r.getLastCellNum() - 2);
                            if (secondTopicCell != null) {
                                String secondTopicTxt = r.getCell(1).getRichStringCellValue().toString();
                                if (org.apache.commons.lang3.StringUtils.isNotBlank(secondTopicTxt)) {
                                    secondTopic = secondTopicTxt;
                                }
                            }
                        }
                        System.out.println(sellSize);
                        String context = r.getCell(2).getRichStringCellValue().toString();

                        if (topicMap.containsKey(topic)) {
                            Map<String, String> tempMap = topicMap.get(topic);

                            if (tempMap.containsKey(secondTopic)) {
                                secondMap.put(secondTopic, context.concat("\n").concat(tempMap.get(secondTopic)));
                            } else {
                                secondMap.put(secondTopic, context);
                            }
                        } else {
                            secondMap.put(secondTopic, context);
                        }
                        topicMap.put(topic, secondMap);

                        System.out.println("----topic:" + topic + "\n" + "second topic:" + secondTopic + "\n" + "context:" + context);

                    }
                }
                resultMap.put(fileName, topicMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    public static Map<String, String> getAllBasicLabelsMapFromExcel(
            File excelDir) {
        Map<String, String> resultMap = Maps.newLinkedHashMap();
        // 获取文件夹下所有excel文件
        File[] files = excelDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().toLowerCase().endsWith(".xlsx")) {
                    return true;
                }
                return false;
            }
        });
        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis);) {

                XSSFWorkbook xwb = new XSSFWorkbook(bis);
                // 读取第一章表格内容
                XSSFSheet sheet = xwb.getSheetAt(0);
                // 定义 row、cell
                XSSFRow row;
                String cell;
                // 循环表格中的内容入map
                for (int i = sheet.getFirstRowNum() + 1; i < sheet
                        .getPhysicalNumberOfRows(); i++) {
                    row = sheet.getRow(i);
                    String label = "";
                    String psg = "";
                    // 遍历一行
                    for (int j = row.getFirstCellNum(); j < row
                            .getPhysicalNumberOfCells(); j++) {
                        cell = row.getCell(j).toString();
                        // 得到一行的值
                        if (j == 1) {
                            label = cell;
                        } else if (j == 2) {
                            psg = cell;
                        } else {
                            continue;
                        }
                        // 装入labelMap
                        if (!StringUtils.isEmpty(label)
                                && !StringUtils.isEmpty(psg)) {
                            resultMap.put(label, psg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

}
