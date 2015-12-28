package com.example.user.haseokhistory;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MiniTabFragment extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    //달력과 시간을 나타냄
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    //TAB2
    TextView DateText ;
    TextView TimeText ;
    EditText MilkText;

    GregorianCalendar calendar = new GregorianCalendar();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day= calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);


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
            return 3;
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

    //사용자의 첫번째 탭
    public View HistoryTab (ViewGroup container){
        View view;
        // Inflate a new layout from our resources
        view = getActivity().getLayoutInflater().inflate(R.layout.mainlist,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        return view;

    }

    //사용자의 두번째 탭
    public View InputDataTab (ViewGroup container){
        View view;

        // Inflate a new layout from our resources
        view = getActivity().getLayoutInflater().inflate(R.layout.input_milk,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        //날짜와 시간을 가져오는 팝업
        Button datebutton = (Button)view.findViewById(R.id.datePickButton);
        Button timebutton = (Button)view.findViewById(R.id.timePickButton);

        DateText = (TextView)view.findViewById(R.id.dateText);
        TimeText = (TextView)view.findViewById(R.id.timeText);

        DateText.setText(year +  "-" + (month +1) + "-"  + day);
        TimeText.setText(hour +  ":" + minute);

        ImageView mUp = (ImageView)view.findViewById(R.id.imgUp);
        ImageView mDown = (ImageView)view.findViewById(R.id.imgDown);

        MilkText = (EditText)view.findViewById(R.id.MilkText);

        //------------------------------------------------

        //버튼 클릭 이벤트 리스너
        datebutton.setOnClickListener(new Tab2ButtonClick());
        timebutton.setOnClickListener(new Tab2ButtonClick());
        mUp.setOnClickListener(new Tab2ButtonClick());
        mDown.setOnClickListener(new Tab2ButtonClick());

        return view;

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


    //두번째 탭 클릭 이벤트 리스너
    public class Tab2ButtonClick implements View.OnClickListener{

        public void onClick(final View v){
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
                            TimeText.setText(hourOfDay + ":" + minute);
                        }
                    };

                    timePickerDialog = new TimePickerDialog(v.getContext(), callback2, hour, minute , true);
                    timePickerDialog.show();
                    break;
                case R.id.imgUp :

                    String value = "";
                    int ReturnValue = 0;

                    value = MilkText.getText().toString();
                    ReturnValue = Integer.parseInt(value);

                    if(ReturnValue == 0 ){
                        MilkText.setText("10");
                    }else{

                        ReturnValue += 10;
                        //Toast.makeText(v.getContext(),"--->" + ReturnValue, Toast.LENGTH_SHORT).show();
                        MilkText.setText(String.valueOf(ReturnValue));
                    }

                    break;
                case R.id.imgDown :
                    //Toast.makeText(v.getContext(),"Down 버튼 클릭", Toast.LENGTH_SHORT).show();
                    //MilkText.setText(MilkText.getText() + "10" );
                    break;

            }
        }
    }

}
