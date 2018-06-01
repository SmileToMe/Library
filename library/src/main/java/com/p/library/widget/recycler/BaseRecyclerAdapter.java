package com.p.library.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang on 2016/4/21.
 * <p/>
 */
public abstract class BaseRecyclerAdapter<ITEM> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ITEM> dataList;

    protected OnItemClickListener<ITEM> mOnClickListener;
    private OnItemLongClickListener<ITEM> mLongClickListener;


    public BaseRecyclerAdapter() {
        dataList = new ArrayList<>();
    }

    public void setData(List<ITEM> dataList) {
        if (dataList == null) {
            clearData();
            return;
        }
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public ITEM getItem(int position) {
        return dataList.get(position);
    }

    public void insertItem(ITEM item, int position) {
        if (position >= getItemCount()) {
            position = getItemCount();
            dataList.add(item);
        } else if (position < 0) {
            position = 0;
            dataList.add(position, item);
        } else {
            dataList.add(position, item);
        }
        notifyItemInserted(position);
//        notifyDataSetChanged();
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    private int headViewCount;
    private int footerViewCount;

    public void setHeadViewCount(int headViewCount) {
        this.headViewCount = headViewCount;
    }

    public int getHeadViewCount() {
        return headViewCount;
    }

    public int getFooterViewCount() {
        return footerViewCount;
    }

    public void setFooterViewCount(int footerViewCount) {
        this.footerViewCount = footerViewCount;
    }

    public void setOnItemClickListener(OnItemClickListener<ITEM> onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<ITEM> onClickListener) {
        this.mLongClickListener = onClickListener;
    }

    public void addData(List<ITEM> dataList) {
        if (dataList == null)
            dataList = new ArrayList<>();
        int old = this.dataList.size();
        int count = dataList.size();
        this.dataList.addAll(dataList);
        notifyItemRangeInserted(old, count);
    }

    public void addNewData(ITEM item) {
        if (dataList == null)
            dataList = new ArrayList<>();
        this.dataList.add(item);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        if (mOnClickListener != null)
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionReal = baseViewHolder.getLayoutPosition() - getHeadViewCount() - getFooterViewCount();
                    mOnClickListener.onclick(positionReal,
                            dataList.get(positionReal));
                }
            });
        if (mLongClickListener != null)
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int positionReal = baseViewHolder.getLayoutPosition() - getHeadViewCount() - getFooterViewCount();
                    mLongClickListener.onclick(baseViewHolder, dataList.get(positionReal));
                    return true;
                }
            });
        bind((BaseViewHolder) holder, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataList != null && dataList.size() > 0) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    public List<ITEM> getDataList() {
        return dataList;
    }

    protected abstract int getLayoutId();


    protected abstract void bind(BaseViewHolder holder, ITEM item);

    public interface OnItemClickListener<ITEM> {
        void onclick(int position, ITEM item);
    }

    public interface OnItemLongClickListener<ITEM> {
        void onclick(BaseViewHolder holder, ITEM item);
    }
}
