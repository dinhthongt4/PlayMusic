package com.example.thong.playmusic.fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerAddAlbumAdapter;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.model.Album;
import com.example.thong.playmusic.widget.SpacesItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thongdt on 29/09/2015.
 */

@EFragment(R.layout.dialog_fragment_add_album)
public class AddAlubmDialogFragment extends DialogFragment {

    @ViewById(R.id.recyclerAddAlbum)
    RecyclerView mRecyclerViewMusic;

    private RecyclerAddAlbumAdapter mRecyclerAddAlbumAdapter;
    private ArrayList<Album> mAlbums;
    private OnSelectedAlbumListener mOnSelectedAlbumListener;

    @AfterViews
    void init() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerViewMusic.setLayoutManager(manager);
        mAlbums = new ArrayList<>();
        getAlbumName();
        mRecyclerAddAlbumAdapter = new RecyclerAddAlbumAdapter(mAlbums);
        int spacingItem = getResources().getDimensionPixelSize(R.dimen.margin_10);
        mRecyclerViewMusic.addItemDecoration(new SpacesItemDecoration(spacingItem));
        mRecyclerViewMusic.setAdapter(mRecyclerAddAlbumAdapter);
        setListener();
    }

    private void getAlbumName() {

        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
        try {
            managerDatabase.open();
            mAlbums.addAll(managerDatabase.getNameAlbum());
            Log.v("Album", "" + mAlbums.size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            managerDatabase.close();
        }
    }

    public void setOnSelectedAlbumListener(OnSelectedAlbumListener onSelectedAlbumListener) {
        mOnSelectedAlbumListener = onSelectedAlbumListener;
    }

    private void setListener() {
        mRecyclerAddAlbumAdapter.setOnClickAlbumListener(new RecyclerAddAlbumAdapter.OnClickAlbumListener() {
            @Override
            public void OnClick(int position) {
                    if(mOnSelectedAlbumListener != null) {
                        mOnSelectedAlbumListener.OnSelected(mAlbums.get(position).getIdAlbum());
                    }
                getDialog().dismiss();
                }
            }
            );

            mRecyclerAddAlbumAdapter.setOnClickNewAlbumListener(new RecyclerAddAlbumAdapter.OnClickNewAlbumListener()

            {
                @Override
                public void OnClick () {
                NewAlbumDialogFragment newAlbumDialogFragment = new NewAlbumDialogFragment_();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                newAlbumDialogFragment.show(ft, "ABC");

                newAlbumDialogFragment.setOnClickSubmitListener(new NewAlbumDialogFragment.OnClickSubmitListener() {
                    @Override
                    public void onClick() {
                        mAlbums.clear();
                        getAlbumName();
                        mRecyclerAddAlbumAdapter.notifyDataSetChanged();
                    }
                });
            }
            }

            );
        }

        public interface OnSelectedAlbumListener {
            void OnSelected(int albumId);
        }
    }
