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

        databaseReference.child("connections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!opponentfound){
                    Log.i("hello","snapshot");

                    //if opponent found or not ?:if not then look for the opponent
                    if(snapshot.hasChildren()){
                        //check all connection
                        for(DataSnapshot connections : snapshot.getChildren()){

                            //get connection id
                            String conId = connections.getKey();

                            //2 players or 1 player ?
                            int getPlayerCount = (int)connections.getChildrenCount();

                            //after created new connection and waiting
                            if(status.equals("waiting")){
                                Log.i("hello","waiting");
                                if(getPlayerCount == 2){
                                    Playerturn = PlayeruniqueID;
//                                    applyPlayerturn(Playerturn);
                                    startActivity(new Intent(MultiplayerActivity.this,Test.class));

                                    //true when found
                                    boolean playerFound = false ;

                                    //getting players connection
                                    for(DataSnapshot players : connections.getChildren()){
                                        String getPlayerUniqueId = players.getKey();

                                        //if player id match with user created connection
                                        if(getPlayerUniqueId.equals(PlayeruniqueID)){
                                            playerFound = true;
                                        }
                                        else if(playerFound){
                                            String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                            OpponentuniqueID = players.getKey();

                                            //set playername to TextView
//                                            player2TV.setText(getOpponentPlayerName);

                                            //assigning connection id
                                            ConnectionID = conId;
                                            opponentfound = true;

                                            //adding turn and won listener to database
                                            databaseReference.child("turns").child(ConnectionID).addValueEventListener(turnEventListener);
                                            databaseReference.child("won").child(ConnectionID).addValueEventListener(wonEventListener);

                                            //hide progress
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                            //remove connection database
                                            databaseReference.child("connections").removeEventListener(this);
                                        }
                                    }
                                }
                            }
                            // case user not created connection
                            else {
                                Log.i("hello","case user not created connection");
                                //if connection has 1 player
                                if(getPlayerCount == 1){
                                    Log.i("hello","player == 1");
                                    //add player to connection
                                    connections.child(PlayeruniqueID).child("player_name").getRef().setValue(name);

                                    //getting both players
                                    for(DataSnapshot players : connections.getChildren()){

                                        String getOpponentName = players.child("player_name").getValue(String.class);
                                        OpponentuniqueID = players.getKey();

                                        //first turn will be to who created connection
                                        Playerturn = OpponentuniqueID;
//                                        applyPlayerturn(Playerturn);

                                        //set playername to TextView
//                                            player2TV.setText(getOpponentPlayerName);

                                        //assign connection id
                                        ConnectionID = conId;
                                        opponentfound = true;

                                        //adding turn and won listener to database
                                        databaseReference.child("turns").child(ConnectionID).addValueEventListener(turnEventListener);
                                        databaseReference.child("won").child(ConnectionID).addValueEventListener(wonEventListener);

                                        //hide progress
                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }

                                        //remove connection database
                                        databaseReference.child("connections").removeEventListener(this);

                                        break;
                                    }
                                }
                            }
                        }
                        // if opponent is not found and user is not waiting then create new connection
                        if(!opponentfound && !status.equals("waiting")){
                            Log.i("hello","!opponent && !waiting");
                            //generate id connection
                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());

                            //add first player to connection and waiting
                            snapshot.child(connectionUniqueId).child(PlayeruniqueID).child("player_name").getRef().setValue(name);

                            status="waiting";
                        }
                    }
                    //if no connection on database : create new connection
                    else{
                        Log.i("hello","!snapshot");
                        //generate id connection
                        String connectionUniqueId = String.valueOf(System.currentTimeMillis());

                        //add first player to connection and waiting
                        snapshot.child(connectionUniqueId).child(PlayeruniqueID).child("player_name").getRef().setValue(name);

                        status="waiting";
                    }
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