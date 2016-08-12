package com.iflytek.yousheng;

import com.iflytek.yousheng.excel.ExcelLogs;
import com.iflytek.yousheng.excel.ExcelUtil;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author luliu3 on 2016/8/11.
 */
public class Synthesizer {

    private String excelFile;
    private String srcDir;
    private String bgmDir;
    private String tgtDir;

    public Synthesizer(String excelFile, String srcDir, String bgmDir, String tgtDir) {
        this.excelFile = excelFile;
        this.srcDir = "\\" + srcDir + "\\";
        this.bgmDir = "\\" + bgmDir + "\\";
        this.tgtDir = "\\" + tgtDir + "\\";
    }

    @SuppressWarnings("unchecked")
    public void doSynthesis() {
        // src: 原声文件, bgm: 背景音文件, tgt: 目标文件
        String src, bgm, tgt;
        Collection<Map> excelData = this.importExcel();
        if (excelData != null) {
            // 检测excel文件内容是否为两列
            Iterator<Map> iterator = excelData.iterator();
            if (iterator.next().size() != 2) {
                System.out.println("Excel file against the rules.");
                System.exit(1);
            } else {


                System.out.println("\nStarting synthesis...");

                int i = 1, count = excelData.size();
                for (Map<String, String> map : excelData) {

                    // 取得原声音乐、背景音乐文件名
                    src = map.get("src");
                    bgm = map.get("bgm");
                    tgt = src.substring(0, src.lastIndexOf("."))+".mp3";

                    String temp = src + " + " + bgm;

                    // 加上文件夹名称
                    src = srcDir + src;
                    bgm = bgmDir + bgm;
                    tgt = tgtDir + tgt;

                    // 调用run.bat合成
                    doSynthesisWithBat(src, bgm, tgt);
                    System.out.println(temp + " synthesis completed!" +
                            "(" + i + "/" + count + ")");

                    i++;
                }
                System.out.println("All done!");
            }
        } else {
            System.out.println("Import excel file failed.");
            System.exit(1);
        }
    }

    private void doSynthesisWithBat(String src, String bgm, String tgt) {

        try {

            String userDir = System.getProperty("user.dir");
            String parentPath = userDir + "\\lib";
            String batPath = parentPath + "\\run.bat";

            src = userDir + src;
            bgm = userDir + bgm;
            tgt = userDir + tgt;

//            System.out.println(src +"\n" + bgm  +"\n" +tgt);

            // command in this form: run.bat src bgm tgt
            ProcessBuilder processBuilder = new ProcessBuilder(batPath, src, bgm, tgt);
            processBuilder.redirectErrorStream(true);
            processBuilder.directory(new File(parentPath));
            Process process = processBuilder.start();

            InputStream in = process.getInputStream();
            byte[] bytes = new byte[1024];
            // 等待process完成
            while (in.read(bytes) != -1) {
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Collection<Map> importExcel() {
        Collection<Map> excelData = null;
        try {
            File file = new File(excelFile);
            InputStream inputStream = new FileInputStream(file);
            ExcelLogs logs = new ExcelLogs();
            excelData = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return excelData;
    }
}
