package com.example.final_project_android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.final_project_android.R;
import com.example.final_project_android.adapter.FurnitureListAdapter;
import com.example.final_project_android.models.FurnitureModel;
import com.example.final_project_android.util.Common;

import java.util.ArrayList;
import java.util.List;


public class FurnitureListFragment extends Fragment {

    private RecyclerView recycler_furniture;
    private FurnitureListAdapter adapter;
    private List<FurnitureModel> furnitureModels;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        container.removeAllViews();
        recycler_furniture = root.findViewById(R.id.recycler_furniture_list);
        ((AppCompatActivity) getActivity())
                //هان بروح ع appbar وبجيب اسم العنصر الي ضغطت عليه
                //Commonاستخدمته هان لاني عناصر categiry خزنتهم فمتغير عم
                .getSupportActionBar()
                .setTitle(Common.categorySelected.getName());
        recycler_furniture.setHasFixedSize(true);
        recycler_furniture.setLayoutManager(new LinearLayoutManager(getContext()));
        furnitureModels = new ArrayList<>();
        // هان من categorySelected هاتلي الليست الي اسمها furnitureModels وبخزنها
        // ف لسيت عاديه وببتعها للادبتر

        furnitureModels.addAll(Common.categorySelected.getFurniture());
        adapter = new FurnitureListAdapter(getContext(),furnitureModels);
        recycler_furniture.setAdapter(adapter);
        return root;
    }
}