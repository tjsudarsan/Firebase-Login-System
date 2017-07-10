package in.theayans.ayans;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class register extends AppCompatActivity {

    private Button register;

    private DatabaseReference mdatabasereg;

    private int i=1;

    private EditText mdid;
    private EditText mname;
    private EditText mpassword;
    private EditText mrpassword;
    private EditText memail;
    private EditText mphno;
    private EditText mupline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

            register  = (Button) findViewById(R.id.register);

            mdid = (EditText) findViewById(R.id.did);
            mname = (EditText) findViewById(R.id.name);
            mpassword = (EditText) findViewById(R.id.passwd);
            mrpassword = (EditText) findViewById(R.id.repasswd);
            memail = (EditText) findViewById(R.id.email);
            mphno = (EditText) findViewById(R.id.phno);
            mupline = (EditText) findViewById(R.id.upline);

            mdid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(mdid.getText().length()<8) {
                        mdid.setError("Invalid Distributor ID");
                    }
                }
            });

            mname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(mname.getText().toString().isEmpty()) {
                        mname.setError("Enter Name");
                    }
                }
            });

            mpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(mpassword.getText().toString().isEmpty()) {
                        mpassword.setError("Enter Enter Password");
                    }
                }
            });

            mrpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(mrpassword.getText().toString().isEmpty()) {
                        mrpassword.setError("Enter Repeat Password");
                    }
                }
            });

            memail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(memail.getText().toString().isEmpty()) {
                        memail.setError("Enter Email");
                    }
                }
            });

            mphno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(mphno.getText().length()<10) {
                        mphno.setError("Invalid Mobile Number");
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                                //1. Create child in root object
                                //2. Assign some value to the child object

                                String disid = mdid.getText().toString().trim();
                                String name = mname.getText().toString().trim();
                                String password = mpassword.getText().toString().trim();
                                String rpassword = mrpassword.getText().toString().trim();
                                String email = memail.getText().toString().trim();
                                String phno = mphno.getText().toString().trim();
                                String upline = mupline.getText().toString().trim();

                                if(disid.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Distributor ID cannot be blank",Toast.LENGTH_SHORT).show();
                                } else if(name.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Name cannot be blank",Toast.LENGTH_SHORT).show();
                                } else if(password.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Password cannot be blank",Toast.LENGTH_SHORT).show();
                                } else if(rpassword.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Retype Password cannot be blank",Toast.LENGTH_SHORT).show();
                                } else if(email.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Email cannot be blank",Toast.LENGTH_SHORT).show();
                                } else if(phno.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Mobile Number cannot be blank",Toast.LENGTH_SHORT).show();
                                } else if (password.equals(rpassword)) {

                                    final ProgressDialog progressDialog = new ProgressDialog(register.this);
                                    progressDialog.setMessage("Loading...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    //Encryption of Password
                                    MessageDigest md = null;
                                    try {
                                        md = MessageDigest.getInstance("MD5");
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                    md.update(password.getBytes(),0,password.length());
                                    String encrypted = new BigInteger(1, md.digest()).toString(16);


                                    HashMap<String, String> datamap = new HashMap<String, String>();

                                    datamap.put("username", name);
                                    datamap.put("password", encrypted);
                                    datamap.put("email", email);
                                    datamap.put("phno", phno);
                                    datamap.put("upline", upline);


                                    mdatabasereg = FirebaseDatabase.getInstance().getReference().child("users");

                                    mdatabasereg.child(disid).setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                new AlertDialog.Builder(register.this)
                                                        .setTitle("Registration Successfull")
                                                        .setMessage("Kindly login to Continue")
                                                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                progressDialog.cancel();
                                                                Intent login = new Intent(register.this, login.class);
                                                                startActivity(login);

                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            } else {

                                                new AlertDialog.Builder(register.this)
                                                        .setTitle("Registration Failed")
                                                        .setMessage("Kindly Check Your Internet Connection or Try after Sometime")
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                progressDialog.cancel();
                                                                Intent login = new Intent(register.this, login.class);
                                                                startActivity(login);
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }
                                        }
                                    });
                                } else {
                                    new AlertDialog.Builder(register.this)
                                            .setTitle("Password Not Matched")
                                            .setMessage("Kindly Retype Password")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mpassword.setText("");
                                                    mrpassword.setText("");

                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }


                    }
            });
    }
}
