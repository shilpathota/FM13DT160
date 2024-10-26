package com.fmsh.temperature.util;

import 	androidx.appcompat.app.AlertDialog;


public interface CommentDialogListener {

    /**
     * 点击确定按钮的回调
     */
    void ok(AlertDialog dialog);

    /**
     * 点击取消按钮的回调
     */
    void cancle(AlertDialog dialog);
}
