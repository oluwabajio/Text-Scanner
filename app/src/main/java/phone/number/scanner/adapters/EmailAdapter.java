package phone.number.scanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import phone.number.scanner.R;
import phone.number.scanner.models.Email;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.MyViewHolder> {

    private List<Email> emailList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmail;

        public MyViewHolder(View view) {
            super(view);
            tvEmail = (TextView) view.findViewById(R.id.tv_email);

        }
    }


    public EmailAdapter(List<Email> emailList) {
        this.emailList = emailList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_email, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Email email = emailList.get(position);
        holder.tvEmail.setText(email.getEmailAddress());

    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }
}