package vn.vnpt.vanquan223.projectnews.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.vnpt.vanquan223.projectnews.R;
import vn.vnpt.vanquan223.projectnews.model.ListNewsDBModel;
import vn.vnpt.vanquan223.projectnews.model.ListNewsModel;

public class ListViewAdapter extends BaseAdapter {
    Activity activity;
    List<ListNewsModel> listNewsModels;

    public ListViewAdapter(Activity activity, List<ListNewsModel> listNewsModels) {
        this.activity = activity;
        this.listNewsModels = listNewsModels;
    }

    @Override
    public int getCount() {
        return listNewsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_view, parent, false);

            Hold hold = new Hold();
            hold.tvTitle = convertView.findViewById(R.id.tvTitle);
            hold.tvDate = convertView.findViewById(R.id.tvDate);
            hold.tvExcerpt = convertView.findViewById(R.id.tvExcerpt);
            hold.ivImage = convertView.findViewById(R.id.ivImage);
            convertView.setTag(hold);
        }

        Hold hold = (Hold) convertView.getTag();
        ListNewsModel model = listNewsModels.get(position);
        if (model.getTitle() != null)
            hold.tvTitle.setText(Html.fromHtml(model.getTitle().getRendered()));
        hold.tvDate.setText(model.getDate());
        if (model.getExcerpt() != null)
            hold.tvExcerpt.setText(Html.fromHtml(model.getExcerpt().getRendered()));
        if (model.getBetter_featured_image() != null)
            Glide.with(activity).load(model.getBetter_featured_image().getSource_url()).into(hold.ivImage);

        return convertView;
    }

    private static class Hold {
        TextView tvTitle;
        TextView tvDate;
        ImageView ivImage;
        TextView tvExcerpt;
    }
}
