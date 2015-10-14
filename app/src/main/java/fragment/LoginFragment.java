package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import callback.CustomCallback;
import anjithsasindran.chatpubnub.R;

/**
 * Created by Anjith Sasindran
 * on 11-Oct-15.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    Button loginButton;
    EditText usernameEdit;
    SharedPreferences sharedPreferences;
    Context context;
    CustomCallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        usernameEdit = (EditText) view.findViewById(R.id.username);
        loginButton = (Button) view.findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        context = getActivity();
        callback = (CustomCallback) context;
        sharedPreferences = context.getSharedPreferences("details", Context.MODE_PRIVATE);

        getActivity().setTitle("Login");

        return view;
    }

    @Override
    public void onClick(View view) {
        String username = usernameEdit.getText().toString().trim();
        if (loginButton.getId() == view.getId()) {
            if (username.length() != 0) {
                callback.loginActivity(0);
                sharedPreferences.edit().putString("username", username).apply();
            } else {
                Toast.makeText(context, "Enter username", Toast.LENGTH_SHORT).show();
            }
        }
    }
}