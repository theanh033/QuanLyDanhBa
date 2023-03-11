package theanh.android.quanlydanhba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import theanh.android.quanlydanhba.Object.Contacts;
import theanh.android.quanlydanhba.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewholder> {

    private Context context;
    private ArrayList<Contacts> mContactData;
    private onContactClick onContactClick;

    public interface onContactClick {
        void onContactClick(Contacts contacts);
        void onItemUpdate(Contacts contacts);
        void onItemDelete(Contacts contacts);
    }

    public SearchAdapter(Context context, onContactClick onContactClick) {
        this.context = context;
        this.onContactClick = onContactClick;
    }

    public void setDisplayData(ArrayList<Contacts> contacts) {
        mContactData.clear();
        mContactData.addAll(contacts);
        notifyDataSetChanged();
    }

    public void setFilterList(ArrayList<Contacts> contacts) {
        this.mContactData = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewholder holder, int position) {
        Contacts contacts = mContactData.get(position);
        holder.txvName.setText(mContactData.get(position).getName());
        holder.txvNum.setText(mContactData.get(position).getPhone());
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
        if (mContactData != null){
            return mContactData.size();
        }
         return 0;
    }

    public class SearchViewholder extends RecyclerView.ViewHolder {

        private TextView txvName, txvNum;
        private Button delete, edit;

        public SearchViewholder(@NonNull View itemView) {
            super(itemView);

            txvName = itemView.findViewById(R.id.name);
            txvNum = itemView.findViewById(R.id.num);
            delete = itemView.findViewById(R.id.deleteContact);
            edit = itemView.findViewById(R.id.editContact);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onContactClick.onContactClick(mContactData.get(getAdapterPosition()));
                }
            });
        }
    }
}
