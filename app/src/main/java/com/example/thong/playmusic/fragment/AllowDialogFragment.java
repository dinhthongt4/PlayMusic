package com.example.thong.playmusic.fragment;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Window;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.database.ManagerDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.sql.SQLException;

/**
 * Created by thongdt on 28/09/2015.
 */

@EFragment(R.layout.dialog_fragment_allow)
public class AllowDialogFragment extends DialogFragment {

    private OnClickSubmitListener mOnClickSubmitListener;

    @AfterViews
    void init() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Click(R.id.txtCancel)
    void setClickCancel() {
        getDialog().dismiss();
    }

    @Click(R.id.txtSubmit)
    void setClickSubmit() {
        if(mOnClickSubmitListener != null) {
            mOnClickSubmitListener.onClick();
        }
        getDialog().dismiss();
    }

    public void setOnClickSubmitListener(OnClickSubmitListener onClickSubmitListener) {
        mOnClickSubmitListener = onClickSubmitListener;
    }


    public interface OnClickSubmitListener {
        void onClick();
    }
}
