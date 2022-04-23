package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerActivity extends AppCompatActivity {
    private static final String TAG = "my_app";
    public static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-6a7e1-default-rtdb.firebaseio.com/");
    private static final String Shared_Name="mypref";
    private static final String KEY_NAME="name";

    public static String PlayeruniqueID="0";
    public static String OpponentuniqueID="0";
    public static boolean opponentfound=false;
    private String status="matching";
    public static String Playerturn = "";
    public static String ConnectionID="";

    //Winning
    public static final List<int[]> combinationList = new ArrayList<>();
    public static final List<String> doneBoxes = new ArrayList<>();

    ValueEventListener turnEventListener,wonEventListener;

    //selected boxes by players
    private final String[] boxSelectedBy = {"","","","","","","","",""};

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
                if(!opponentfound){

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
//                    int i=0;
//                    while(true){
//                        if(i==50000){
//                            Log.i(TAG, String.valueOf(i));
//                            break;
//                        }
//                        Log.i(TAG, String.valueOf(i));
//                        i++;
//                    }
//                    Toast.makeText(getApplication(), "Player not Found", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(MultiplayerActivity.this,MainActivity.class));
//                    finish();
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

                        //getting box position selected by user
                        final int getBoxPosition=Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));

                        //getting player id selected the box
                        final String getPlayerId=dataSnapshot.child("player_id").getValue(String.class);

                        //if user has not selected
                        if(!doneBoxes.contains(String.valueOf(getBoxPosition))){
                            //select box
                            doneBoxes.add(String.valueOf(getBoxPosition));
                            switch (getBoxPosition){
                                case 1:
                                    selectBox(GameboardAdapter.arrBms.get(0),getBoxPosition,getPlayerId);
                                    break;
                                case 2:
                                    selectBox(GameboardAdapter.arrBms.get(1),getBoxPosition,getPlayerId);
                                    break;
                                case 3:
                                    selectBox(GameboardAdapter.arrBms.get(2),getBoxPosition,getPlayerId);
                                    break;
                                case 4:
                                    selectBox(GameboardAdapter.arrBms.get(3),getBoxPosition,getPlayerId);
                                    break;
                                case 5:
                                    selectBox(GameboardAdapter.arrBms.get(4),getBoxPosition,getPlayerId);
                                    break;
                                case 6:
                                    selectBox(GameboardAdapter.arrBms.get(5),getBoxPosition,getPlayerId);
                                    break;
                                case 7:
                                    selectBox(GameboardAdapter.arrBms.get(6),getBoxPosition,getPlayerId);
                                    break;
                                case 8:
                                    selectBox(GameboardAdapter.arrBms.get(7),getBoxPosition,getPlayerId);
                                    break;
                                case 9:
                                    selectBox(GameboardAdapter.arrBms.get(8),getBoxPosition,getPlayerId);
                                    break;
                                default:;
                            }
                        }
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

                //if user won
                if(snapshot.hasChild("player_id")){
                    String getWinPlayerId = snapshot.child("player_id").getValue(String.class);
                    final WinDialog winDialog;
                    if(getWinPlayerId.equals(PlayeruniqueID)){

                        //show win
                        winDialog = new WinDialog(MultiplayerActivity.this,"You won");
                    }
                    else{

                        winDialog = new WinDialog(MultiplayerActivity.this,"Opponent won");
                    }
                    winDialog.setCancelable(false);
                    winDialog.show();

                    //remove listener
                    databaseReference.child("turns").child(ConnectionID).removeEventListener(turnEventListener);
                    databaseReference.child("won").child(ConnectionID).removeEventListener(wonEventListener);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void applyPlayerturn(String PlayeruniqueID2){
        TextView textViewTurn = findViewById(R.id.textTurn);
        if(PlayeruniqueID2.equals(PlayeruniqueID)){
            textViewTurn.setText("Turn A");
        }
        else{
            textViewTurn.setText("Turn B");
        }
    }

    private void selectBox(Bitmap imageCell, int selectedBoxPosition , String selectedByPlayer ){
        boxSelectedBy[selectedBoxPosition - 1] = selectedByPlayer;

        if(selectedByPlayer.equals(PlayeruniqueID)){
            Playerturn = OpponentuniqueID;
        }
        else{
            Playerturn = PlayeruniqueID;
        }

        applyPlayerturn(Playerturn);

        //checking player has won?
        if(checkPlayerWin(selectedByPlayer)){

            //sending won player id to database
            databaseReference.child("won").child(ConnectionID).child("player_id").setValue(selectedByPlayer);
        }

        if(doneBoxes.size()==9){
            final WinDialog winDialog = new WinDialog(MultiplayerActivity.this,"Draw!");
            winDialog.show();
        }
    }
    private boolean checkPlayerWin(String playerId){
        boolean isPlayerWon = false ;

        //compare player turn every combination
        for(int i=0; i< combinationList.size();i++){
            final int[] comination = combinationList.get(i);

            //check last three turn of user
            if(
                    boxSelectedBy[comination[0]].equals(playerId) &&
                    boxSelectedBy[comination[1]].equals(playerId) &&
                    boxSelectedBy[comination[2]].equals(playerId)
            ){
                isPlayerWon = true ;
            }
        }
        return isPlayerWon;
    }

}