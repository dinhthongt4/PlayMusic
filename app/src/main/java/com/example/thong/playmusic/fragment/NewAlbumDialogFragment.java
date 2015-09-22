package com.example.thong.playmusic.fragment;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.widget.NewAlbumSpinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;

@EFragment(R.layout.diaglog_fragment_new_album)
public class NewAlbumDialogFragment extends DialogFragment {

    private ArrayList<ChildMusicOnline> mChildMusicOnlines;
    private boolean[] mSelected;
    private OnClickSubmitListener mOnClickSubmitListener;

    @ViewById(R.id.edtNameAlbum)
    EditText mEdtNameAlbum;

    @ViewById(R.id.planetSpinner)
    NewAlbumSpinner mSpinnerMusics;

    @AfterViews
    void init() {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getListMusics();

        mSpinnerMusics.setItems(mChildMusicOnlines, "Select Music", new NewAlbumSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                mSelected = selected;
            }
        });

    }

    public void setOnClickSubmitListener(OnClickSubmitListener onClickSubmitListener) {
        mOnClickSubmitListener = onClickSubmitListener;
    }

    public void getListMusics() {
        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
        try {
            managerDatabase.open();
            mChildMusicOnlines = managerDatabase.getDataMediaInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            managerDatabase.close();
        }
    }

    @Click(R.id.txtCancel)
    void setClickCancel() {
        getDialog().dismiss();
    }

    @Click(R.id.txtSubmit)
    void setClickSubmit() {
        if(mOnClickSubmitListener != null) {


                ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
                try {
                    managerDatabase.open();
                    managerDatabase.insertMediaGroupName(mEdtNameAlbum.getText().toString());
                    int idAlbum = managerDatabase.getIDAlbum(mEdtNameAlbum.getText().toString());
                    Log.v("New Album","idAlbum" + idAlbum);
                    if(mSelected != null) {
                        for (int i = 0; i < mSelected.length; i++) {
                            if(mSelected[i]) {
                                managerDatabase.insertMediaGroup(mChildMusicOnlines.get(i).getIdMusic(),idAlbum);
                            }
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    managerDatabase.close();
                }


            mOnClickSubmitListener.onClick();
            getDialog().dismiss();
        }
    }

    public interface OnClickSubmitListener {
        void onClick();
    }
}
