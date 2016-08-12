package com.iflytek.yousheng.model;

import com.iflytek.yousheng.excel.ExcelCell;

/**
 * @author luliu3 on 2016/8/12.
 */
public class DrySoundBGMList {
    @ExcelCell(index = 0)
    private String src;
    @ExcelCell(index = 1)
    private String bgm;

    public DrySoundBGMList(String src, String bgm) {
        this.src = src;
        this.bgm = bgm;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getBgm() {
        return bgm;
    }

    public void setBgm(String bgm) {
        this.bgm = bgm;
    }
}
