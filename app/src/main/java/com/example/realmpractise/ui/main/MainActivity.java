/*
 * Copyright 2020 Mushfiqus Salehin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.example.realmpractise.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realmpractise.R;
import com.example.realmpractise.data.RepositoryComponent;
import com.example.realmpractise.models.Resource;
import com.example.realmpractise.models.Result;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

/**
 * Provides the main UI of the app
 **/

public class MainActivity extends AppCompatActivity implements HistoryFragment.OnHistoryClickListener {
    private static final String TAG = "MainActivity";

    private boolean mOnline = true;

    private MainViewModel mViewModel;
    @BindView(R.id.main_propic)
    ImageView mProPic;
    @BindView(R.id.main_name)
    TextView mName;
    @BindView(R.id.main_email)
    TextView mEmail;
    @BindView(R.id.main_phone)
    TextView mPhone;
    @BindView(R.id.main_switch)
    FloatingActionButton mSwitch;
    @BindView(R.id.main_refresh)
    FloatingActionButton mRefresh;
    @BindView(R.id.main_shimmer)
    ShimmerFrameLayout mLoading;
    @BindView(R.id.main_content)
    ConstraintLayout mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupUI();
    }

    private void setupUI() {
        mViewModel.getmUserListener()
                .observe(this, resultResource -> {
                    if (resultResource != null) {
                        switch (resultResource.getStatus()) {
                            case SUCCESSFUL:
                                showLoading(false);
                                Result result = resultResource.getData();
                                if (result != null) {
                                    setError(false);
                                    showUser(result);
                                } else {
                                    setError(true);
                                }
                                break;
                            case ERROR:
                                showLoading(false);
                                setError(true);
                                Log.e(TAG, "onChanged: ", resultResource.getError());
                                break;
                            case LOADING:
                                showLoading(true);
                                break;
                        }
                    }
                });
        mViewModel.loadUser(mOnline);
        showOnline(mOnline);
        mSwitch.setOnClickListener(v -> {
            mOnline = !mOnline;
            mViewModel.loadUser(mOnline);
            showOnline(mOnline);
        });
    }

    /** Displays or hides error messages */

    private void setError(boolean error) {
        if (error) {
            mProPic.setVisibility(View.INVISIBLE);
            mName.setText(getString(R.string.error));
            mEmail.setVisibility(View.INVISIBLE);
            mPhone.setVisibility(View.INVISIBLE);
        } else {
            mProPic.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mPhone.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mContent.setVisibility(View.VISIBLE);
        }
    }

    private void showOnline(boolean online) {
        if (online) {
            getSupportActionBar().setTitle(MainActivity.this.getString(R.string.online));
            mRefresh.setImageResource(R.drawable.ic_refresh);
            mRefresh.setOnClickListener(v -> {
                mViewModel.loadUser(true);
            });
        } else {
            getSupportActionBar().setTitle(MainActivity.this.getString(R.string.offline));
            mRefresh.setImageResource(R.drawable.ic_history);
            mRefresh.setOnClickListener(v -> {
                HistoryFragment historyFragment = HistoryFragment.newInstance();
                historyFragment.show(getSupportFragmentManager(), "History");
            });
        }
    }

    private void showUser(Result result) {
        Glide.with(mProPic)
                .load(result.getPicture().getLarge())
                .circleCrop()
                .into(mProPic);

        mName.setText(getString(R.string.name, result.getName().getTitle(), result.getName().getFirst(), result.getName().getLast()));
        mEmail.setText(getString(R.string.email, result.getEmail()));
        mPhone.setText(getString(R.string.phone, result.getPhone()));
    }

    @Override
    public void onHistoryItemClick(Result result) {
        showUser(result);
    }
}