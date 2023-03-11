package theanh.android.quanlydanhba.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import theanh.android.quanlydanhba.Adapter.ContactAdapter;
import theanh.android.quanlydanhba.AddContactActivity;
import theanh.android.quanlydanhba.ContactActivity;
import theanh.android.quanlydanhba.Object.Contacts;
import theanh.android.quanlydanhba.R;
import theanh.android.quanlydanhba.SearchActivity;

public class ContactFragment extends Fragment {

    private DatabaseReference contactRef;
    private RecyclerView rcvContact;
    private List<Contacts> contactsList;
    private ContactAdapter contactAdapter;
    long id = 0;

    private ImageView btnAdd;
    private RelativeLayout searchLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        rcvContact = view.findViewById(R.id.rcvContact);

        searchLayout = view.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        btnAdd = view.findViewById(R.id.addIcon);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });

        contactRef = FirebaseDatabase.getInstance().getReference().child("Contact");
        contactsList = new ArrayList<>();
        contactAdapter = new ContactAdapter(requireContext(), contactsList, new ContactAdapter.onContactClick() {
            @Override
            public void onItemContactClick(Contacts contacts) {
                showContact(contacts);
            }

            @Override
            public void onItemUpdate(Contacts contacts) {
                updateContact(contacts);
            }

            @Override
            public void onItemDelete(Contacts contacts) {
                deleteContact(contacts);
            }
        });
        rcvContact.setAdapter(contactAdapter);
        rcvContact.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.line));
        rcvContact.addItemDecoration(dividerItemDecoration);

        getListContact();
        return view;
    }


    private void getListContact() {
        contactRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Contacts contacts = snapshot.getValue(Contacts.class);
                if (contacts != null) {
                    contactsList.add(contacts);
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Contacts contacts = snapshot.getValue(Contacts.class);
                if (contacts == null || contactsList == null || contactsList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < contactsList.size(); i++) {
                    if (contacts.getId() == contactsList.get(i).getId()) {
                        contactsList.set(i, contacts);
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Contacts contacts = snapshot.getValue(Contacts.class);
                if (contacts == null || contactsList == null || contactsList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < contactsList.size(); i++) {
                    if (contacts.getId() == contactsList.get(i).getId()) {
                        contactsList.remove(contactsList.get(i));
                        break;
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateContact(Contacts contacts) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_updatecontact);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText uptContactName, uptContactNum;
        TextView cancel, done;

        uptContactName = dialog.findViewById(R.id.uptContactName);
        uptContactNum = dialog.findViewById(R.id.uptPhoneNum);

        uptContactName.setText(contacts.getName());
        uptContactNum.setText(contacts.getPhone());

        done = dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = uptContactName.getText().toString().trim();
                String num = uptContactNum.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    contacts.setName(name);
                }
                if (!TextUtils.isEmpty(num)) {
                    contacts.setPhone(num);
                }

                contactRef.child(String.valueOf(contacts.getId())).updateChildren(contacts.map(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteContact(Contacts contacts) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Comic")
                .setMessage("You sure ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        contactRef.child(String.valueOf(contacts.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(requireContext(), "Delete successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showContact(Contacts contacts) {
        Intent intent = new Intent(requireContext(), ContactActivity.class);
        intent.putExtra("name", contacts.getName());
        intent.putExtra("num", contacts.getPhone());
        startActivity(intent);
    }
}
