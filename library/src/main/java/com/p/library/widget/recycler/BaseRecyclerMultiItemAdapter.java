package com.p.library.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jiang on 2016/5/11.
 * <p/>
 */
public abstract class BaseRecyclerMultiItemAdapter<ITEM> extends BaseRecyclerAdapter<ITEM> {

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, getDataList().get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        return new BaseViewHolder(view);
    }


    protected abstract int getLayoutId(int viewType);

    protected abstract int getItemViewType(int position, ITEM item);

    @Override
    protected int getLayoutId() {
        return 0;
    }

}
