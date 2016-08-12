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
                System.err.println("Excel file against the rules.");
                System.exit(1);
            } else {

                System.out.println("\nStarting synthesis:\n");

                int i = 1, success = 0, failed = 0, total = excelData.size();
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

                    // test target file exists
                    String tgtAbsolutePath = System.getProperty("user.dir") + tgt;
                    File tgtFile = new File(tgtAbsolutePath);
                    if (tgtFile.exists()) {
                        success++;
                        System.out.println("Success: " + temp + " (" + i + "/" + total + ")");
                    } else {
                        failed++;
                        System.err.println("Failed: " + temp + " (" + i + "/" + total + ")");
                    }
                    i++;
                }
                System.out.println("\nAll done!");

                System.out.print("Summary, Total: " + total);
                System.out.print(", Success: " + success);
                System.err.println(", Failed: " + failed);
            }
        } else {
            System.err.println("Import excel file failed.");
            System.exit(1);
        }
    }

    private void doSynthesisWithBat(String src, String bgm, String tgt) {

        try {

            String userDir = System.getProperty("user.dir");
            String scriptPath = userDir + "\\lib";
            String batAbsolutePath = scriptPath + "\\run.bat";

            src = userDir + src;
            bgm = userDir + bgm;
            tgt = userDir + tgt;

            // test src and bgm exists
            File srcFile = new File(src);
            File bgmFile = new File(bgm);
            if (!srcFile.exists() || !bgmFile.exists()) {
                System.err.println("Dry-Sound file or BGM file not exist, please confirm and try again!");
                throw new FileNotFoundException();
            }

//            System.out.println(src +"\n" + bgm  +"\n" +tgt);

            // command in this form: run.bat src bgm tgt
            ProcessBuilder processBuilder = new ProcessBuilder(batAbsolutePath, src, bgm, tgt);
            processBuilder.redirectErrorStream(true);
            processBuilder.directory(new File(scriptPath));
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
