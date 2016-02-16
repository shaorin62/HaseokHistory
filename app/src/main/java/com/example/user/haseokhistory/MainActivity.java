package com.example.user.haseokhistory;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intro Start~~~!!
        LoadIntroActivity();

        //이전에 실행되던 앱이 없을 경우에만 새로 시작
       // if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MiniTabFragment fragment = new MiniTabFragment();

            //프래그먼트가 존재하지 않거나 선택된 항목이 다를경우 프래그먼트를 생성
            transaction.replace(R.id.mini_content_fragment, fragment);
            transaction.commit();
        //}
    }

    //----------------------------- 앱 인트로 시작------------------------------------------
    public void LoadIntroActivity(){
        //intent 객체 생성하고 인트로 액티비티 클래스를 지정
        Intent intent  = new Intent(getApplicationContext(), IntroActivity.class);

        //intent 객체 배경 이미지 지정
        intent.putExtra("BackImage", R.drawable.intro);
        //intro Activity 실행
        startActivity(intent);

    }
    //----------------------------- 앱 인트로 종료------------------------------------------

    //------------------------------기본동작 매서드-----------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------------기본동작 매서드 종료 -----------------------------------------
}
