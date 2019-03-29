package com.example.gaurav.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.ecommerce.Model.Users;
import com.example.gaurav.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingbar;
    private String ParentDbName="Users";
    private CheckBox chkBoxRememberMe;
    private TextView AdminLink,NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton=(Button) findViewById(R.id.login_btn);
        InputPhoneNumber=(EditText)findViewById(R.id.login_phone_number_input);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        loadingbar =new ProgressDialog(this);
         chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("c","hc1");
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                ParentDbName="Admins";
                Log.d("c","hc");

            }

        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                ParentDbName="Users";
            }
        });
    }

    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

         if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
         {
             loadingbar.setTitle("Login Account");
             loadingbar.setMessage("Please wait,while we are checking the credentials.");
             loadingbar.setCanceledOnTouchOutside(false);
             loadingbar.show();

             AllowAccessToAccount(phone,password);

         }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
               if(chkBoxRememberMe.isChecked())
               {
                   Paper.book().write(Prevalent.UserPhoneKey,phone);
                   Paper.book().write(Prevalent.UserPasswordKey,password);
               }

        final DatabaseReference Rootref;

        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(ParentDbName).child(phone).exists())
                {
                    Users userData=dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                           if(ParentDbName.equals("Admins"))
                           {
                               Log.d("Chocho","chuchu");
                               Toast.makeText(loginActivity.this, "Welcome Admin,you are logged in successfully.", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();

                               Intent intent = new Intent(loginActivity.this, AdminCategoryActivity.class);
                               startActivity(intent);
                           }
                           else if (ParentDbName.equals("Users"))
                           {
                               Toast.makeText(loginActivity.this, "Logged in successfully......", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();

                              Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                              Prevalent.currentonlineUsers=userData;
                               startActivity(intent);
                           }
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(loginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else
                {
                    Toast.makeText(loginActivity.this, "Account with this "+phone+" number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    //Toast.makeText(loginActivity.this, "You need to create a new account.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
