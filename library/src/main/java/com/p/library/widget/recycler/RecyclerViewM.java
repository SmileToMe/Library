package com.p.library.widget.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.p.library.utils.LogUtil;


/**
 * Created by Jiang on 2016/4/21.
 * <p>
 */
public class RecyclerViewM extends RecyclerView {


    public RecyclerViewM(Context context) {
        this(context, null);
    }

    public RecyclerViewM(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewM(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 当前RecyclerView类型
     */
    protected LayoutManagerType layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    private boolean isLoading;

    private boolean noMore;

    private int pageNow;

    private FooterHolder mFooter;

    private onLoadListener onLoadListener;


    public void setOnLoadListener(onLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;

        init();
    }

    HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        mHeaderAndFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        super.setAdapter(mHeaderAndFooterAdapter);
    }

    public void addHeader(View header) {
        mHeaderAndFooterAdapter.addHeaderView(header);
        if (mHeaderAndFooterAdapter.getInnerAdapter() instanceof BaseRecyclerAdapter)
            ((BaseRecyclerAdapter) mHeaderAndFooterAdapter.getInnerAdapter()).
                    setHeadViewCount(mHeaderAndFooterAdapter.getHeaderViewsCount());
    }

    public void addFooter(View footer) {
        mHeaderAndFooterAdapter.addFooterView(footer);
        if (mHeaderAndFooterAdapter.getInnerAdapter() instanceof BaseRecyclerAdapter)
            ((BaseRecyclerAdapter) mHeaderAndFooterAdapter.getInnerAdapter()).
                    setFooterViewCount(mHeaderAndFooterAdapter.getFooterViewsCount());
    }

    public void removeHeader(View header) {
        mHeaderAndFooterAdapter.removeHeaderView(header);
        if (mHeaderAndFooterAdapter.getInnerAdapter() instanceof BaseRecyclerAdapter)
            ((BaseRecyclerAdapter) mHeaderAndFooterAdapter.getInnerAdapter()).
                    setHeadViewCount(mHeaderAndFooterAdapter.getHeaderViewsCount());
    }

    public void removeHeader() {
        View headerView = mHeaderAndFooterAdapter.getHeaderView();
        if (headerView != null)
            removeHeader(headerView);
    }

    public View getHeaderView() {
        return mHeaderAndFooterAdapter.getHeaderView();
    }

    public boolean isHeaderOrFooter(int position) {
        return mHeaderAndFooterAdapter.isHeader(position) || mHeaderAndFooterAdapter.isFooter(position);
    }

    public void init() {
        if (mFooter == null) {
            mFooter = new FooterHolder(this);

            mHeaderAndFooterAdapter.addFooterView(mFooter.getView());
            LayoutManager layout = getLayoutManager();
            if (layout instanceof GridLayoutManager) {
                GridLayoutManager manager = (GridLayoutManager) layout;
                final int spanCount = manager.getSpanCount();
                //相当于weight 值 spanCount 总weight
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (isHeaderOrFooter(position))
                            return spanCount;
                        else
                            return 1;
                    }
                });
            }
        }
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onLoadListener == null)
                    return;

                LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManagerType == null) {
                    if (layoutManager instanceof GridLayoutManager) {
                        layoutManagerType = LayoutManagerType.GridLayout;
                    } else if (layoutManager instanceof LinearLayoutManager) {
                        layoutManagerType = LayoutManagerType.LinearLayout;
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        layoutManagerType = LayoutManagerType.StaggeredGridLayout;
                    } else {
                        throw new RuntimeException(
                                "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                    }
                }

                switch (layoutManagerType) {
                    case LinearLayout:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case GridLayout:
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case StaggeredGridLayout:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null) {
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        }
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        lastVisibleItemPosition = findMax(lastPositions);
                        break;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onLoadListener == null)
                    return;

                if (isLoading || noMore || pageNow == 0)
                    return;
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 &&
                        newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition >= totalItemCount - 2)) {
                    isLoading = true;
                    onLoadListener.onLoadNextPage(pageNow + 1);
                }
            }
        });
    }

    public int getPageNow() {
        return pageNow;
    }

    public boolean isNoMore() {
        return noMore;
    }

    public void setPageNow(int totalPage, int pageNow) {
        LogUtil.d("LoadMoreRecyclerView " + this.hashCode() + " totalPage:" + totalPage + " pageNow " + pageNow);
        this.pageNow = pageNow;
        if (pageNow >= totalPage) {
            noMore = true;
            if (onLoadListener != null)
                setFooterNoMore();
        } else {
            noMore = false;
            if (onLoadListener != null)
                resetFooter();
        }
        isLoading = false;
    }

    public View getFootView() {
        if (mFooter == null)
            init();
        return mFooter.getView();
    }

    public void setFooterNoMore() {
        if (mFooter == null)
            init();
        mFooter.setFooterNoMore();
    }


    public void resetFooter() {
        if (mFooter == null)
            init();
        mFooter.reSetFooter();
    }

    public void setFooterMsg(String msg) {
        if (mFooter == null)
            init();
        isLoading = false;
        mFooter.setFootMessage(msg);
    }

    public void hideFooter() {
        if (mFooter == null)
            return;
        mFooter.hideFooter();
    }

    /**
     * 取数组中最大值
     *
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

    public interface onLoadListener {
        void onLoadNextPage(int nextPage);
    }
}
