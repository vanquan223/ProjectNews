package vn.vnpt.vanquan223.projectnews.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.vnpt.vanquan223.projectnews.R;
import vn.vnpt.vanquan223.projectnews.model.ListNewsModel;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<ListNewsModel> listNewsModels;
    public IRegisterClick iRegisterClick;

    public RecyclerViewAdapter(Activity activity, List<ListNewsModel> listNewsModels) {
        this.activity = activity;
        this.listNewsModels = listNewsModels;
    }

    public void reloadData(List<ListNewsModel> listNewsModels) {
        this.listNewsModels = listNewsModels;
        this.notifyDataSetChanged();
    }

    public void registerClickAdapter(Activity activity) {
        iRegisterClick = (IRegisterClick) activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_view, parent, false);
        NewHolder newHolder = new NewHolder(view);
        return newHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewHolder newHolder = (NewHolder) holder;
        ListNewsModel model = listNewsModels.get(position);
        newHolder.tvDate.setText(model.getDate());
        if (model.getTitle() != null)
            newHolder.tvTitle.setText(Html.fromHtml(model.getTitle().getRendered()));
        if (model.getExcerpt() != null)
            newHolder.tvExcerpt.setText(Html.fromHtml(model.getExcerpt().getRendered()));
        if (model.getBetter_featured_image() != null)
            Glide.with(activity).load(model.getBetter_featured_image().getSource_url()).into(newHolder.ivImage);


    }

    @Override
    public int getItemCount() {
        return listNewsModels.size();
    }

    private class NewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        ImageView ivImage;
        TextView tvExcerpt;
        Button btBookmark;
        Button btShare;

        public NewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvExcerpt = itemView.findViewById(R.id.tvExcerpt);
            btBookmark = itemView.findViewById(R.id.btBookmark);
            btShare = itemView.findViewById(R.id.btShare);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRegisterClick.onClickItem(getAdapterPosition());
                }
            });
            btBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListNewsModel model = listNewsModels.get(getAdapterPosition());
                    if (model.isBookMark() == false) {
                        btBookmark.setBackgroundResource(R.drawable.ic_bookmark_filled);
                    }else {
                        btBookmark.setBackgroundResource(R.drawable.ic_bookmark);
                    }
                    iRegisterClick.onClickBookMark(getAdapterPosition());
                }
            });
            btShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRegisterClick.onClickShare(getAdapterPosition());
                }
            });

        }
    }

    public interface IRegisterClick {
        void onClickItem(int position);

        void onClickBookMark(int position);

        void onClickShare(int position);
    }
}
