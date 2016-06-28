package com.kabir.flickrtask.main.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    @Bind(R.id.tags_edit_text) EditText tagsEditText;
    @Bind(R.id.count_edit_text) EditText countEditText;
    @Bind(R.id.loading_progress_bar) ProgressBar loadingProgressBar;
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

        tagsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        countEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        contentRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        imageProvider = new GlideImageProvider(this);

        flickrAdapter = new FlickrAdapter(this, imageProvider);
        contentRecyclerView.setAdapter(flickrAdapter);

        flickrPresenter = new FlickrPresenterImpl(this, new FlickrApiProvider());
        flickrPresenter.attachView(getIntent().getExtras());
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
    public void showToast(@NonNull CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setContent(@Nullable List<FlickrItem> flickrItems) {
        flickrAdapter.updateDataSet(flickrItems);
    }

    @Override
    public void showContent(boolean show) {
        contentRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void doSearch() {

        if (flickrPresenter == null) {
            return;
        }

        String tags = tagsEditText.getText().toString();
        int count = 0;
        String countText = countEditText.getText().toString();
        if (!countText.isEmpty()){
            count = Integer.valueOf(countText);
        }
        flickrPresenter.loadData(tags, count);
    }

    private void hideKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
