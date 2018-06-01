package com.hushi.CRM.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hushi.CRM.R;
import com.p.library.widget.recycler.RecyclerViewM;

/**
 * 我收到的
 */
public class ReceivedFragment extends BaseFragment {

    private View view;
    private RecyclerViewM mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    public static ReceivedFragment newInstance() {
        Bundle args = new Bundle();
        ReceivedFragment fragment = new ReceivedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_log_received, container, false);

        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mRecyclerView = (RecyclerViewM) inflate.findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh_layout);
    }
}
