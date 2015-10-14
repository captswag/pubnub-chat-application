package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import pojo.Message;
import anjithsasindran.chatpubnub.R;

/**
 * Created by Anjith Sasindran
 * on 11-Oct-15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    ArrayList<String> chatMessageList;
    Gson gson = new Gson();
    String message;
    String username;
    String myUsername;
    Context context;

    public ChatAdapter(ArrayList<String> chatMessageList, String myUsername) {
        this.chatMessageList = chatMessageList;
        this.myUsername = myUsername;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChatViewHolder(LayoutInflater.from(context).inflate
                (R.layout.chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        message = chatMessageList.get(position);
        Message messageObject = gson.fromJson(message, Message.class);

        username = messageObject.getUsername();
        holder.username.setText(messageObject.getUsername());
        if (!(username.equals(myUsername))) {
            holder.chatHolder.setBackgroundColor(context.getResources().getColor(R.color.blue_400));
        }
        holder.message.setText(messageObject.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}
