package com.example.realmpractise.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.realmpractise.R;
import com.example.realmpractise.models.Resource;
import com.example.realmpractise.models.Result;
import com.example.realmpractise.models.Status;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHistoryClickListener} interface
 * to handle interaction events.
 */
public class HistoryFragment extends BottomSheetDialogFragment {
    private static final String TAG = "HistoryFragment";

    private OnHistoryClickListener mListener;
    private MainViewModel mViewModel;
    private HistoryAdapter mAdapter;

    @BindView(R.id.history_list)
    RecyclerView mList;
    @BindView(R.id.history_toolbar)
    Toolbar mToolbar;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        return view;
    }

    private void setupUI() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mToolbar.inflateMenu(R.menu.history_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.item_history_close) {
                dismiss();
            }
            return true;
        });
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HistoryAdapter(new ArrayList<>());
        mAdapter.bindToRecyclerView(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Result result = (Result) adapter.getItem(position);
            if (mListener != null) {
                mListener.onHistoryItemClick(result);
            }
        });
        mViewModel
                .getmLatestSavesListener()
                .observe(this, resultResource -> {
                    if (resultResource != null) {
                        if (resultResource.getStatus() == Status.SUCCESSFUL) {
                            mAdapter.replaceData(resultResource.getData());
                        }
                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryClickListener) {
            mListener = (OnHistoryClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHistoryClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHistoryClickListener {
        void onHistoryItemClick(Result result);
    }
}
