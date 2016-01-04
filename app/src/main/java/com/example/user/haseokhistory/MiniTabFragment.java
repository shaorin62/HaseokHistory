package com.example.user.haseokhistory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MiniTabFragment extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    //달력과 시간을 나타냄
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    //팝업을 띄우기 위한 다이얼로그
    AlertDialog alertDialog;

    EditText pop_seq;
    EditText pop_date;
    EditText pop_time;
    EditText pop_milk;

    //Tab1
    ArrayList<String> mArrMilk = new ArrayList<String>();
    ArrayAdapter<String> mAdapter;
    int mSelectIndex = -1;
    TextView TextDate;

    //TAB2
    TextView DateText ;
    TextView TimeText ;
    EditText MilkText;
    ListView mListmilk;

    DbHelper mDbHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;

    GregorianCalendar calendar = new GregorianCalendar();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day= calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);

    Calendar c = Calendar.getInstance();
    Date mnow = new Date();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mini_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Toast.makeText(getActivity(), "MiniTabCreated", Toast.LENGTH_SHORT).show();

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MiniPagerAdapter());

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        mSlidingTabLayout.setViewPager(mViewPager, caculateScreenX());
    }

    //현재 화면 X 축 사이즈 구하기
    private int caculateScreenX() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    class MiniPagerAdapter extends PagerAdapter {

        //탭 카운팅
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        //상단 탭 화면 타이틀 구현
        @Override
        public CharSequence getPageTitle(int position) {

            String TitleName = "";

            switch (position) {
                case 0:
                    TitleName = "History";
                    break;
                case 1:
                    TitleName = "Input Data";
                    break;
                case 2:
                    TitleName = "ETC";
                    break;
            }
            return TitleName ;
        }

        //화면 레이아웃 그림
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;

            if(position == 0){

                view = HistoryTab(container);

            }else if(position == 1){

                view = InputDataTab(container);

            }else{
                view = ETCTab(container, position);
            }

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    //------------------생성된 탭 별 이벤트 -------------------------------------------

    //사용자의 첫번째 탭
    public View HistoryTab (ViewGroup container){
        View view;
        // Inflate a new layout from our resources
        view = getActivity().getLayoutInflater().inflate(R.layout.mainlist,
                container, false);

        // Add the newly created View to the ViewPager
        container.addView(view);


        TextDate = (TextView)view.findViewById(R.id.TextDate);
        //오늘 날짜를 생성한다.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TextDate.setText(String.valueOf(format.format(mnow)));

        ImageView mLeft = (ImageView)view.findViewById(R.id.imgLeft);
        ImageView mRight = (ImageView)view.findViewById(R.id.imgRight);

        mLeft.setOnClickListener(new ButtonClick());
        mRight.setOnClickListener(new ButtonClick());

        //DBHelper 객체 생성하여 변수에 담기
        mDbHelper = new DbHelper(view.getContext());
        //읽고 쓰기가 가능한 DB 객체를 반환합니다.
        mDb =  mDbHelper.getWritableDatabase();

        //모든 리스트 화면에 갱신
        initListView(view);
        SelectRtn();

        return view;

    }

    //사용자의 두번째 탭
    public View InputDataTab (ViewGroup container){
        View view;
        String strAM_PM = "";

        // Inflate a new layout from our resources
        view = getActivity().getLayoutInflater().inflate(R.layout.input_milk,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        //날짜와 시간을 가져오는 팝업
        Button datebutton = (Button)view.findViewById(R.id.datePickButton);
        Button timebutton = (Button)view.findViewById(R.id.timePickButton);
        Button btnInsert = (Button)view.findViewById(R.id.btnInsert);

        DateText = (TextView)view.findViewById(R.id.dateText);
        TimeText = (TextView)view.findViewById(R.id.timeText);

        //날짜 형식에 맞추기
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateText.setText(String.valueOf(format.format(mnow)));

        //시간 형식 맞추기
        Calendar datetime = Calendar.getInstance();
        if (datetime.get(Calendar.AM_PM) == Calendar.AM){
            strAM_PM = "AM";
        }else if (datetime.get(Calendar.AM_PM) == Calendar.PM){
            strAM_PM = "PM";
        }
        TimeText.setText(strAM_PM + ":" + hour + ":" + minute);

        ImageView mUp = (ImageView)view.findViewById(R.id.imgUp);
        ImageView mDown = (ImageView)view.findViewById(R.id.imgDown);

        MilkText = (EditText)view.findViewById(R.id.MilkText);
        //------------------------------------------------

        //버튼 클릭 이벤트 리스너
        datebutton.setOnClickListener(new ButtonClick());
        timebutton.setOnClickListener(new ButtonClick());
        btnInsert.setOnClickListener(new ButtonClick());
        mUp.setOnClickListener(new ButtonClick());
        mDown.setOnClickListener(new ButtonClick());



        return view;

    }

    //각종 버튼 클릭 이벤트 리스너
    public class ButtonClick implements View.OnClickListener{

        public void onClick(final View v){
            int ReturnValue = 0;

            switch (v.getId()){
                case R.id.datePickButton :
                    DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener(){
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                            //Toast.makeText(v.getContext(),year +  " : " + (monthOfYear +1) + " : "  + dayOfMonth, Toast.LENGTH_SHORT).show();
                            DateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    };

                    datePickerDialog = new DatePickerDialog(v.getContext(), callback, year,month,day);
                    datePickerDialog.show();

                    break;
                case R.id.timePickButton :
                    TimePickerDialog.OnTimeSetListener callback2 = new TimePickerDialog.OnTimeSetListener(){
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                            //Toast.makeText(v.getContext(),hourOfDay +  " : " + minute, Toast.LENGTH_SHORT).show();

                            String strAM_PM = "";
                            Calendar datetime = Calendar.getInstance();
                            if (datetime.get(Calendar.AM_PM) == Calendar.AM){
                                strAM_PM = "AM";
                            }else if (datetime.get(Calendar.AM_PM) == Calendar.PM){
                                strAM_PM = "PM";
                            }

                            TimeText.setText(hourOfDay + ":" + minute + " : " + strAM_PM);
                        }
                    };

                    timePickerDialog = new TimePickerDialog(v.getContext(), callback2, hour, minute,true );
                    timePickerDialog.show();
                    break;
                case R.id.imgUp :

                    ReturnValue = Integer.parseInt(MilkText.getText().toString());

                    if(ReturnValue == 0 ){
                        MilkText.setText("10");
                    }else{

                        ReturnValue += 10;
                        //Toast.makeText(v.getContext(),"--->" + ReturnValue, Toast.LENGTH_SHORT).show();
                        MilkText.setText(String.valueOf(ReturnValue));
                    }

                    break;
                case R.id.imgDown :

                    ReturnValue = Integer.parseInt(MilkText.getText().toString());

                    if(ReturnValue == 0){
                        MilkText.setText("0");
                    }else{
                        ReturnValue -= 10;
                        MilkText.setText(String.valueOf(ReturnValue));
                    }

                    //Toast.makeText(v.getContext(),"Down 버튼 클릭", Toast.LENGTH_SHORT).show();
                    //MilkText.setText(MilkText.getText() + "10" );
                    break;

                case R.id.imgLeft :

                    c.setTime(mnow);
                    c.add(Calendar.DATE, -1);
                    mnow = c.getTime();

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    TextDate.setText(String.valueOf(format.format(mnow)));
                    SelectRtn();

                    break;
                case R.id.imgRight :

                    c.setTime(mnow);
                    c.add(Calendar.DATE, 1);
                    mnow = c.getTime();

                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    TextDate.setText(String.valueOf(format2.format(mnow)));
                    SelectRtn();

                    break;

                case R.id.btnInsert:
                    onAddEvent();
                    break;
                case R.id.btnUpdate:
                    onUpdateEvent();
                    break;
                case R.id.btnDelete:
                    onDelEvent();
                    break;

            }
        }
    }

    //사용자의 세번째 탭
    public View ETCTab (ViewGroup container, int position){
        View view;
        TextView title;

        // Inflate a new layout from our resources
        view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        // Retrieve a TextView from the inflated View, and update it's text
        title = (TextView) view.findViewById(R.id.item_title);
        title.setText(String.valueOf(position + 1));

        return view;

    }

    //------------------생성된 탭 별 이벤트 종료-------------------------------------------


    //-------SQLite 생성과 업그레이드------------------------------------------

    class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context){
            super(context,"HaseokHistory", null, 1);
        }

        //앱이 실행될때 한번만 실행
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table Milk(seq integer PRIMARY KEY autoincrement, date TEXT, time TEXT, milk integer);");

        }

        //DB 업그레이드 될때 실행됨
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //기존 테이블 삭제하고 새로 생성
            db.execSQL("drop table if exists Milk");
            onCreate(db);
        }
    }

    //데이터 삽입
    public void onAddEvent(){
        String strDateText = DateText.getText().toString();
        String strTimeText = TimeText.getText().toString();



        int intMilkText = Integer.parseInt(MilkText.getText().toString());

        //입력한 데이터로 쿼리문 작성
        String strQuery = "insert into  Milk(date, time, milk) values('" + strDateText + "' , '" + strTimeText +"' , " + intMilkText + ");";
        mDb.execSQL(strQuery);

        SelectRtn();

        Toast.makeText(getActivity(), "데이터가 저장되었습니다. ", Toast.LENGTH_LONG).show();

        mCursor.moveToLast();

        //Log.d("TAG", "----->" + mSelectIndex);

        mSelectIndex = mCursor.getInt(0);

        //페이지 화면 이동
        mViewPager.setCurrentItem(0);
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
        SelectRtn();
        //갱신후 화면 닫기
        alertDialog.dismiss();
    }


    //데이터 삭제
    public void onDelEvent(){
        mDb.execSQL("delete from Milk where seq = " + mSelectIndex);
        //삭제하고 데이터 화면에 갱신
        SelectRtn();

        //갱신후 화면 닫기
        alertDialog.dismiss();
    }

    //-----------------------------DB 조회 SelectRtn-----------------
    public void initListView(View v){
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mArrMilk);
        mListmilk = (ListView)v.findViewById(R.id.listMilk);
        mListmilk.setAdapter(mAdapter);
        mListmilk.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListmilk.setDivider(new ColorDrawable(Color.GRAY));
        mListmilk.setDividerHeight(2);

        mListmilk.setOnItemClickListener(mItemListener);

    }

    public void SelectRtn(){

        String NowDate;
        NowDate = String.valueOf(TextDate.getText());
        Log.d("Tag", "--->" + NowDate);

        mArrMilk.clear();
        String Query = "select seq, date, time, milk  from Milk where date = '" + NowDate + "'";
        mCursor = mDb.rawQuery(Query, null);

        for(int i = 0; i < mCursor.getCount(); i++){
            //다음번 레코드로 커서 이동
            mCursor.moveToNext();
            //현재 레코드의 SEQ 를 구한다.
            int nSEQ = mCursor.getInt(0);
            String date = mCursor.getString(1);
            String time = mCursor.getString(2);
            int milk = mCursor.getInt(3);

            String strRecord = nSEQ + " : " + date + " | " + time + " | " + milk + "ML";

            Log.d("Tag", "--->" + strRecord);


            mArrMilk.add(strRecord);

        }
        mAdapter.notifyDataSetChanged();
    }

    //------------------개체 선택 Listener ---------------

    AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView parent, View view, int position, long id){
            ViewRecord(position);

        }
    };

    //팝업에서 선택된 레코드 보여주기
    public void ViewRecord(int nIndex){
        mCursor.moveToPosition(nIndex);
        int nSEQ = mCursor.getInt(0);
        String date = mCursor.getString(1);
        String time = mCursor.getString(2);
        int milk = mCursor.getInt(3);

        mSelectIndex = mCursor.getInt(0);


        //팝업으로 띄울 Layout
        final RelativeLayout layout = (RelativeLayout)View.inflate(getActivity(), R.layout.popup_dialog, null);
        //팝업의 EditText
        pop_seq = (EditText)layout.findViewById(R.id.edit_seq);
        pop_date = (EditText)layout.findViewById(R.id.edit_date);
        pop_time = (EditText)layout.findViewById(R.id.edit_time);
        pop_milk = (EditText)layout.findViewById(R.id.edit_milk);
        Button btnUpdate= (Button)layout.findViewById(R.id.btnUpdate);
        Button btnDelete = (Button)layout.findViewById(R.id.btnDelete);


        pop_seq.setText(String.valueOf(nSEQ));
        pop_seq.setFocusable(false);
        pop_seq.setClickable(false);

        pop_date.setText(date);
        pop_time.setText(time);
        pop_milk.setText(String.valueOf(milk));

        btnUpdate.setOnClickListener(new ButtonClick());
        btnDelete.setOnClickListener(new ButtonClick());

        //선택된 데이터 팝업 생성
        alertDialog  = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("DataSelect")
                .setView(layout)
                .setPositiveButton("OK", null)
                .show();


        alertDialog.show();

    }
}
