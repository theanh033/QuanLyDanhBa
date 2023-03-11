package theanh.android.quanlydanhba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import theanh.android.quanlydanhba.Object.Contacts;
import theanh.android.quanlydanhba.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contacts> contactsList;
    private onContactClick onContactClick;

    public interface onContactClick{
        void onItemContactClick(Contacts contacts);
        void onItemUpdate(Contacts contacts);
        void onItemDelete(Contacts contacts);
    }

    public ContactAdapter(Context context, List<Contacts> contactsList, onContactClick onContactClick) {
        this.context = context;
        this.contactsList = contactsList;
        this.onContactClick = onContactClick;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contacts contacts = contactsList.get(position);
        holder.txvName.setText(contactsList.get(position).getName());
        holder.txvNum.setText(contactsList.get(position).getPhone());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContactClick.onItemUpdate(contacts);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContactClick.onItemDelete(contacts);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (contactsList != null) {
            return contactsList.size();
        }
        return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView txvName, txvNum;
        private Button delete, edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            txvName = itemView.findViewById(R.id.name);
            txvNum = itemView.findViewById(R.id.num);
            delete = itemView.findViewById(R.id.deleteContact);
            edit = itemView.findViewById(R.id.editContact);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onContactClick.onItemContactClick(contactsList.get(getAdapterPosition()));
                }
            });
        }
    }
}
