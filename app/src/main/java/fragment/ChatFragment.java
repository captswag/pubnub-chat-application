package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.ChatAdapter;
import callback.CustomCallback;
import keys.PubnubKeys;
import pojo.Message;
import anjithsasindran.chatpubnub.R;

/**
 * Created by Anjith Sasindran
 * on 11-Oct-15.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {

    String TAG = "ChatFragment";
    SharedPreferences sharedPreferences;
    CustomCallback callback;
    Context context;
    Pubnub pubnub;
    EditText chatMessage;
    Button send;
    RecyclerView chatList;
    ArrayList<String> chatMessageList;
    ChatAdapter chatAdapter;
    Gson gson;
    JSONObject messageObject;
    String username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        setHasOptionsMenu(true);

        context = getActivity();
        callback = (CustomCallback) context;
        sharedPreferences = context.getSharedPreferences("details", Context.MODE_PRIVATE);
        gson = new Gson();

        username = sharedPreferences.getString("username", null);
        getActivity().setTitle(username);

        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList, username);

        chatList = (RecyclerView) view.findViewById(R.id.chatlist);
        chatList.setLayoutManager(new LinearLayoutManager(context));
        chatList.setAdapter(chatAdapter);

        chatMessage = (EditText) view.findViewById(R.id.message);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(this);

        pubnub = new Pubnub(PubnubKeys.PUBLISH_KEY, PubnubKeys.SUBSCRIBE_KEY);

        try {
            pubnub.subscribe(PubnubKeys.CHANNEL_NAME, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);
                    chatMessageList.add(message.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                            chatList.scrollToPosition(chatMessageList.size() - 1);
                        }
                    });
                    Log.d("successCallback", "message " + message);
                }

                @Override
                public void successCallback(String channel, Object message, String timetoken) {
                    super.successCallback(channel, message, timetoken);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    super.errorCallback(channel, error);
                    Log.d("errorCallback", "error " + error);
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    super.connectCallback(channel, message);
                    Log.d("connectCallback", "message " + message);
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    super.reconnectCallback(channel, message);
                    Log.d("reconnectCallback", "message " + message);
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    super.disconnectCallback(channel, message);
                    Log.d("disconnectCallback", "message " + message);
                }
            });
        } catch (PubnubException pe) {
            Log.d(TAG, pe.toString());
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            callback.loginActivity(1);
            sharedPreferences.edit().remove("username").apply();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (send.getId() == view.getId()) {
            String message = chatMessage.getText().toString().trim();
            if (message.length() != 0) {
                message = gson.toJson(new Message(username, message));
                try {
                    messageObject = new JSONObject(message);
                } catch (JSONException je) {
                    Log.d(TAG, je.toString());
                }
                chatMessage.setText("");
                pubnub.publish(PubnubKeys.CHANNEL_NAME, messageObject, new Callback() {
                    @Override
                    public void successCallback(String channel, Object message) {
                        super.successCallback(channel, message);
                        Log.d("successCallback", "message " + message);
                    }

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        Log.d("errorCallback", "error " + error);
                    }
                });
            } else {
                Toast.makeText(context, "Please enter message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pubnub.unsubscribe(PubnubKeys.CHANNEL_NAME);
        Log.d(TAG, "Un subscribed");
    }
}