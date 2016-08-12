package com.iflytek.yousheng;

import java.io.IOException;

/**
 * @author luliu3 on 2016/8/11.
 */
public class MainApp {
    public static void main(String[] args) {

        // excel: excel文件, srcDir: 原声音乐文件夹, bgmDir: 背景音乐文件, tgtDir: 目标文件夹
        String excelFile, srcDir, bgmDir, tgtDir;
        int numArgs = args.length;
        if (numArgs != 4) {
            System.out.println("Usage: java -jar YoushengSynthesizer.jar <excel_file>" +
                    " <source_dir> <BGM_dir> <target_dir>");
            throw new IllegalArgumentException("Illegal number of arguments.");
        } else {
            excelFile = args[0];
            srcDir = args[1];
            bgmDir = args[2];
            tgtDir = args[3];
        }
        Synthesizer synthesizer = new Synthesizer(excelFile, srcDir, bgmDir, tgtDir);
        synthesizer.doSynthesis();
    }
}
