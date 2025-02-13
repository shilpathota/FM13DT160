package com.fmsh.temperature.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.viewbinding.ViewBinding;
import com.fmsh.temperature.R;
import com.fmsh.temperature.activity.RecordActivity;
import com.fmsh.temperature.databinding.FragmentImBinding;
import com.fmsh.temperature.databinding.FragmentSettingBinding;
import com.fmsh.temperature.util.HintDialog;
import com.fmsh.temperature.util.LogUtil;
import com.fmsh.temperature.util.MyConstant;
import com.fmsh.temperature.util.NFCUtils;
import com.fmsh.temperature.util.SpUtils;
import com.fmsh.temperature.util.TransUtil;
import com.fmsh.temperature.util.UIUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

//import butterknife.BindView;
//import butterknife.OnClick;
//import butterknife.Unbinder;

/**
 * Created by wyj on 2018/7/2.
 */
public class SettingFragment extends BaseFragment {
    private FragmentSettingBinding binding;

//    @BindView(R.id.tvDelay)
//    TextView tvDelay;
//    @BindView(R.id.tvInterval)
//    TextView tvInterval;
//    @BindView(R.id.tvMinTp)
//    TextView tvMinTp;
//    @BindView(R.id.tvMAxTp)
//    TextView tvMAxTp;
//    @BindView(R.id.tvMode)
//    TextView tvMode;
//    @BindView(R.id.tvCount)
//    TextView tvCount;
//    @BindView(R.id.applyConfigButton)
//    Button applyConfigButton;
//    @BindView(R.id.resultButton)
//    Button resultButton;
//    @BindView(R.id.resultFiledButton)
//    Button resultFiledButton;


    @Override
    protected int setView() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void init(View view) {

        binding.tvDelay.setText(UIUtils.getString(R.string.text_measure_delay) + "   " + delayTime[SpUtils.getIntValue("delay", 1)]);
        binding.tvInterval.setText(UIUtils.getString(R.string.text_measure_interval) + "   " + intervals[SpUtils.getIntValue("interval", 2)]);

        setCount();
        setupClickListeners();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(hidden + "onHiddenChanged");

        if (!hidden) {
            setCount();

        }
    }


