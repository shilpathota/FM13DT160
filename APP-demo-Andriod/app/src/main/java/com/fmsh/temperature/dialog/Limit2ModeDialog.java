package com.fmsh.temperature.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.fmsh.temperature.R;
import com.fmsh.temperature.activity.MainActivity;
import com.fmsh.temperature.databinding.Limit2ModeDialogBinding;
import com.fmsh.temperature.util.HintDialog;
import com.fmsh.temperature.util.MyConstant;
import com.fmsh.temperature.util.SpUtils;
import com.fmsh.temperature.util.UIUtils;

import java.lang.reflect.Field;

/**
 * @author wuyajiang
 * @date 2021/12/29
 */
public class Limit2ModeDialog implements View.OnClickListener {
    private MainActivity mContext;
    private TextView mMinLimit0;
    private TextView mMinLimit1;
    private TextView mMinLimit2;
    private TextView mMaxLimit0;
    private TextView mMaxLimit1;
    private TextView mMaxLimit2;
    private AlertDialog.Builder mAlert;
    private AlertDialog mAlertDialog;


    public Limit2ModeDialog(MainActivity context) {
        this.mContext = context;
    }

    private Limit2ModeDialogBinding binding;  // Add binding instance

    public void limit2Model() {
        mAlert = new AlertDialog.Builder(mContext);

        // Inflate the layout using ViewBinding
        binding = Limit2ModeDialogBinding.inflate(LayoutInflater.from(mContext));
        View inflate = binding.getRoot();

        // Set dialog notes text
        binding.notes.setText(UIUtils.getString(R.string.notes) + "\n" +
                "max_limit2 > max_limit1 > max_limit0\n" +
                "> min_limit0 > min_limit1 > min_limit2");

        // Assign views to variables directly from binding
        mMinLimit0 = binding.minLimit0;
        mMinLimit1 = binding.minLimit1;
        mMinLimit2 = binding.minLimit2;
        mMaxLimit0 = binding.maxLimit0;
        mMaxLimit1 = binding.maxLimit1;
        mMaxLimit2 = binding.maxLimit2;

        TextView tv_cancel = binding.tvCancel;
        TextView tv_confirm = binding.tvConfirm;

        // Set text and click listeners
        mMinLimit0.setText("min_limit0:   " + SpUtils.getIntValue(MyConstant.min_limit0, 12) + "°C");
        mMinLimit1.setText("min_limit1:   " + SpUtils.getIntValue(MyConstant.min_limit1, 5) + "°C");
        mMinLimit2.setText("min_limit2:   " + SpUtils.getIntValue(MyConstant.min_limit2, 2) + "°C");
        mMaxLimit0.setText("max_limit0:   " + SpUtils.getIntValue(MyConstant.max_limit0, 22) + "°C");
        mMaxLimit1.setText("max_limit1:   " + SpUtils.getIntValue(MyConstant.max_limit1, 25) + "°C");
        mMaxLimit2.setText("max_limit2:   " + SpUtils.getIntValue(MyConstant.max_limit2, 27) + "°C");

        // Set view to the alert dialog
        mAlert.setView(inflate);
        mAlertDialog = mAlert.create();
        mAlertDialog.show();

        // Cancel and Confirm button listeners
        tv_cancel.setOnClickListener(v -> mAlertDialog.dismiss());
        tv_confirm.setOnClickListener(v -> {
            if (mOnConfirmClick != null) {
                mOnConfirmClick.onClick(mAlertDialog);
            }
        });
    }

    final private static String[] thresholds = new String[]{"-40°C", "-30°C", "-20°C", "-18°C", "-15°C", "-10°C", "-8°C", "-5°C", "-4°C", "-3°C", "-2°C", "-1°C", "0°C", "1°C", "2°C", "3°C", "4°C", "5°C", "8°C", "10°C", "15°C", "18°C", "20°C", "23°C", "25°C", "30°C", "35°C", "40°C", "50°C", "60°C", "70°C", "80°C"}; /* Celsius */
    final private static int[] thresholdUnitIds = new int[]{R.string.celsius};

    private void pickerDialog(int selectValue, final String key, final TextView textView) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setCancelable(false);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        final NumberPicker numberPicker = new NumberPicker(mContext);
        numberPicker.setDisplayedValues(thresholds);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(thresholds.length - 1);
        numberPicker.setValue(selectValue);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setId(View.NO_ID);
        linearLayout.addView(numberPicker);
        alert.setView(linearLayout);
        alert.setPositiveButton(UIUtils.getString(R.string.text_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int value = numberPicker.getValue();
                String timeValue = thresholds[value];
                textView.setText(key + ":   " + thresholds[value]);
                SpUtils.putIntValue(key, Integer.parseInt(timeValue.replace("°C", "")));
                SpUtils.putIntValue(key + "value", value);
                dialog.dismiss();
            }
        });

        alert.setNegativeButton(UIUtils.getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alert.create();
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        //        mAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.minLimit0) {
            pickerDialog(SpUtils.getIntValue(MyConstant.min_limit0 + "value", 12),
                    MyConstant.min_limit0, binding.minLimit0);
        } else if (v == binding.minLimit1) {
            pickerDialog(SpUtils.getIntValue(MyConstant.min_limit1 + "value", 5),
                    MyConstant.min_limit1, binding.minLimit1);
        } else if (v == binding.minLimit2) {
            pickerDialog(SpUtils.getIntValue(MyConstant.min_limit2 + "value", 2),
                    MyConstant.min_limit2, binding.minLimit2);
        } else if (v == binding.maxLimit0) {
            pickerDialog(SpUtils.getIntValue(MyConstant.max_limit0 + "value", 22),
                    MyConstant.max_limit0, binding.maxLimit0);
        } else if (v == binding.maxLimit1) {
            pickerDialog(SpUtils.getIntValue(MyConstant.max_limit1 + "value", 25),
                    MyConstant.max_limit1, binding.maxLimit1);
        } else if (v == binding.maxLimit2) {
            pickerDialog(SpUtils.getIntValue(MyConstant.max_limit2 + "value", 27),
                    MyConstant.max_limit2, binding.maxLimit2);
        }
    }


    public boolean checkLimitData() {
        int minLimit0 = SpUtils.getIntValue(MyConstant.min_limit0, 0);
        int minLimit1 = SpUtils.getIntValue(MyConstant.min_limit1, -10);
        int minLimit2 = SpUtils.getIntValue(MyConstant.min_limit2, -20);
        int maxLimit0 = SpUtils.getIntValue(MyConstant.max_limit0, 20);
        int maxLimit1 = SpUtils.getIntValue(MyConstant.max_limit1, 30);
        int maxLimit2 = SpUtils.getIntValue(MyConstant.max_limit2, 40);
        if (maxLimit2 > maxLimit1 && maxLimit1 > maxLimit0 && maxLimit0 > minLimit0 && minLimit0 > minLimit1 && minLimit1 > minLimit2) {
            return true;
        } else {
            HintDialog.faileDialog(mContext, UIUtils.getString(R.string.notes_hint));
            return false;
        }
    }

    public void setOnConfirmClick(OnConfirmClick onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    private OnConfirmClick mOnConfirmClick;

    public interface OnConfirmClick {
        void onClick(DialogInterface dialog);
    }
}
