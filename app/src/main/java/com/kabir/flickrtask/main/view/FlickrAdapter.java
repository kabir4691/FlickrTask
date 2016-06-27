package com.kabir.flickrtask.main.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.kabir.flickrtask.R;
import com.kabir.flickrtask.main.model.FlickrItem;
import com.kabir.flickrtask.utils.ListUtils;
import com.kabir.flickrtask.utils.RandomColorGenerator;
import com.kabir.flickrtask.utils.image.provider.ImageProvider;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kabir on 27-06-2016.
 */
public class FlickrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ImageProvider imageProvider;

    private List<FlickrItem> itemList;

    public FlickrAdapter(@NonNull Context context, @NonNull ImageProvider imageProvider) {
        layoutInflater = LayoutInflater.from(context);
        this.imageProvider = imageProvider;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(layoutInflater.inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        FlickrItem flickrItem = itemList.get(position);

        if (flickrItem == null) {
            return;
        }

        imageProvider.displayImage(flickrItem.getImageUrl(), 0.3f, RandomColorGenerator.getRandomColor(),
                                   android.R.color.darker_gray, viewHolder.imageView);
        viewHolder.textView.setText(flickrItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return ListUtils.isNullOrEmpty(itemList) ? 0 : itemList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.view_switcher) ViewSwitcher viewSwitcher;
        @Bind(R.id.image_view) ImageView imageView;
        @Bind(R.id.text_view) TextView textView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            viewSwitcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewSwitcher.showNext();
                }
            });
        }
    }

    public void updateDataSet(@Nullable List<FlickrItem> list) {
        itemList = list;
        notifyDataSetChanged();
    }
}
