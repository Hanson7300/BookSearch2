package com.example.android.booksearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hansson on 2017/8/10.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_book, parent, false);
        }

        //找到当前需要填充位置的 Book class数据
        Book currentBook = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getTitle());
        TextView authorTextView = (TextView) convertView.findViewById(R.id.author);
        authorTextView.setText(currentBook.getAuthor());
        TextView pressTextView = (TextView) convertView.findViewById(R.id.press);
        pressTextView.setText(currentBook.getPublisher());
        TextView rateTextView = (TextView) convertView.findViewById(R.id.rate);
        rateTextView.setText(String.valueOf(currentBook.getRate()));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        Picasso.with(imageView.getContext()).load(currentBook.getImageResourceUrl()).resize(202, 360).into(imageView);

        return convertView;
    }
}
