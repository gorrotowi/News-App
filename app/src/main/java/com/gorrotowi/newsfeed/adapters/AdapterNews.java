package com.gorrotowi.newsfeed.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gorrotowi.newsfeed.R;
import com.gorrotowi.newsfeed.entitys.ItemNew;

import java.util.List;

/**
 * Created by Gorro on 29/01/17.
 */

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    private List<ItemNew> data;
    private Context context;

    public AdapterNews(Context context, List<ItemNew> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtItemTitle.setText(data.get(position).getTitle());
        holder.txtItemSubTitle.setText(data.get(position).getSubTitle());
        holder.txtItemSection.setText(data.get(position).getSection());
        Glide.with(context).load(data.get(position).getImgUrl()).into(holder.imgItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(data.get(holder.getAdapterPosition()).getUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItem;
        TextView txtItemTitle;
        TextView txtItemSubTitle;
        TextView txtItemSection;

        public ViewHolder(View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItemNew);
            txtItemTitle = (TextView) itemView.findViewById(R.id.txtItemTitle);
            txtItemSubTitle = (TextView) itemView.findViewById(R.id.txtItemSubTitle);
            txtItemSection = (TextView) itemView.findViewById(R.id.txtItemSection);
        }
    }

}
