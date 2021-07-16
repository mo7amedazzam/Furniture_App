package com.example.final_project_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.final_project_android.R;
import com.example.final_project_android.database.DatabaseHelper;
import com.example.final_project_android.listener.CartRecyclerClickListener;
import com.example.final_project_android.models.FurnitureModel;
import com.example.final_project_android.util.Common;


import java.util.List;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<FurnitureModel>modelList;
    private DatabaseHelper db;

    public CartAdapter(Context context, List<FurnitureModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        db= new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).
                inflate(R.layout.layout_cart_item, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder,  int position) {
        Glide.with(context).load(modelList.get(position).getImage()).into(holder.imageCart);
        holder.textFurnitureName.setText(modelList.get(position).getName());
        holder.textFurniturePrice.setText(String.valueOf(modelList.get(position).getPrice()));
        holder.numberButton.setNumber(String.valueOf(modelList.get(position).getQuantity()));

        holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                modelList.get(position).setQuantity(newValue);
                db.createItem(modelList.get(position));
            }
        });

       holder.setListener(new CartRecyclerClickListener() {
           @Override
           public void onItemClick(int pos) {
               Common.selectFurniture = modelList.get(pos);
           }
       });

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }



    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageCart;
        TextView textFurnitureName;
        TextView textFurniturePrice;
        ElegantNumberButton numberButton;
        CartRecyclerClickListener listener;
        public void setListener(CartRecyclerClickListener listener) {
            this.listener = listener;
        }

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCart = itemView.findViewById(R.id.img_cart);
            textFurnitureName = itemView.findViewById(R.id.text_Furniture_name);
            textFurniturePrice = itemView.findViewById(R.id.text_Furniture_price1);
            numberButton = itemView.findViewById(R.id.number_button);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
