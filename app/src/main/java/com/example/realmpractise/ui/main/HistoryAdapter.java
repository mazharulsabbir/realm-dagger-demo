package com.example.realmpractise.ui.main;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.realmpractise.R;
import com.example.realmpractise.models.Result;
import com.example.realmpractise.utils.ViewUtils;

import java.util.List;

public class HistoryAdapter extends BaseQuickAdapter<Result, BaseViewHolder> {
    public HistoryAdapter(@Nullable List<Result> data) {
        super(R.layout.item_history, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Result item) {
        if (helper.getAdapterPosition() == getItemCount()-1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            params.bottomMargin = (int)ViewUtils.pxFromDp(100);
        }
        helper.setText(R.id.item_history_name,
                helper.itemView.getContext().getString(R.string.name, item.getName().getTitle(), item.getName().getFirst(), item.getName().getLast()));
        helper.setText(R.id.item_history_email, item.getEmail());
        Glide.with(helper.itemView)
                .load(item.getPicture().getThumbnail())
                .circleCrop()
                .into((ImageView) helper.getView(R.id.item_history_pic));
    }
}
