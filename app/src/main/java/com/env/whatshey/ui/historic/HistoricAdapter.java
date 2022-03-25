package com.env.whatshey.ui.historic;

import static com.env.whatshey.ui.historic.Historic.TYPE_CHAT_LEFT;
import static com.env.whatshey.ui.historic.Historic.TYPE_CHAT_RIGHT;
import static com.env.whatshey.ui.historic.Historic.TYPE_DATE_INDICATOR;
import static com.env.whatshey.utilities.FormatDateUtils.getDateMessage;
import static com.env.whatshey.utilities.FormatDateUtils.getHours;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.env.whatshey.R;
import com.env.whatshey.databinding.AdapterDateBinding;
import com.env.whatshey.databinding.AdapterReceiverBinding;
import com.env.whatshey.databinding.AdapterSenderBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoricAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> sectionsDate = new ArrayList<>();
    private final List<Historic> historicList = new ArrayList<>();
    private List<String> currentSelectedHistoric = new ArrayList<>();
    private ClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Historic.TYPE_CHAT_LEFT) {
            return new ReceiverHolder(inflater.inflate(R.layout.adapter_receiver, parent, false));
        } else if (viewType == TYPE_CHAT_RIGHT) {
            return new SenderHolder(inflater.inflate(R.layout.adapter_sender, parent, false));
        } else return new DateHolder(inflater.inflate(R.layout.adapter_date, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReceiverHolder) {
            ReceiverHolder receiverHolder = (ReceiverHolder) holder;
            receiverHolder.bindView(historicList.get(position));
            receiverHolder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(historicList.get(position));
                }
            });
            receiverHolder.itemView.setOnLongClickListener(view -> {
                if (listener != null) {
                    currentSelectedHistoric.add(historicList.get(position).getId());
                    listener.onItemLongClick(position, historicList.get(position));
                    return true;
                }
                return false;
            });
        } else if (holder instanceof SenderHolder) {
            SenderHolder senderHolder = (SenderHolder) holder;
            senderHolder.bindView(historicList.get(position));
            senderHolder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(historicList.get(position));
                }
            });
            senderHolder.itemView.setOnLongClickListener(view -> {
                if (listener != null) {
                    currentSelectedHistoric.add(historicList.get(position).getId());
                    listener.onItemLongClick(position, historicList.get(position));
                    return true;
                }
                return false;
            });
        } else {
            DateHolder dateHolder = (DateHolder) holder;
            dateHolder.bindView(getDateMessage(historicList.get(position).getTimestamp()));
        }
    }

    public List<String> getCurrentSelectedHistoric() {
        return currentSelectedHistoric;
    }

    public void cleanSelectedHistoric() {
        for (int i = 0; i < historicList.size(); i++) {
            Historic historic = historicList.get(i);
            for (String id : currentSelectedHistoric) {
                if (id.equals(historic.getId())) {
                    historic.setItemSelected(false);
                    notifyItemChanged(i, historic);
                }
            }
        }
    }

    public void addData(Historic historic) {
        sectionsDate.add(getDateMessage(historic.getTimestamp()));

        int oldPosition = sectionsDate.size() - 1;
        if (oldPosition >= 1) {
            if (!sectionsDate.get(oldPosition).equals(sectionsDate.get(oldPosition - 1))) {
                this.historicList.add(new Historic(historic.getTimestamp(), TYPE_DATE_INDICATOR));
            }
        } else {
            this.historicList.add(new Historic(historic.getTimestamp(), TYPE_DATE_INDICATOR));
        }
        int type = getItemCount() == 0 ? TYPE_CHAT_RIGHT :
                historicList.get(getItemCount() - 1).getHistoricType() == TYPE_CHAT_RIGHT
                        ? TYPE_CHAT_LEFT : TYPE_CHAT_RIGHT;
        historic.setHistoricType(type);
        this.historicList.add(historic);
        notifyItemInserted(getItemCount());
    }

    public void addData(List<Historic> historicList) {
        int oldCount = historicList.size();
        for (int i = 0; i < historicList.size(); i++) {
            Historic historic = historicList.get(i);
            int type = i % 2 == 0 ? TYPE_CHAT_RIGHT : TYPE_CHAT_LEFT;
            historic.setHistoricType(type);
            sectionsDate.add(getDateMessage(historic.getTimestamp()));
            if (i >= 1) {
                if (!sectionsDate.get(i).equals(sectionsDate.get(i - 1))) {
                    this.historicList.add(new Historic(historic.getTimestamp(), TYPE_DATE_INDICATOR));
                }
            } else
                this.historicList.add(new Historic(historic.getTimestamp(), TYPE_DATE_INDICATOR));
            this.historicList.add(historic);
        }
        notifyItemRangeInserted(oldCount, this.historicList.size());
    }

    @Override
    public int getItemViewType(int position) {
        return historicList.get(position).getHistoricType();
    }

    @Override
    public int getItemCount() {
        return historicList.size();
    }

    public void setHistoricClickListener(ClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clean() {
        historicList.clear();
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClick(Historic historic);

        void onItemLongClick(int position, Historic historic);
    }

    public static class SenderHolder extends RecyclerView.ViewHolder {
        private final AdapterSenderBinding binding;

        public SenderHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterSenderBinding.bind(itemView);
        }

        private void bindView(Historic historic) {
            binding.textNumberSender.setText(historic.getWhatsNumber());
            binding.textDateSender.setText(getHours(historic.getTimestamp()));
            binding.linearSender.setBackgroundResource(historic.getBackground());
        }
    }

    public static class ReceiverHolder extends RecyclerView.ViewHolder {
        private final AdapterReceiverBinding binding;

        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterReceiverBinding.bind(itemView);
        }

        private void bindView(Historic historic) {
            binding.textNumberReceiver.setText(historic.getWhatsNumber());
            binding.textDateReceiver.setText(getHours(historic.getTimestamp()));
            binding.linearReceiver.setBackgroundResource(historic.getBackground());
        }
    }

    public static class DateHolder extends RecyclerView.ViewHolder {
        private final AdapterDateBinding binding;

        public DateHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterDateBinding.bind(itemView);
        }

        private void bindView(String date) {
            binding.textAdapterDate.setText(date);
        }
    }
}
