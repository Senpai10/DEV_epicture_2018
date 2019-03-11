package eu.epitech.epicture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String AccessToken;
    private String username;
    Fragment selectedFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.navigation_upload:
                    selectedFragment = new UploadFragment();
                    break;
                case R.id.navigation_favorite:
                    selectedFragment = new FavoriteFragment();
                    break;
                case R.id.navigation_profil:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    public void onChooseImage(View view){
        Log.d("onChooseImageMAIN", "MAINBegin function");
        ((UploadFragment)selectedFragment).onChooseImage(view);
        Log.d("onChooseImageMAIN", "MAINBegin function");
    }

    public void onUploadImage(View view){
        ((UploadFragment)selectedFragment).onUploadImage(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AccessToken = getIntent().getStringExtra("token");
        username = getIntent().getStringExtra("username");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    public String getToken() {
        return (AccessToken);
    }


    private View.OnClickListener btnLogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("DEBUG", "CLICKER");
        }
    };
}