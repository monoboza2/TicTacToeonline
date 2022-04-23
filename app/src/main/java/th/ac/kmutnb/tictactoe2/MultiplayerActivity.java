package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultiplayerActivity extends AppCompatActivity {
    private static final String TAG = "my_app";
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-6a7e1-default-rtdb.firebaseio.com/");
    private static final String Shared_Name="mypref";
    private static final String KEY_NAME="name";

    private String PlayeruniqueID="0";
    private String OpponentuniqueID="0";
    private boolean opponentfound=false;
    private String status="matching";
    private String Playerturn="";
    private String ConnectionID="";

    ValueEventListener turnEventListener,wonEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_Name,MODE_PRIVATE);
        String name =sharedPreferences.getString(KEY_NAME,null);

//        PlayeruniqueID=String.valueOf(System.currentTimeMillis());

//        databaseReference.child("conections").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(opponentfound){
//
//                    if(snapshot.hasChildren()){
//
//                        for(DataSnapshot conections:snapshot.getChildren()){
//                            long conId=Long.parseLong(conections.getKey());
//                            int getplayerCount= (int)conections.getChildrenCount();
//
//                            if(status.equals("waiting")){
//                                if(getplayerCount==2){
//                                    Playerturn=PlayeruniqueID;
//                                    startActivity(new Intent(MultiplayerActivity.this,Test.class));
////                                    applyPlayerturn( Playerturn);
//
//                                }
//                            }
//                        }
//                    }
//                }
//                else{
//                    String connectionUniqueID=String.valueOf(System.currentTimeMillis());
//
//                    snapshot.child(connectionUniqueID).child(PlayeruniqueID).child("player_name").getRef().setValue(name);
//
//                    status="waiting";
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void Online(View view){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting for Opponent");
        progressDialog.show();

        PlayeruniqueID=String.valueOf(System.currentTimeMillis());
        SharedPreferences sharedPreferences=getSharedPreferences(Shared_Name,MODE_PRIVATE);
        String name =sharedPreferences.getString(KEY_NAME,null);

        databaseReference.child("conections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(opponentfound){

                    if(snapshot.hasChildren()){

                        for(DataSnapshot conections:snapshot.getChildren()){
                            String conId=conections.getKey();
                            int getplayerCount= (int)conections.getChildrenCount();

                            if(status.equals("waiting")){
                                if(getplayerCount==2){
                                    Playerturn=PlayeruniqueID;
                                    startActivity(new Intent(MultiplayerActivity.this,Test.class));
//                                    applyPlayerturn( Playerturn);

                                    boolean Playerfound =false;

                                    for(DataSnapshot players:conections.getChildren()){
                                        String getPlayeruniqueID =players.getKey();

                                        if(getPlayeruniqueID.equals(PlayeruniqueID)){
                                            Playerfound=true;
                                        }
                                        else if(Playerfound){
                                            String getOpponentPlayername=players.child("player_name").getValue(String.class);
                                            OpponentuniqueID=players.getKey();

                                            ConnectionID=conId;
                                            opponentfound=true;

                                            databaseReference.child("turns").child(ConnectionID).addValueEventListener(turnEventListener);
                                            databaseReference.child("won").child(ConnectionID).addValueEventListener(wonEventListener);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            databaseReference.child("conections").removeEventListener(this);

                                            break;
                                        }
                                    }
                                }
                                //check found or not
                                if(!opponentfound && status.equals("waiting")){

                                    String connectionUniqueID=String.valueOf(System.currentTimeMillis());

                                    snapshot.child(connectionUniqueID).child(PlayeruniqueID).child("player_name").getRef().setValue(name);

                                    status="waiting";

                                }
                            }
                            else{

                                if(getplayerCount==1){
                                    conections.child(PlayeruniqueID).child("player_name").getRef().setValue(name);

                                    for(DataSnapshot players : conections.getChildren()){

                                        String getOpponentPlayername=players.child("player_name").getValue(String.class);
                                        OpponentuniqueID=players.getKey();

                                        Playerturn= OpponentuniqueID;

//                                        applyPlayerturn();

                                        /// settext

                                        ConnectionID=conId;
                                        opponentfound=true;
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    String connectionUniqueID=String.valueOf(System.currentTimeMillis());

                    snapshot.child(connectionUniqueID).child(PlayeruniqueID).child("player_name").getRef().setValue(name);

                    status="waiting";
                    int i=0;
                    while(true){
                        if(i==50000){
                            Log.i(TAG, String.valueOf(i));
                            break;
                        }
                        Log.i(TAG, String.valueOf(i));
                        i++;
                    }
                    Toast.makeText(getApplication(), "Player not Found", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MultiplayerActivity.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turnEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getChildrenCount()==2){
                        final int getBoxPosition=Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));
                        final String getPlayerId=dataSnapshot.child("player_id").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

    }

    private void applyPlayerturn(String PlayeruniqueID2){

        if(PlayeruniqueID2.equals(PlayeruniqueID)){

        }
    }

}