package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-6a7e1-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText Email=findViewById(R.id.Email);
        EditText User=findViewById(R.id.Regisuser);
        EditText Password=findViewById(R.id.Regispass);
        EditText Conpass=findViewById(R.id.Conpass);
        Button Regisbtn=findViewById(R.id.Regisbtn);

        Regisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Emailtxt=Email.getText().toString();
                String Usertxt=User.getText().toString();
                String Passtxt=Password.getText().toString();
                String Contxt=Conpass.getText().toString();

                if(Emailtxt.isEmpty()||Usertxt.isEmpty()||Passtxt.isEmpty()||Contxt.isEmpty()){
                    Toast.makeText(getApplication(), "Please fill data", Toast.LENGTH_LONG).show();
                }

                else if(!Passtxt.equals(Contxt)){
                    Toast.makeText(getApplication(), "Password not matching", Toast.LENGTH_LONG).show();
                }

                else{
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(Usertxt)){
                                Toast.makeText(getApplication(), "Username have already", Toast.LENGTH_LONG).show();
                            }
                            else{
                                databaseReference.child("Users").child(Usertxt).child("Email").setValue(Emailtxt);
                                databaseReference.child("Users").child(Usertxt).child("Password").setValue(Passtxt);

                                Toast.makeText(getApplication(), "Success", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }
}