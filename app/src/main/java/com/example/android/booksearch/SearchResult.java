package com.example.android.booksearch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {
    //Log message tag
    public static final String LOG_TAG = SearchResult.class.getSimpleName();
    //基本网址
    public static final String DOUBAN = "https://api.douban.com/v2/book/search?q=";

    public String targetUrl;
    public ListView listView;
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        //取得关键词
        Intent intent = getIntent();
        String keyWord = intent.getExtras().getString("keyWord");

        //取得目标HTTP地址
        targetUrl = DOUBAN + keyWord;

        //附着adapter
        listView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(SearchResult.this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        //无数据时显示默认TextView
        TextView empty = (TextView) findViewById(R.id.empty);
        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = mAdapter.getItem(position);
                Uri webUri = Uri.parse(currentBook.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,webUri);
                startActivity(webIntent);
            }
        });


        BookAsyncTask task = new BookAsyncTask();
        try {
            task.execute(new URL(targetUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {
        @Override
        protected void onPostExecute(ArrayList<Book> data) {
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }

        //后台工作 根据url 返回数据
        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // 创建url
            URL url = createUrl(targetUrl);
            String jsonResponse = "";
            try {
                //开始连接,从数据流读取数据
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException");
            }

            //调用解析JSON方法
            ArrayList<Book> booksArrayList = extractFeatureFromJson(jsonResponse);
            return booksArrayList;
        }

        //URL String to url
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating url", exception);
            }
            return url;
        }

        //Json解析细节
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    //从数据流读取数据 自己定义
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem with retrieving the book Json results");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    //必须处理IOException
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<Book> extractFeatureFromJson(String bookJson) {
            ArrayList<Book> finalArrarlist = new ArrayList<>();
            try {
                JSONObject baseJsonResponse = new JSONObject(bookJson);
                //如果没有匹配的结果,count将为零
                int count = baseJsonResponse.optInt("count");

                JSONArray books = baseJsonResponse.optJSONArray("books");
                //循环提取书籍信息
                if (books.length() > 0 && count != 0) {
                    for (int index = 0; index < books.length(); index++) {
                        JSONObject singleBook = books.optJSONObject(index);

                        //提取评分
                        JSONObject rating = singleBook.optJSONObject("rating");
                        String rateString = rating.optString("average");

                        //提取作者
                        JSONArray authorArray = singleBook.optJSONArray("author");

                        StringBuilder authorString = new StringBuilder();
                        for (int i = 0; i < authorArray.length(); i++) {
                            String singleAuthor = authorArray.getString(i);
                            authorString.append(singleAuthor).append(" ");
                        }

                        //提取图像url
                        String imageUrl = singleBook.optString("image");
                        //提取出版社
                        String publisherString = singleBook.optString("publisher");
                        //提取书名
                        String titleString = singleBook.optString("title");
                        //提取网页URL
                        String webUrl = singleBook.optString("alt");

                        //添加到Book 列表中,返回ArrayList
                        Book singlebook = new Book(titleString, rateString, authorString.toString(), publisherString, imageUrl,webUrl);
                        finalArrarlist.add(singlebook);
                    }
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the book Json results", e);
            }
            return finalArrarlist;
        }
    }
}
