package in.theayans.ayans;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {

    private Button loginbtn;

    private DatabaseReference mdatabaselogin;

    public EditText mdisid;
    public EditText mpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mdisid = (EditText) findViewById(R.id.disid);

        mpassword = (EditText) findViewById(R.id.password);

        loginbtn = (Button) findViewById(R.id.login);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String disid = mdisid.getText().toString().trim();
                final String password = mpassword.getText().toString().trim();

                if(disid.isEmpty() && password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Distributor ID or Password is Empty",Toast.LENGTH_SHORT).show();
                }
                else {

                    final ProgressDialog progressDialog = new ProgressDialog(login.this);
                    progressDialog.setMessage("loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    mdatabaselogin = FirebaseDatabase.getInstance().getReference().child("users");

                    mdatabaselogin.child(disid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserLoginHelper user = dataSnapshot.getValue(UserLoginHelper.class);

                            String pass = user.password;

                            MessageDigest md = null;
                            try {
                                md = MessageDigest.getInstance("MD5");
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            md.update(password.getBytes(), 0, password.length());
                            String ency = new BigInteger(1, md.digest()).toString(16);

                            if (pass.equals(ency)) {
                                //Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                calldashboard();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Password is Incorrect", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), "Invalid Distributor Id", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void calldashboard(){
        Intent dashboard = new Intent(this,MainActivity.class);
        startActivity(dashboard);
    }


    public void register(View v) throws InterruptedException {
        Intent register = new Intent(this,register.class);
        startActivity(register);
    }
}
