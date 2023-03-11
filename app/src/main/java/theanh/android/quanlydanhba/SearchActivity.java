package theanh.android.quanlydanhba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import theanh.android.quanlydanhba.Adapter.SearchAdapter;
import theanh.android.quanlydanhba.Object.Contacts;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView txvCancel;
    private RecyclerView rcvSearch;

    private ArrayList<Contacts> contactsList;
    private SearchAdapter searchAdapter;
    private DatabaseReference contacRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        rcvSearch = findViewById(R.id.rcvSearch);

        contactsList = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, new SearchAdapter.onContactClick() {
            @Override
            public void onContactClick(Contacts contacts) {

            }

            @Override
            public void onItemUpdate(Contacts contacts) {

            }

            @Override
            public void onItemDelete(Contacts contacts) {

            }
        });

        rcvSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvSearch.setAdapter(searchAdapter);

        getListContact();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return false;
            }
        });

        txvCancel = findViewById(R.id.backToExplore);
        txvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getListContact() {
        contacRef = FirebaseDatabase.getInstance().getReference("Contact");
        contacRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (contactsList != null) {
                    contactsList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Contacts comics = dataSnapshot.getValue(Contacts.class);
                    contactsList.add(comics);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchContact(String newText) {
        ArrayList<Contacts> filterList = new ArrayList<>();
        for (Contacts contacts : contactsList) {
            if (contacts.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(contacts);
            }
            searchAdapter.setFilterList(filterList);
        }
    }

    private void showChapter(Contacts contacts) {
        Intent intent = new Intent(SearchActivity.this, ContactActivity.class);
        intent.putExtra("name", contacts.getName());
        intent.putExtra("num", contacts.getPhone());
        startActivity(intent);
    }
}
