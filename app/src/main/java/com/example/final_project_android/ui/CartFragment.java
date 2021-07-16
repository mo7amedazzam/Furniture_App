package com.example.final_project_android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_android.R;
import com.example.final_project_android.adapter.CartAdapter;
import com.example.final_project_android.database.DatabaseHelper;
import com.example.final_project_android.models.FurnitureModel;
import com.example.final_project_android.models.Order;
import com.example.final_project_android.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private RecyclerView recyclerCart;
    private TextView textTotal;
    private TextView textEmptyCart;
    private CardView cardView;
    private MaterialButton buttonPlaceOrder;
    private CartAdapter cartAdapter;
    private List<FurnitureModel> list = new ArrayList<>();
    private DatabaseHelper db;
    private String address;
    double finalTotal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_slideshow, container, false);
        container.removeAllViews();

        recyclerCart = itemView.findViewById(R.id.recycler_cart);
        textTotal = itemView.findViewById(R.id.text_total_price);
        textEmptyCart = itemView.findViewById(R.id.text_empty_cart);
        cardView = itemView.findViewById(R.id.group_place_holder);
        buttonPlaceOrder = itemView.findViewById(R.id.btn_place_order);

        db = new DatabaseHelper(getContext());
        list = db.getAllFurniture();

        if (list == null || list.isEmpty()) {
            recyclerCart.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            textEmptyCart.setVisibility(View.VISIBLE);
        } else {
            recyclerCart.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            textEmptyCart.setVisibility(View.GONE);
            cartAdapter = new CartAdapter(getContext(), list);
            recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerCart.setAdapter(cartAdapter);
            calculateTotalPrice();


        }

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderClick();
            }
        });

        return itemView;
    }


    private void onPlaceOrderClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_place_order, null);
        EditText editAddress = itemView.findViewById(R.id.edit_address1);
        RadioButton rdi_home = itemView.findViewById(R.id.rdi_home_address);
        RadioButton rdi_other = itemView.findViewById(R.id.rdi_other_address);
        RadioButton rdi_cod = itemView.findViewById(R.id.rdi_cod);

        Common.currentUser.getAddress();

        rdi_home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    editAddress.setText(Common.currentUser.getAddress());
                }
            }
        });

        rdi_other.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                editAddress.setText(" "); //clear
                editAddress.setHint("Enter your address");

            }
        });

        builder.setView(itemView);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (rdi_cod.isChecked()) {
                    address = editAddress.getText().toString();
                    Order order = new Order();
                    order.setUserId(Common.currentUser.getUid());
                    order.setUserName(Common.currentUser.getName());
                    order.setUserPhone(Common.currentUser.getPhone());
                    order.setShippingAddress(address);
                    order.setFinalPayment(finalTotal);
                    order.setTransactionId("Cash On Delivery");
                    submitOrderToFirebase(order);

                } else {

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Reload() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(CartFragment.this.getId(), new CartFragment()).commit();
    }

    private void submitOrderToFirebase(Order order) {
        FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                .child(Common.createOrderNumber())//هاي بتعمل ارقام عشوائية ل orders
                .setValue(order)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Place Order successfully!", Toast.LENGTH_SHORT).show();
                db.deleteAll();
                Reload();

            }
        });

    }

    private void calculateTotalPrice() {

        finalTotal = db.getAmountFull();
        textTotal.setText(new StringBuilder("").append(finalTotal).append("$"));

    }


    @Override
    public void onResume() {
        super.onResume();
        calculateTotalPrice();

    }

    @Override
    public void onStart() {
        super.onStart();
        calculateTotalPrice();

    }

}

