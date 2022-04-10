package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private static final String TAG = "my_app";
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-6a7e1-default-rtdb.firebaseio.com/");
    private static final String Shared_Name="mypref";
    private static final String KEY_NAME="name";
    private static final String KEY_PASS="pass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText Username=findViewById(R.id.Username);
        EditText Password=findViewById(R.id.Password);
        Button LoginBtn=findViewById(R.id.Loginbtn);
        TextView Register=findViewById(R.id.Registertxt);

        SharedPreferences sharedPreferences=getSharedPreferences(Shared_Name,MODE_PRIVATE);
        String name =sharedPreferences.getString(KEY_NAME,null);

        if(name != null){
            startActivity(new Intent(Login.this,MainActivity.class));
        }


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Usertxt=Username.getText().toString();
                String Passtxt=Password.getText().toString();

                if(Usertxt.isEmpty()||Passtxt.isEmpty()){
                    Toast.makeText(getApplication(),"Please enter Username or Password",Toast.LENGTH_LONG).show();
                    Log.i(TAG,"Test0");
                }
                else{
                    Log.i(TAG,"Test1");
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(Usertxt)){
                                String getpass=snapshot.child(Usertxt).child("Password").getValue(String.class);

                                if(getpass.equals(Passtxt)){
                                    Toast.makeText(getApplication(), "Login Success", Toast.LENGTH_LONG).show();

                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString(KEY_NAME,Usertxt);
                                    editor.putString(KEY_PASS,Passtxt);
                                    editor.apply();

                                    startActivity(new Intent(Login.this,MainActivity.class));
                                }
                                else{
                                    Toast.makeText(getApplication(), "Wrong Password", Toast.LENGTH_LONG).show();
                                }

                            }
                            else{
                                Toast.makeText(getApplication(), "Wrong Username", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
    }
}