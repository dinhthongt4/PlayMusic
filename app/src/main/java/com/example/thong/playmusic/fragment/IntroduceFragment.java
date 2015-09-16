package com.example.thong.playmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thong.playmusic.R;

/**
 * Created by thong on 9/15/15.
 */
public class IntroduceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_fragment_introduce,container,false);
        ImageView imgIntroduce = (ImageView) view.findViewById(R.id.imgIntroduce);
        Bundle bundle = getArguments();
        imgIntroduce.setBackgroundResource(bundle.getInt("count"));

        return view;
    }
}
