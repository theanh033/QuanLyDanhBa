package theanh.android.quanlydanhba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import theanh.android.quanlydanhba.Fragment.ContactFragment;
import theanh.android.quanlydanhba.Fragment.KeypadFragment;
import theanh.android.quanlydanhba.Fragment.RecentFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    ContactFragment contactFragment = new ContactFragment();
    RecentFragment recentFragment = new RecentFragment();
    KeypadFragment keypadFragment  = new KeypadFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.mainNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, recentFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_contacts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,contactFragment).commit();
                        return true;
                    case R.id.menu_recents:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,recentFragment).commit();
                        return true;
                    case R.id.menu_keypad:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,keypadFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}