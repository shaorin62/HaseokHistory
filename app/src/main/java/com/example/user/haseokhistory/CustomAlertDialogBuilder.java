package com.example.user.haseokhistory;

/**
 * Created by USER on 2016-01-04.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomAlertDialogBuilder extends  AlertDialog.Builder{

    private final Context mContext;
    EditText pop_seq;
    EditText pop_date;
    EditText pop_time;
    EditText pop_milk;
    MiniTabFragment mMiniTabFragment;

    SQLiteDatabase mDb;

    int mSelectIndex = mMiniTabFragment.mSelectIndex;

    public CustomAlertDialogBuilder(Context context, Cursor mCursor) {
        super(context);

        int nSEQ = mCursor.getInt(0);
        String date = mCursor.getString(1);
        String time = mCursor.getString(2);
        int milk = mCursor.getInt(3);


        mContext = context;

        View customDialog = View.inflate(mContext, R.layout.popup_dialog, null);
        pop_seq = (EditText)customDialog.findViewById(R.id.edit_seq);
        pop_date = (EditText)customDialog.findViewById(R.id.edit_date);
        pop_time = (EditText)customDialog.findViewById(R.id.edit_time);
        pop_milk = (EditText)customDialog.findViewById(R.id.edit_milk);
        Button btnUpdate= (Button)customDialog.findViewById(R.id.btnUpdate);
        Button btnDelete = (Button)customDialog.findViewById(R.id.btnDelete);





        pop_seq.setText(String.valueOf(nSEQ));
        pop_seq.setFocusable(false);
        pop_seq.setClickable(false);

        pop_date.setText(date);
        pop_time.setText(time);
        pop_milk.setText(String.valueOf(milk));

        btnUpdate.setOnClickListener(new ButtonClick());
        btnDelete.setOnClickListener(new ButtonClick());

        setView(customDialog);

    }

    public class ButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnUpdate:
                    onUpdateEvent();
                    break;
                case R.id.btnDelete:
                    onDelEvent();
                    break;
            }

        }
    }

    //데이터 수정
    public void onUpdateEvent(){
        String strQuery;
        String strdate;
        String strtime;
        int intmilk;

        strdate = String.valueOf(pop_date.getText());
        strtime = String.valueOf(pop_time.getText());
        intmilk = Integer.parseInt(String.valueOf(pop_milk.getText()));

        strQuery = "update Milk set date = '" + strdate + "', time = '" + strtime + "', milk = " + intmilk + " where seq = " + mSelectIndex;

        mDb.execSQL(strQuery);
        mMiniTabFragment.SelectRtn();
        //갱신후 화면 닫기
        mMiniTabFragment.alertDialog.dismiss();
    }

    //데이터 삭제
    public void onDelEvent(){
        mDb.execSQL("delete from Milk where seq = " + mSelectIndex);
        //삭제하고 데이터 화면에 갱신
        mMiniTabFragment.SelectRtn();

        //갱신후 화면 닫기
        mMiniTabFragment.alertDialog.dismiss();
    }

}
