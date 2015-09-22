package com.example.thong.playmusic.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.thong.playmusic.model.ChildMusicOnline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thongdt on 17/09/2015.
 */
public class NewAlbumSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener{

    private List<ChildMusicOnline> mChildMusicOnlines;
    private boolean[] mSelected;
    private String mDefaultText;
    private MultiSpinnerListener mMultiSpinnerListener;
    public NewAlbumSpinner(Context context) {
        super(context);

    }
    public NewAlbumSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewAlbumSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { mDefaultText });
        setAdapter(adapter);
        mMultiSpinnerListener.onItemsSelected(mSelected);

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        if (b)
            mSelected[i] = true;
        else
            mSelected[i] = false;
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        ArrayList<String> items = new ArrayList<>();
        for(int i = 0; i < mChildMusicOnlines.size(); i++) {
            items.add(mChildMusicOnlines.get(i).getTitle());
        }

        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), mSelected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<ChildMusicOnline> childMusicOnlines, String allText,
                         MultiSpinnerListener listener) {
        this.mChildMusicOnlines = childMusicOnlines;
        this.mDefaultText = allText;
        this.mMultiSpinnerListener = listener;

        // all selected by default
        mSelected = new boolean[mChildMusicOnlines.size()];
        for (int i = 0; i < mSelected.length; i++)
            mSelected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}
