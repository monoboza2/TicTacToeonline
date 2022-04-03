package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText Username=findViewById(R.id.Username);
        EditText Password=findViewById(R.id.Password);
        Button LoginBtn=findViewById(R.id.Loginbtn);
        TextView Register=findViewById(R.id.Registertxt);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Usertxt=Username.getText().toString();
                String Passtxt=Password.getText().toString();

                if(Usertxt.isEmpty()||Passtxt.isEmpty()){
                    Toast.makeText(getApplication(),"Please enter Username or Password",Toast.LENGTH_LONG).show();
                    Log.i(TAG,"KUY");
                }
                else{
                    Log.i(TAG,"KUY2");
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(Usertxt)){
                                String getpass=snapshot.child(Usertxt).child("Password").getValue(String.class);

                                if(getpass.equals(Passtxt)){
                                    Toast.makeText(getApplication(), "Login Success", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Login.this,MainActivity.class));
                                    finish();
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