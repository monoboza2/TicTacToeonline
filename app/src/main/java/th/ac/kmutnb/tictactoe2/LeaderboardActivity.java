package th.ac.kmutnb.tictactoe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    RecyclerView recyclerView;
//    ProgressBar progressBar;
    List<ScoreData> list;
    ScoreAdapter adapter;
    DatabaseReference databaseReference= MultiplayerActivity.databaseReference;
    public static String userName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        recyclerView=findViewById(R.id.Leaderboard_recycle);
//        progressBar=findViewById(R.id.Leaderboardprogress);
        list = new ArrayList();
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        databaseReference.child("Users").orderByChild("Score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    int limit = (int) snapshot.getChildrenCount();
                    int count = 0;
                    for(DataSnapshot userData : snapshot.getChildren()){
                        if(count > (limit - 6)){
                            userName = userData.getKey();
                            list.add(new ScoreData(userData.getKey(),userData.child("Score").getValue(Integer.class)));
                        }
                        count++;
                    }
                    adapter=new ScoreAdapter(list,LeaderboardActivity.this);
                    recyclerView.setAdapter(adapter);
                    databaseReference.child("Users").removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}