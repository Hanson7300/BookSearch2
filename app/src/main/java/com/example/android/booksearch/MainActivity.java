package com.example.android.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.android.booksearch.R.id.search;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到搜索按钮
        final Button search_button = (Button) findViewById(search);
        //找到文本框
        final EditText edit_text = (EditText) findViewById(R.id.edit_text_input);

        //点击搜索按钮
        // 打开搜索结果
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击搜索按钮时传入关键词
                String keyWord = edit_text.getText().toString();
                if(keyWord.length()>15){
                    keyWord.substring(0,15);
                }
                //如果没有输入关键词,显示Toast
                if (keyWord.matches("")) {
                    Toast.makeText(MainActivity.this, R.string.wrong_hint,Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //打开新Activity
                    Intent searchResultIntent = new Intent(MainActivity.this, SearchResult.class);
                    searchResultIntent.putExtra("keyWord", keyWord);
                    startActivity(searchResultIntent);
                }
            }
        });
    }
}
