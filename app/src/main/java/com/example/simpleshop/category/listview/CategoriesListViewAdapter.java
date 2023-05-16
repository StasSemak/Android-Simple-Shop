package com.example.simpleshop.category.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.simpleshop.R;

import com.example.simpleshop.application.HomeApplication;
import com.example.simpleshop.constants.Urls;
import com.example.simpleshop.dto.category.CategoryItemDTO;

import java.util.List;

public class CategoriesListViewAdapter extends BaseAdapter {
    List<CategoryItemDTO> categories;
    LayoutInflater inflater;

    public CategoriesListViewAdapter(List<CategoryItemDTO> categories) {
        this.categories = categories;
        inflater = LayoutInflater.from(HomeApplication.getAppContext());
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categories.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.category_view, null);
        TextView textView = (TextView) view.findViewById(R.id.categoryName);
        ImageView imageView = (ImageView) view.findViewById(R.id.categoryImage);
        String url = Urls.BASE + "/images/" + categories.get(i).getImage();
        Glide.with(HomeApplication.getAppContext())
                .load(url)
                .apply(new RequestOptions().override(600))
                .into(imageView);
        textView.setText(categories.get(i).getName());
        return null;
    }
}
