package theanh.android.quanlydanhba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import theanh.android.quanlydanhba.Adapter.ContactAdapter;
import theanh.android.quanlydanhba.Object.Contacts;

public class AddContactActivity extends AppCompatActivity {

    DatabaseReference contactRef;
    List<Contacts> contactsList;
    ContactAdapter contactAdapter;

    EditText contactName, contactNum;
    Button cancel, done;

    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getSupportActionBar().setTitle("Add Contact");

        contactName = findViewById(R.id.edtContactName);
        contactNum = findViewById(R.id.edtPhoneNum);

        cancel = findViewById(R.id.btnCancel);
        done = findViewById(R.id.btnDone);

        contactRef = FirebaseDatabase.getInstance().getReference().child("Contact");

        contactRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        contactsList = new ArrayList<>();
//        contactAdapter = new ContactAdapter(AddContactActivity.this, contactsList, new ContactAdapter.onContactClick() {
//            @Override
//            public void onItemContactClick(Contacts contacts) {
//
//            }
//        });

        addContact();
    }

    private void addContact() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cId = String.valueOf(id+1);
                String name = contactName.getText().toString().trim();
                String num = contactNum.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AddContactActivity.this, "Please enter contact's name.", Toast.LENGTH_SHORT).show();
                    contactName.setError("Contact's name is required");
                    contactName.requestFocus();
                } else if (TextUtils.isEmpty(num)) {
                    Toast.makeText(AddContactActivity.this, "Please enter contact's phone number.", Toast.LENGTH_SHORT).show();
                    contactNum.setError("Contact's phone number is required");
                    contactNum.requestFocus();
                } else if (!Patterns.PHONE.matcher(num).matches()) {
                    Toast.makeText(AddContactActivity.this, "Please re-enter contact's phone number", Toast.LENGTH_SHORT).show();
                    contactNum.setError("Valid phone number is required");
                    contactNum.requestFocus();
                } else {
                    Contacts contacts = new Contacts(cId, name, num);
                    String path = String.valueOf(contacts.getId());

                    contactRef.child(path).setValue(contacts, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(AddContactActivity.this, "Add successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}