package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapplication.R;
import com.example.foodapplication.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View TransactionView = inflater.inflate(R.layout.transaction_layout, parent, false);
        return new ViewHolder(TransactionView);
    }

    @Override
    public void onBindViewHolder(TransactionAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.txtTransactionDate.setText(transaction.getDate());
        holder.txtTransactionAmount.setText(Integer.toString(transaction.getCredits()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTransactionDate, txtTransactionAmount;
        public ViewHolder(View itemView) {
            super(itemView);

            txtTransactionDate = itemView.findViewById(R.id.txtTransactionDate);
            txtTransactionAmount = itemView.findViewById(R.id.txtTransactionAmount);
        }
    }

    public TransactionAdapter(List<Transaction> transactions) { this.transactions = transactions; }
}
