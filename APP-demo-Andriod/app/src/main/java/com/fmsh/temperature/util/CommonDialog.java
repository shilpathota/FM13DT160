package com.fmsh.temperature.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmsh.temperature.R;
import com.fmsh.temperature.databinding.DialogPromptBinding;


/**
 * Created by pc on 2017/11/21.
 */

public class CommonDialog extends AlertDialog implements View.OnClickListener {
    public TextView contentTxt;
    private Button btnCancel;
    private Button btnConfirm;
    private DialogPromptBinding binding;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private int layoutId;
    private int type = 0;
    private String positiveNameColor;
    private String backGroundPositiveNameColor;
    private String negativeNameColor;
    private String backGroundNegativeNameColor;
    public EditText mEtPwd;


    public CommonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId, int layoutId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.layoutId = layoutId;
    }

    public CommonDialog(Context context, int themeResId, int layoutId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.layoutId = layoutId;
        this.listener = listener;

    }

    public CommonDialog setColorPositiveButton(String color) {
        this.positiveNameColor = color;
        return this;

    }

    public CommonDialog setColorNegativeButton(String color) {
        this.negativeNameColor = color;
        return this;

    }

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    public CommonDialog setBackgroundNegativeButtonColor(String color) {
        this.backGroundNegativeNameColor = color;
        return this;
    }

    public CommonDialog setBackgroundPositiveButton(String color) {

        this.backGroundPositiveNameColor = color;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        setCanceledOnTouchOutside(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        initView();
    }


    private void initView() {

        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        contentTxt = findViewById(R.id.context);
//        mEtPwd = findViewById(R.id.etPwd);

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(this);
            if (negativeName != null)
                btnConfirm.setText(negativeName);
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(this);
            if (positiveName != null)
                btnCancel.setText(positiveName);
        }
        if (contentTxt != null && content != null) {
            contentTxt.setText(content);
        }


        setCancelable(false);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        //        if(negativeNameColor !=null){
        //            confirm.setTextColor(Color.parseColor(negativeNameColor));
        //        }
        //        if(positiveNameColor !=null){
        //            cancelTxt.setTextColor(Color.parseColor(positiveNameColor));
        //        }
        //        if(backGroundNegativeNameColor != null){
        //            confirm.setBackgroundColor(Color.parseColor(backGroundNegativeNameColor));
        //        }
        //        if(backGroundPositiveNameColor != null){
        //            cancelTxt.setBackgroundColor(Color.parseColor(backGroundPositiveNameColor));
        //        }

    }

    @Override
    public void onClick(View v) {
        if(v==binding.btnCancel) {
            if (listener != null) {
                listener.onClick(this, false);
            }
        }
        else if(v==binding.btnConfirm){
                if (listener != null) {
                    listener.onClick(this, true);
                }
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}