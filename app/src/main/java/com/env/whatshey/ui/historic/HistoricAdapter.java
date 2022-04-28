package com.env.whatshey.ui.historic;

import static com.env.whatshey.utilities.FormatDateUtils.getDateMessage;
import static com.env.whatshey.utilities.FormatDateUtils.getHours;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;

import com.env.whatshey.R;
import com.env.whatshey.databinding.AdapterHistoricItemBinding;

import br.kleberf65.androidutils.base.BaseBindingAdapter;

public class HistoricAdapter extends BaseBindingAdapter<AdapterHistoricItemBinding, Historic> {

    private boolean itemSelectedExists;
    private final ItemClickListener clickListener;

    public HistoricAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getLayout() {
        return R.layout.adapter_historic_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        dataList.get(position).setAdapterPosition(position);
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void bindDataView(AdapterHistoricItemBinding binding, Historic historic) {
        binding.textNumber.setText(historic.getWhatsNumber());
        binding.textDate.setText(getHours(historic.getTimestamp()));
        binding.itemView.setGravity(historic.getGravity());
        binding.itemBubble.setBackgroundResource(historic.getBackgroundBubble());
        if (historic.isDateVisible()) {
            binding.textAdapterDate.setText(getDateMessage(historic.getTimestamp()));
            binding.textAdapterDate.setVisibility(View.VISIBLE);
        } else binding.textAdapterDate.setVisibility(View.GONE);
        binding.itemView.setBackgroundResource(historic.isItemSelected() ?
                R.drawable.historic_item_selected : R.drawable.historic_item_unselected);

        //Listeners
        binding.itemView.setOnClickListener(view -> {
            if (itemSelectedExists) {
                notifyItemSelectedOrClean(true, historic.getAdapterPosition());
                clickListener.onItemLongClick(view, historic);
            } else clickListener.onItemClick(view, historic);
        });
        binding.itemView.setOnLongClickListener(view -> {
            notifyItemSelectedOrClean(true, historic.getAdapterPosition());
            clickListener.onItemLongClick(view, historic);
            return true;
        });
    }

    public void notifyItemSelectedOrClean(boolean selected, int position) {
        for (int i = 0; i < dataList.size(); i++) {
            Historic item = dataList.get(i);
            if (selected)
                item.setItemSelected(item.getAdapterPosition() == position);
            else item.setItemSelected(false);
            notifyItemChanged(i, item);
        }
        itemSelectedExists = selected;
    }

    public void addHistoric(Historic historic) {
        dataList.add(historic);
        notifyItemInserted(dataList.size() - 1);
    }

    public void removeHistoric(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAll() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public boolean isItemSelectedExists() {
        return itemSelectedExists;
    }

    public interface ItemClickListener {
        void onItemClick(View view, Historic historic);

        default void onItemLongClick(View view, Historic historic) {
        }
    }
}
