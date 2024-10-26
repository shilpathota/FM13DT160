package com.fmsh.temperature.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;
import com.fmsh.temperature.activity.MainActivity;

//import butterknife.ButterKnife;

/**
 * Created by wyj on 2018/7/2.
 */
public abstract class BaseFragment<T extends ViewBinding> extends Fragment  implements View.OnClickListener{

    protected T binding; // Generic binding to be initialized by child fragments

    protected abstract int setView();

    protected abstract void init(View view);

    protected abstract void initData(Bundle savedInstanceState);
    public MainActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext =(MainActivity) getActivity();


    }
    protected abstract T initializeBinding(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = initializeBinding(inflater, container); // Initialize binding in child
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

    }

}
