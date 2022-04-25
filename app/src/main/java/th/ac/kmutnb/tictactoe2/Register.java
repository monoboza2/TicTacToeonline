package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Register extends AppCompatActivity {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    public static String Passen="";
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
                try {
                    Log.i("test",encrypt(Passtxt));
                    Passen=encrypt(Passtxt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                                databaseReference.child("Users").child(Usertxt).child("Password").setValue(Passen);
                                databaseReference.child("Users").child(Usertxt).child("Score").setValue(0);

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

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Register.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }
    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Register.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(Register.KEY.getBytes(),Register.ALGORITHM);
        return key;
    }
}