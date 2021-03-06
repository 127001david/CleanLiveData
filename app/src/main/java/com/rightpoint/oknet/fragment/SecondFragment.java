package com.rightpoint.oknet.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rightpoint.oknet.R;
import com.rightpoint.oknet.ok.OkError;
import com.rightpoint.oknet.vm.CountViewModel;
import com.rightpoint.oknet.vm.EpidemicViewModel;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/5 7:07 AM 
 */
public class SecondFragment extends Fragment {
    private TextView mTvCount;
    private CountViewModel countViewModel;
    private Observer<Integer> countObserver;
    private EpidemicViewModel epidemicViewModel;
    private Observer<String> epidemicObserver;
    private Observer<OkError> errorObserver;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countViewModel = new ViewModelProvider(getActivity()
                , new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CountViewModel.class);
        epidemicViewModel = new ViewModelProvider(getActivity()
                , new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(EpidemicViewModel.class);

        countObserver = integer -> mTvCount.setText(integer + "");
        epidemicObserver = epidemic -> Log.d(EpidemicViewModel.TAG, "Second: " + epidemic);
        errorObserver = okError -> Log.d(EpidemicViewModel.TAG
                , null != okError ? okError.errorMsg : "error");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fm_second, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvCount = getView().findViewById(R.id.tv_count);
    }

    @Override
    public void onResume() {
        super.onResume();

        countViewModel.countLiveData.observe(this, countObserver);
        epidemicViewModel.epidemicLiveData.observe(this
                , epidemicObserver, errorObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        countViewModel.countLiveData.removeObserver(countObserver);
        epidemicViewModel.epidemicLiveData.removeObserver(epidemicObserver);
    }
}
