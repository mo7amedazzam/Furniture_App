package com.example.final_project_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project_android.R;
import com.example.final_project_android.database.DatabaseHelper;
import com.example.final_project_android.models.FurnitureModel;

import java.util.List;

public class FurnitureListAdapter extends RecyclerView.Adapter<FurnitureListAdapter.FurnitureListViewHolder> {
    private Context context;
    private List<FurnitureModel> furnitureModels;
    private DatabaseHelper db;

    public FurnitureListAdapter(Context context, List<FurnitureModel> furnitureModels) {
        this.context = context;
        this.furnitureModels = furnitureModels;
        db= new DatabaseHelper(context);

    }

    @NonNull
    @Override
    public FurnitureListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FurnitureListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_furniture_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FurnitureListViewHolder holder, int position) {
        Glide.with(context).load(furnitureModels.get(position).getImage()).into(holder.imageFood);
        holder.textPrice.setText(new StringBuilder("$")
                .append(furnitureModels.get(position).getPrice()));
        holder.textName.setText(new StringBuilder("").
                append(furnitureModels.get(position).getName()));



        holder.imageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.createItem(furnitureModels.get(position));
                Toast.makeText(context, "ADD DONE!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return furnitureModels.size();
    }

    public class FurnitureListViewHolder extends RecyclerView.ViewHolder  {
        TextView textName;
        TextView textPrice;
        ImageView imageFood;
        ImageView imageCart;


        public FurnitureListViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_food_name);
            textPrice = itemView.findViewById(R.id.text_food_price);
            imageFood = itemView.findViewById(R.id.image_food_list);
            imageCart = itemView.findViewById(R.id.image_cart);


        }


    }
}
