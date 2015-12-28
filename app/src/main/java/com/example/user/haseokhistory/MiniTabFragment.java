package com.example.user.haseokhistory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class MiniTabFragment extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

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

        Button button = (Button)view.findViewById(R.id.datePickButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "나는 fragment", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    //사용자의 두번째 탭
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

}
