package com.example.final_project_android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project_android.R;
import com.example.final_project_android.listener.IRecyclerClickListener;
import com.example.final_project_android.models.CategoryModel;
import com.example.final_project_android.ui.FurnitureListFragment;
import com.example.final_project_android.util.Common;


import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    Context context;
    List<CategoryModel> categoryList;

    public CategoriesAdapter(Context context, List<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(LayoutInflater.from(context).
                inflate(R.layout.layout_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImage()).into(holder.imageCategory);
        holder.nameCategory.setText(new StringBuilder(categoryList.get(position).getName()));

        //Event
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                // هان لما اضغط عpostion الي فالاريليست بخزن الصورة والنص الي فيها وبحطهم ف common
                Common.categorySelected = categoryList.get(pos);
                //تخزين العنصر المضغوط عليه
                Fragment fragment = new FurnitureListFragment();
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.commit();

                Log.d("Selected", categoryList.get(pos).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageCategory;
        TextView nameCategory;
        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.image_category);
            nameCategory = itemView.findViewById(R.id.text_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(v, getAdapterPosition());
        }
    }

}
