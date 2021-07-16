package com.example.final_project_android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_android.R;
import com.example.final_project_android.models.UserModel;
import com.example.final_project_android.util.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private static int APP_REQUEST_CODE = 7171;
    private FirebaseAuth firebaseAuth; //هاد كلاس من الفير بيز لتسجيل الدخول وكلها كلاسات جاهزة بس بنستدعيها
    private FirebaseAuth.AuthStateListener listener;//هاد عشان اتحكم بالتحقق
    private AlertDialog dialog;
    private DatabaseReference databaseReference;
    private List<AuthUI.IdpConfig> providers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        //FirebaseDatabase هان بعمل انستنس من كلاس
        //class common حطيت فيه المتغيرات الي بستخدمهم اكتر من مرة
        databaseReference = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        // cloudFunctions = RetrofitClient.getInstance().create(ICloudFunctions.class);


        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            //هاي الدالة بتخرن رقم اي يوزر بدخل
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { // هاي بتفحص ازا الرقم موجود او لا
                    //already login
                    MainActivity.this.checkUserFromFirebase(user);
                } else {
                    //لو غير موجود
                    MainActivity.this.phoneLogin();
                }
            }


        };
    }


    private void checkUserFromFirebase(FirebaseUser user) {
        dialog.show(); //هاد dialog تبع انتظار انتهاء التحميل
        databaseReference.child(user.getUid())// هان ببعت child ل (الاب)databaseReference وchild هاد هو اليوزر بستقبله
                .addListenerForSingleValueEvent(new ValueEventListener() {//هاي بتلف عالعناصر عندي
                    @Override
                    // هاد snapshot اوبجكت جاهز بيجي بفحصلي ازا انا مسجل دخول او لا
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(MainActivity.this, "You already registered", Toast.LENGTH_SHORT).show();
                            //userModel هان انا بحددله كيف شكل البيانات الي جايه من
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            //لو تمت العملية
                            goToHomeActivity(userModel);
                        } else {
                            //لو ما كان مسجل قبل هيك
                            showRegisterDialog(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();

                        // Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void showRegisterDialog(FirebaseUser user) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage("please fill information");

        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);

        EditText editName = itemView.findViewById(R.id.edit_name);
        EditText editAddress = itemView.findViewById(R.id.edit_Address);
        EditText editPhone = itemView.findViewById(R.id.edit_phone);

        editPhone.setText(user.getPhoneNumber());

        builder.setView(itemView);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(editName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please Enter your name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editAddress.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please Enter your Address", Toast.LENGTH_SHORT).show();
                } else {
                    UserModel userModel = new UserModel();
                    userModel.setUid(user.getUid());
                    userModel.setName(editName.getText().toString());
                    userModel.setAddress(editAddress.getText().toString());
                    userModel.setPhone(editPhone.getText().toString());
                    databaseReference.child(user.getUid()).setValue(userModel)
                            //لو زبط التسجيل او تمت العملية
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Regester Success", Toast.LENGTH_SHORT).show();
                                        goToHomeActivity(userModel); //وفيها بياخد الاوبجكت وبخزنه ف currunt user وحولو ع HomeActivity

                                    }

                                }
                            });
                }
            }
        });

        builder.setView(itemView);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToHomeActivity(UserModel userModel) {

        Common.currentUser = userModel; //  هاد currentUser متغير عام واوبجكت من UserModel

        //startActivity
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    private void phoneLogin() { //هاي أول شاشة للمستخدم الجديد
       //هان بعمل Instance من AuthUI وبعمل الها(للواجهة) Build
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()
                ,APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Failed sign in!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }
}