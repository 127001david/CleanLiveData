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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countViewModel = new ViewModelProvider(getActivity()
                , new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CountViewModel.class);
        epidemicViewModel = new ViewModelProvider(getActivity()
                , new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(EpidemicViewModel.class);
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
        countObserver = integer -> {
            Log.d("CountViewModel", "onViewCreated: " + integer);
            mTvCount.setText(integer + "");
        };
        countViewModel.countLiveData.observe(this, countObserver);

        epidemicViewModel.epidemicLiveData.observe(this
                , epidemic -> Log.d(EpidemicViewModel.TAG, "Second: " + epidemic.total));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CountViewModel", "onPause: Second");
        countViewModel.countLiveData.removeObserver(countObserver);
        epidemicViewModel.epidemicLiveData.removeObserver(this);
    }
}
