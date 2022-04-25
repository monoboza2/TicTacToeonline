package th.ac.kmutnb.tictactoe2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionViewAdapter> {
    List<ConnectionData> list;
    Context context;

    public ConnectionAdapter(List<ConnectionData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ConnectionViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.connection_list,parent,false);
        return new ConnectionViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionViewAdapter holder, int position) {
        ConnectionData currentItem = list.get(position);
        holder.nameRoom.setText("Room: "+currentItem.getNameRoom());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ConnectionViewAdapter extends RecyclerView.ViewHolder {
        TextView nameRoom;
        public ConnectionViewAdapter(@NonNull View itemView) {
            super(itemView);
            nameRoom = itemView.findViewById(R.id.nameRoom);
        }
    }
}
