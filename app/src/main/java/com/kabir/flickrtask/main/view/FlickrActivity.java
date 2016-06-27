package com.kabir.flickrtask.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kabir.flickrtask.R;
import com.kabir.flickrtask.main.api.FlickrApiProvider;
import com.kabir.flickrtask.main.model.FlickrItem;
import com.kabir.flickrtask.main.presenter.FlickrPresenter;
import com.kabir.flickrtask.main.presenter.FlickrPresenterImpl;
import com.kabir.flickrtask.utils.image.provider.GlideImageProvider;
import com.kabir.flickrtask.utils.image.provider.ImageProvider;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FlickrActivity extends AppCompatActivity implements FlickrView {

    @Bind(R.id.content_recycler_view) RecyclerView contentRecyclerView;

    private ImageProvider imageProvider;

    private FlickrAdapter flickrAdapter;

    private FlickrPresenter flickrPresenter;

    private boolean isViewDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isViewDestroyed = false;

        setContentView(R.layout.flickr_activity);
        ButterKnife.bind(this);

        contentRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        imageProvider = new GlideImageProvider(this);

        flickrAdapter = new FlickrAdapter(this, imageProvider);
        contentRecyclerView.setAdapter(flickrAdapter);

        flickrPresenter = new FlickrPresenterImpl(this, new FlickrApiProvider());
        if (flickrPresenter.attachView(getIntent().getExtras())) {
            flickrPresenter.loadData();
        }
    }

    @Override
    protected void onDestroy() {

        isViewDestroyed = true;

        if (flickrPresenter != null) {
            flickrPresenter.detachView();
        }
        if (imageProvider != null) {
            imageProvider.releaseMemory();
        }

        super.onDestroy();
    }

    @Override
    public boolean isViewDestroyed() {
        return isViewDestroyed;
    }

    @Override
    public void setContent(@Nullable List<FlickrItem> flickrItems) {
        flickrAdapter.updateDataSet(flickrItems);
    }
}
