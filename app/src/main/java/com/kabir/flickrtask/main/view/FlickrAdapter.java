package com.kabir.flickrtask.main.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.kabir.flickrtask.R;
import com.kabir.flickrtask.main.model.FlickrItem;
import com.kabir.flickrtask.utils.ListUtils;
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
    private Animation inAnimation;
    private Animation outAnimation;

    private List<FlickrItem> itemList;

    private float THUMBNAIL_QUALITY = 0.25f;

    public FlickrAdapter(@NonNull Context context, @NonNull ImageProvider imageProvider) {
        layoutInflater = LayoutInflater.from(context);
        this.imageProvider = imageProvider;
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.animate_in);
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.animate_out);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(layoutInflater.inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewHolder(holder, position, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

        if (ListUtils.isNullOrEmpty(payloads)) {
            onBindViewHolder(holder, position);
            return;
        }

        int payload = (int) payloads.get(0);
        if (payload == position) {
            bindViewHolder(holder, position, true);
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.isNullOrEmpty(itemList) ? 0 : itemList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.view_switcher) ViewSwitcher viewSwitcher;
        @Bind(R.id.image_view) ImageView imageView;
        @Bind(R.id.text_view) TextView textView;

        public ImageViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            viewSwitcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ListUtils.isNullOrEmpty(itemList)){
                        return;
                    }

                    FlickrItem flickrItem = itemList.get(getAdapterPosition());
                    if (flickrItem == null){
                        return;
                    }

                    flickrItem.setFlipped(flickrItem.isFlipped());
                    notifyItemChanged(getAdapterPosition(), getAdapterPosition());
                }
            });
        }
    }

    public void updateDataSet(@Nullable List<FlickrItem> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    private void bindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean animate) {

        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        FlickrItem flickrItem = itemList.get(position);

        if (flickrItem == null) {
            return;
        }

        if (animate) {
            viewHolder.viewSwitcher.setInAnimation(inAnimation);
            viewHolder.viewSwitcher.setOutAnimation(outAnimation);
        } else {
            viewHolder.viewSwitcher.setInAnimation(null);
            viewHolder.viewSwitcher.setOutAnimation(null);
        }

        viewHolder.viewSwitcher.setDisplayedChild(flickrItem.isFlipped() ? 1 : 0);
        imageProvider.displayImage(flickrItem.getImageUrl(), THUMBNAIL_QUALITY, flickrItem.getImagePlaceholder(),
                                   android.R.color.darker_gray, viewHolder.imageView);
        viewHolder.textView.setText(flickrItem.getTitle());
    }
}
