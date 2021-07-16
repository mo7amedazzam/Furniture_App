package com.example.final_project_android.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_android.R;
import com.example.final_project_android.adapter.CategoriesAdapter;
import com.example.final_project_android.models.CategoryModel;
import com.example.final_project_android.util.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class HomeFragment extends Fragment{
    private RecyclerView categoriesRecycler;
    private CategoriesAdapter adapter;
    private AlertDialog alertDialog;
    List<CategoryModel> categoryModelsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        container.removeAllViews();//هان بحشف كل الفراغمنت وبخلي اخر وحده تظهر بس
        categoriesRecycler = root.findViewById(R.id.recycler_category);
        //waiting
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        alertDialog.show();
        categoryModelsList= new ArrayList<>();

        //هان ببني list عالفير بيز واسمها category زي user
        DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF);
        categoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 //هان بمشي عالابناء الي بيجو من الفير بيز (category)
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //ونفس الي قبل هام بنحددله نوع الكلاس الي يجيب ع هواه العناصر
                    CategoryModel categoryModel =dataSnapshot.getValue(CategoryModel.class);
                    categoryModel.setMenu_id(dataSnapshot.getKey());//عشان اميز id ببعت الكي
                     //هاي categoryModelsList الي حبعتها عالادبتر ف بدي اخزن فيها البيانات من الفير
                     categoryModelsList.add(categoryModel);//
                    alertDialog.dismiss();
                    Log.d("Done",categoryModel.getName());
                }
//                categoryCallbackListener.onCategoryLoadSuccess(categoryModelsList);
                adapter = new CategoriesAdapter(getContext(),categoryModelsList);
                categoriesRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));//زي fixed في flutter
                categoriesRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // categoryCallbackListener.onCategoryLoadFailed(error.getMessage());
                Log.d("error", error.getMessage());
                alertDialog.dismiss();
            }
        });




        return root;
    }

}