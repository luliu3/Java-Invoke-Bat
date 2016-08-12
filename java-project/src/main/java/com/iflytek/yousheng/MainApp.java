package com.iflytek.yousheng;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author luliu3 on 2016/8/11.
 */
public class MainApp {
    public static void main(String[] args) {

        // excel: excel文件, srcDir: 原声音乐文件夹, bgmDir: 背景音乐文件, tgtDir: 目标文件夹
        String excelFile, srcDir, bgmDir, tgtDir;
        int numArgs = args.length;
        if (numArgs != 4) {
            System.err.println("Usage: java -jar YoushengSynthesizer.jar <excel_file>" +
                    " <source_dir> <BGM_dir> <target_dir>");
            throw new IllegalArgumentException("Illegal number of arguments.");
        } else {
            excelFile = args[0];
            srcDir = args[1];
            bgmDir = args[2];
            tgtDir = args[3];
        }
        Synthesizer synthesizer = new Synthesizer(excelFile, srcDir, bgmDir, tgtDir);

        Scanner input = new Scanner(System.in);
        System.out.println("\n提示：是否需要扫描干声文件? (y/n)");
        String answer = input.next();
        if ("y".equals(answer) || "Y".equals(answer)) {

            System.out.println("\n提示：扫描中...");

            synthesizer.exportDrySoundBGMList();

            System.out.println("\n提示：请按照下列步骤操作");
            System.out.println("1. 请编辑excel文件的bgm列（保持与src列一一对应）");
            System.out.println("2. 如果excel文件编辑好了，按Enter键开始合成:");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\n提示：不扫描干声文件，使用默认excel文件开始合成：");
        }
        input.close();

        // synthesis
        synthesizer.doSynthesis();
    }
}