    private void setCount() {
        int intValue = SpUtils.getIntValue(MyConstant.tpMode, 0);
        if (intValue == 0) {
            int count = SpUtils.getIntValue("count", 0);
            LogUtil.d("count" + count);
            if (count >= counts.length) {
                count = counts.length - 1;
                SpUtils.putIntValue("count", counts.length - 1);
            }

            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts[count]);

        } else if (intValue == 1) {
            int count = SpUtils.getIntValue("count", 0);
            LogUtil.d("count" + count);
            if (count >= counts1.length) {
                count = counts1.length - 1;
                SpUtils.putIntValue("count", counts1.length - 1);
            }

            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts1[count]);
        } else if (intValue == 2) {
            int count = SpUtils.getIntValue("count", 0);
            LogUtil.d("count" + count);
            if (count >= counts2.length) {
                count = counts2.length - 1;
                SpUtils.putIntValue("count", counts2.length - 1);
            }

            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts2[count]);
        } else if (intValue == 3) {

            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts3[SpUtils.getIntValue("count", 0)]);
        }
        binding.tvMinTp.setText(UIUtils.getString(R.string.text_min_tp) + "   " + SpUtils.getIntValue(MyConstant.min_limit0, 0) + "°C");
        binding.tvMAxTp.setText(UIUtils.getString(R.string.text_max_tp) + "   " + SpUtils.getIntValue(MyConstant.max_limit0, 20) + "°C");
        if(intValue == 3){
            binding.tvMAxTp.setEnabled(false);
            binding.tvMinTp.setEnabled(false);
        }else {
            binding.tvMAxTp.setEnabled(true);
            binding.tvMinTp.setEnabled(true);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("onResume");
        if (binding.resultButton != null) {
            binding.resultButton.setEnabled(true);
        }
        if (binding.resultFiledButton != null) {
            binding.resultFiledButton.setEnabled(true);
        }
    }


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected FragmentSettingBinding initializeBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingBinding.inflate(inflater, container, false); // Inflate with View Binding
    }

    private void showSingleChoiceDialog() {
        final String[] items = new String[]{UIUtils.getString(R.string.text_measure_normal_mode), UIUtils.getString(R.string.text_measure_compression_mode)};
        final QMUIDialog.CheckableDialogBuilder dialogBuilder = new QMUIDialog.CheckableDialogBuilder(getActivity())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtils.putIntValue(MyConstant.tpMode, which);
                        if (which == 0) {
                            binding.tvMode.setText(UIUtils.getString(R.string.text_measure_mode) + "   " + UIUtils.getString(R.string.text_measure_normal_mode));
                        } else {
                            binding.tvMode.setText(UIUtils.getString(R.string.text_measure_mode) + "   " + UIUtils.getString(R.string.text_measure_compression_mode));
                        }
                        dialog.dismiss();

                    }
                });

        dialogBuilder.create(R.style.DialogTheme2).show();
    }

    private void setupClickListeners() {
        binding.tvDelay.setOnClickListener(v ->
                showNumpicker(delayTime, SpUtils.getIntValue("delay", 1), 1)
        );

        binding.tvInterval.setOnClickListener(v ->
                showNumpicker(intervals, SpUtils.getIntValue("interval", 2), 2)
        );

        binding.tvMode.setOnClickListener(v -> showSingleChoiceDialog());

        binding.tvCount.setOnClickListener(v -> {
            int intValue = SpUtils.getIntValue(MyConstant.tpMode, 0);
            if (intValue == 0) {
                showNumpicker(counts, SpUtils.getIntValue("count", 0), 3);
            } else if (intValue == 1) {
                showNumpicker(counts1, SpUtils.getIntValue("count", 0), 3);
            } else if (intValue == 2) {
                showNumpicker(counts2, SpUtils.getIntValue("count", 0), 3);
            } else if (intValue == 3) {
                showNumpicker(counts3, SpUtils.getIntValue("count", 0), 3);
            }
        });

        binding.tvMinTp.setOnClickListener(v ->
                showNumpicker(thresholds, SpUtils.getIntValue(MyConstant.min_limit0 + "value", 12), 4)
        );

        binding.tvMAxTp.setOnClickListener(v ->
                showNumpicker(thresholds, SpUtils.getIntValue(MyConstant.max_limit0 + "value", 27), 5)
        );

        binding.applyConfigButton.setOnClickListener(v -> applyConfig());

        binding.resultButton.setOnClickListener(v -> {
            mContext.nfcDialog();
            mContext.mImFragment.mStatu = 9;
            mContext.mImFragment.isShowFiled = false;
            mContext.FLAG = 0;
        });

        binding.stopButton.setOnClickListener(v -> {
            mContext.mImFragment.mStatu = 8;
            mContext.FLAG = 0;
            mContext.nfcDialog();
        });

        binding.resultFiledButton.setOnClickListener(v -> {
            mContext.nfcDialog();
            mContext.mImFragment.mStatu = 9;
            mContext.mImFragment.isShowFiled = true;
            mContext.FLAG = 0;
        });
    }

    private void applyConfig() {
        // Handle delay
        String delay = binding.tvDelay.getText().toString().trim();
        String[] split = delay.split(":");
        if (split[1].contains("no delay")) {
            SpUtils.putIntValue(MyConstant.delayTime, 0);
        } else if (split[1].contains("minutes")) {
            String[] s = split[1].split("minutes");
            SpUtils.putIntValue(MyConstant.delayTime, Integer.parseInt(s[0].trim()));
        } else if (split[1].contains("hour")) {
            String[] s = split[1].split("hour");
            SpUtils.putIntValue(MyConstant.delayTime, Integer.parseInt(s[0].trim()) * 60);
        }

        // Handle interval
        String interval = binding.tvInterval.getText().toString().trim();
        String[] split1 = interval.split(":");
        if (split1[1].contains("s") && !split1[1].contains("minutes")) {
            String s = split1[1].replace("s", "").trim();
            SpUtils.putIntValue(MyConstant.intervalTime, Integer.parseInt(s));
        } else if (split1[1].contains("minutes")) {
            String minutes = split1[1].replace("minutes", "").trim();
            SpUtils.putIntValue(MyConstant.intervalTime, Integer.parseInt(minutes) * 60);
        } else if (split1[1].contains("hour")) {
            String hour = split1[1].replace("hour", "").trim();
            SpUtils.putIntValue(MyConstant.intervalTime, Integer.parseInt(hour) * 60 * 60);
        }

        // Handle count
        String count = binding.tvCount.getText().toString().trim();
        String[] split2 = count.split(":");
        SpUtils.putIntValue(MyConstant.tpCount, Integer.parseInt(split2[1].trim()));

        // Handle min tp
        String minTp = binding.tvMinTp.getText().toString().trim();
        String[] split3 = minTp.split(":");
        int min = Integer.parseInt(split3[1].replace("°C", "").trim());
        SpUtils.putIntValue(MyConstant.min_limit0, min);

        // Handle max tp
        String maxTp = binding.tvMAxTp.getText().toString().trim();
        String[] split4 = maxTp.split(":");
        int max = Integer.parseInt(split4[1].replace("°C", "").trim());
        SpUtils.putIntValue(MyConstant.max_limit0, max);

        // Check min and max values
        if (min > max) {
            HintDialog.messageDialog(mContext, UIUtils.getString(R.string.text_min_max));
            return;
        }

        mContext.mImFragment.mStatu = 7;
        mContext.FLAG = 0;
        mContext.nfcDialog();
    }
    private String[] delayTime = {"0 minutes", "1 minutes", "2 minutes", " 5 minutes", "10 minutes", "15 minutes", "30 minutes", "1 hour", "2 hour", "4 hour"};
    final private String[] intervals = {"1s", "2s", "5s", "6s", "8s", "10s", "12s", "15s", "20s", "25s", "30s", "35s", "40s", "50s", "60s", "75s", "90s", "100s", "120s", "5 minutes", "10 minutes", "15 minutes", "30 minutes", "1 hour"};
    final private String[] counts = {"2", "3", "5", "8", "10", "20", "30", "50", "80", "100", "200", "300", "500", "800", "1000", "2000", "3000", "4000", "4864"};
    final private String[] counts1 = {"2", "3", "5", "8", "10", "20", "30", "50", "80", "100", "200", "300", "500", "800", "1000", "2000", "3000", "4000", "4864", "5000", "8000", "10000", "13000", "14592"};
    final private String[] counts2 = {"2", "3", "5", "8", "10", "20", "30", "50", "80", "100", "200", "300", "500", "800", "1000", "2000", "3000", "4000", "4864", "5000", "8000", "9000", "9728"};
    final private String[] counts3 = {"2", "3", "5", "8", "10", "20", "30", "50", "80", "100", "200", "300", "500", "800", "1000", "2000", "3000", "4000", "4864", "5000", "8000", "10000", "20000", "30000", "38912"};
    final private static String[] thresholds = new String[]{"-40°C", "-30°C", "-20°C", "-18°C", "-15°C", "-10°C", "-8°C", "-5°C", "-4°C", "-3°C", "-2°C", "-1°C", "0°C", "1°C", "2°C", "3°C", "4°C", "5°C", "8°C", "10°C", "15°C", "18°C", "20°C", "23°C", "25°C", "30°C", "35°C", "40°C", "50°C", "60°C", "70°C", "80°C"}; /* Celsius */
    final private static int[] thresholdUnitIds = new int[]{R.string.celsius};

    public void showNumpicker(final String[] values, int selectedValue, final int type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setCancelable(false);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        final NumberPicker numberPicker = new NumberPicker(mContext);
        numberPicker.setDisplayedValues(values);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(values.length - 1);
        numberPicker.setValue(selectedValue);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setId(View.NO_ID);
        linearLayout.addView(numberPicker);
        alert.setView(linearLayout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                int value = numberPicker.getValue();
                String timeValue = values[value];
                switch (type) {
                    case 1:
                        SpUtils.putIntValue("delay", value);
                        binding.tvDelay.setText(UIUtils.getString(R.string.text_measure_delay) + "   " + delayTime[value]);
                        if (value == 0) {
                            SpUtils.putIntValue(MyConstant.delayTime, 0);
                        } else if (0 < value && value < 7) {
                            String minutes = timeValue.replace("minutes", "").trim();
                            SpUtils.putIntValue(MyConstant.delayTime, Integer.parseInt(minutes));

                            //                            MyConstant.DELAYTIME = Integer.parseInt(minutes);
                        } else {
                            String hour = timeValue.replace("hour", "").trim();
                            SpUtils.putIntValue(MyConstant.delayTime, Integer.parseInt(hour) * 60);
                            //                            MyConstant.DELAYTIME = Integer.parseInt(hour) * 60;
                        }
                        break;
                    case 2:
                        SpUtils.putIntValue("interval", value);
                        if (timeValue.contains("minutes")) {
                            String minutes = timeValue.replace("minutes", "").trim();
                            SpUtils.putIntValue(MyConstant.intervalTime, Integer.parseInt(minutes) * 60);
                        }
                        if (timeValue.contains("hour")) {
                            String hour = timeValue.replace("hour", "").trim();
                            SpUtils.putIntValue(MyConstant.intervalTime, Integer.parseInt(hour) * 60 * 60);
                        }
                        if (!timeValue.contains("minutes") && timeValue.contains("s")) {
                            SpUtils.putIntValue("interval", value);
                            String s = timeValue.replace("s", "").trim();
                            SpUtils.putIntValue(MyConstant.intervalTime, Integer.parseInt(s));
                        }
                        //                        MyConstant.INTERVALTIME = Integer.parseInt(s);
                        binding.tvInterval.setText(UIUtils.getString(R.string.text_measure_interval) + "   " + intervals[value]);
                        break;
                    case 3:
                        SpUtils.putIntValue("count", value);
                        SpUtils.putIntValue(MyConstant.tpCount, Integer.parseInt(timeValue));
                        //                        MyConstant.TPCOUNT = Integer.parseInt(timeValue);
                        int intValue = SpUtils.getIntValue(MyConstant.tpMode, 0);
                        if (intValue == 0) {
                            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts[value]);
                        } else if (intValue == 1) {
                            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts1[value]);
                        } else if (intValue == 2) {
                            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts2[value]);
                        } else if (intValue == 3) {
                            binding.tvCount.setText(UIUtils.getString(R.string.text_measure_count) + "   " + counts3[value]);
                        }
                        break;
                    case 4:
                        SpUtils.putIntValue(MyConstant.min_limit0 + "value", value);
                        binding.tvMinTp.setText(UIUtils.getString(R.string.text_min_tp) + "   " + thresholds[value]);
                        SpUtils.putIntValue(MyConstant.min_limit0, Integer.parseInt(timeValue.replace("°C", "")));

                        break;
                    case 5:
                        SpUtils.putIntValue(MyConstant.max_limit0 + "value", value);
                        binding.tvMAxTp.setText(UIUtils.getString(R.string.text_max_tp) + "   " + thresholds[value]);
                        SpUtils.putIntValue(MyConstant.max_limit0, Integer.parseInt(timeValue.replace("°C", "")));
                        break;
                    default:
                        break;
                }


            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.dismiss();
            }
        });
        alert.show();
    }


}
