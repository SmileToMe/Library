package com.hushi.CRM.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hushi.CRM.R;
import com.hushi.CRM.bean.LogBean;
import com.p.library.utils.ViewUtil;
import com.p.library.widget.recycler.BaseRecyclerAdapter;
import com.p.library.widget.recycler.BaseViewHolder;
import com.p.library.widget.recycler.RecyclerViewM;

import java.util.ArrayList;
import java.util.List;

/**
 * 我发出的
 */
public class SentFragment extends BaseFragment {

    private View view;

    private RecyclerViewM mRecyclerView;

    private SwipeRefreshLayout mSwipeLayout;

    private SentAdapter mAdapter;


    public static SentFragment newInstance() {
        Bundle args = new Bundle();
        SentFragment fragment = new SentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_log_sent, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mSwipeLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerViewM) inflate.findViewById(R.id.recycler_view);
        ViewUtil.initSwipeRefresh(mSwipeLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeLayout.setRefreshing(false);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SentAdapter();
        mRecyclerView.setAdapter(mAdapter);

        List<LogBean> logList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            logList.add(new LogBean());
        }
        mAdapter.setData(logList);

    }


    private class SentAdapter extends BaseRecyclerAdapter<LogBean> {

        @Override
        protected int getLayoutId() {
            return R.layout.item_log_sent;
        }

        @Override
        protected void bind(BaseViewHolder holder, LogBean logBean) {

            holder.setText(R.id.item_logNickname, "客户名字" + "（" + "拜访方式" + "）");
            holder.setText(R.id.item_logSentTime, "2018-5-7 16:51");
            holder.setText(R.id.item_logArrangements, "学术会议");
            holder.setText(R.id.item_logIntention, "不详");
            holder.setText(R.id.item_logVisitTime, "2018-5-7 16:52:19-12:00");
            holder.setText(R.id.item_logDescription, "客户外出未归，已电话联系！客户外出未归，已电话联系！客户外出未归，已电话联系！");

        }
    }


}
