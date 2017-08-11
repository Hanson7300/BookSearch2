package com.example.android.booksearch;

/**
 * Created by Hansson on 2017/8/10.
 */

public class Book {
    private String mTitle;
    private String mRate;
    private String mAuthor;
    private String mPublisher;
    private String mImageResourceUrl;


    public Book(String title,String rate, String author, String publisher, String imageResourceUrl) {
        mTitle = title;
        mRate = rate;
        mAuthor = author;
        mPublisher = publisher;
        mImageResourceUrl = imageResourceUrl;
    }

    public String getRate() {
        return mRate;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getImageResourceUrl() {
        return mImageResourceUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mTitle='" + mTitle + '\'' +
                ", mRate='" + mRate + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mPublisher='" + mPublisher + '\'' +
                ", mImageResourceUrl='" + mImageResourceUrl + '\'' +
                '}';
    }
}
