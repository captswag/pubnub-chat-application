package anjithsasindran.chatpubnub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import callback.CustomCallback;
import fragment.ChatFragment;
import fragment.LoginFragment;

public class MainActivity extends AppCompatActivity implements CustomCallback {

    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("details", MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeLogin();
    }

    public void changeLogin() {
        if (sharedPreferences.getString("username", null) == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new LoginFragment(), "Login");
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ChatFragment(), "Chat");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void loginActivity(int LOGIN_STATE) {
        if (LOGIN_STATE == 0) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ChatFragment(), "Chat");
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new LoginFragment(), "Login");
            fragmentTransaction.commit();
        }
    }
}