package phone.number.scanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import phone.number.scanner.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private List<String> itemList;
    private String itemName;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem;

        public MyViewHolder(View view) {
            super(view);
            tvItem = (TextView) view.findViewById(R.id.tv_item);

        }
    }


    public ItemAdapter(List<String> itemList, String itemName) {
        this.itemList = itemList;
        this.itemName = itemName;
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (itemName.equalsIgnoreCase("email")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_email, parent, false);
        } else  if (itemName.equalsIgnoreCase("phone_number")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_phone_number, parent, false);
        } else  if (itemName.equalsIgnoreCase("website")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_web_url, parent, false);
        }

        return new ItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.MyViewHolder holder, int position) {
        String item = itemList.get(position);

        holder.tvItem.setText(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}