package com.fmsh.temperature.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import androidx.viewbinding.ViewBinding;
import com.fmsh.temperature.R;
import com.fmsh.temperature.databinding.ActivityMainBinding;
import com.fmsh.temperature.fragment.BaseFragment;
import com.fmsh.temperature.fragment.IMFragment;
import com.fmsh.temperature.fragment.SettingFragment;
import com.fmsh.temperature.tools.BroadcastManager;
import com.fmsh.temperature.util.ActivityUtils;
import com.fmsh.temperature.util.ExcelUtils;
import com.fmsh.temperature.util.LogUtil;
import com.fmsh.temperature.util.TimeUitls;
import com.fmsh.temperature.util.UIUtils;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

//import butterknife.BindView;
//import butterknife.OnClick;

public class MainActivity extends BaseActivity {

//    @BindView(R.id.topbar)
//    QMUITopBarLayout topbar;
//    @BindView(R.id.fl_fragment)
//    FrameLayout flFragment;
//    @BindView(R.id.cb_lab1)
//    CheckBox cbLab1;
//    @BindView(R.id.cb_lab2)
//    CheckBox cbLab2;
private ActivityMainBinding binding;

    public IMFragment mImFragment;
    private SettingFragment mSettingFragment;
    /**
     * 0 为即使测温页面,1 为RTC 测温页面
     */
    public int FLAG = 0 ;
    private QMUIDialog mQmuiDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected ActivityMainBinding inflateBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
       if (!hasNfc(this)){
           startAppSettings();
       }

        TimeUitls.getTimeZone();

    }

    @Override
    protected void initView() {
        String version = "1.0.0";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();


        }
//
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topbar.setTitle(UIUtils.getString(R.string.text_lab1));
        binding.topbar.addLeftTextButton(UIUtils.getString(R.string.text_version) + version, 0x124);
//        topbar.setTitle(UIUtils.getString(R.string.text_lab1));
//        topbar.addLeftTextButton(UIUtils.getString(R.string.text_version)+version,0x124);

        binding.topbar.addRightImageButton(R.mipmap.more,0x111).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImFragment != null) {
                    mImFragment.showBottomSheet();
                }
            }
        });

        if (mImFragment == null) {
            mImFragment = new IMFragment();
        }
        if (!mImFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_fragment, mImFragment).commit();
        }
        binding.cbLab1.setOnClickListener(v -> handleLab1Click());
        binding.cbLab2.setOnClickListener(v -> handleLab2Click());
    }

    public void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        if (nextFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //判断nextFragment是否添加
            if (!nextFragment.isAdded()) {
                //隐藏当前Fragment
                if (fromFragment != null) {
                    transaction.hide(fromFragment);
                }
                transaction.add(R.id.fl_fragment, nextFragment).commit();
            } else {
                //隐藏当前Fragment
                if (fromFragment != null) {
                    transaction.hide(fromFragment);
                }
                transaction.show(nextFragment).commit();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void handleLab1Click() {
                if (mImFragment == null) {
                    mImFragment = new IMFragment();
                }
                FLAG = 0;
                mImFragment.mStatu = 0;
                binding.topbar.setTitle(UIUtils.getString(R.string.text_lab1));
                switchFragment(mSettingFragment, mImFragment);
                binding.cbLab1.setChecked(true);
                binding.cbLab2.setChecked(false);
                binding.topbar.addRightImageButton(R.mipmap.more,0x111).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mImFragment != null) {
                            mImFragment.showBottomSheet();
                        }
                    }
                });
    }

    private void handleLab2Click() {
                if (mSettingFragment == null) {

                    mSettingFragment = new SettingFragment();
                }
                FLAG = 1;
                binding.topbar.setTitle(UIUtils.getString(R.string.text_lab2));
                switchFragment(mImFragment, mSettingFragment);
                binding.cbLab1.setChecked(false);
                binding.cbLab2.setChecked(true);
                binding.topbar.removeAllRightViews();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("destory");
        BroadcastManager.getInstance(mContext).destroy("instruct");
    }


    public void nfcDialog(){
        if(mQmuiDialog != null){
            mQmuiDialog.dismiss();
            mQmuiDialog =null;
        }
        mQmuiDialog = new QMUIDialog.CustomDialogBuilder(ActivityUtils.instance.getCurrentActivity())
                .setLayout(R.layout.dialog_nfc_hint).addAction(UIUtils.getString(R.string.text_cancel), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).create(R.style.DialogTheme2);

        mQmuiDialog.show();
        mQmuiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mImFragment.mStatu == 7 || mImFragment.mStatu == 8 || mImFragment.mStatu == 9){
                    FLAG = 1;
                    //防止在定时测温界面响应即使测温结果
                }
                mImFragment.mStatu = 0;

            }
        });
    }
    public void disNFCDialog(){
        if(mQmuiDialog != null){

            mQmuiDialog.dismiss();
        }
    }

    public  boolean hasNfc(Activity context){

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        // adapter存在，能启用
        return adapter != null && adapter.isEnabled();

    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        try {
            Intent intent = new Intent(
                    Settings.ACTION_NFC_SETTINGS);
            startActivityForResult(intent, 100);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(requestCode + " --- " + resultCode);

        if (requestCode == 100) {
            if (!hasNfc(this)) {
                // Ensure that the dialog is shown only if NFC is not available.
                showDialog();
            } else {
                LogUtil.d("NFC enabled successfully.");
            }
        }
    }

    private void showDialog() {
        QMUIDialog qmuiDialog = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(UIUtils.getString(R.string.tips))
                .setMessage(UIUtils.getString(R.string.open_nfc))
                .addAction(UIUtils.getString(R.string.text_cancel), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        // Instead of directly finishing, show a message or perform another action.
                        // You may log this event or return to a previous activity.
                        LogUtil.d("NFC not enabled, staying in app.");
                    }
                })
                .addAction(UIUtils.getString(R.string.setting), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        // Open NFC settings to allow the user to enable NFC.
                        startAppSettings();
                    }
                })
                .create();

        qmuiDialog.show();
    }

}
