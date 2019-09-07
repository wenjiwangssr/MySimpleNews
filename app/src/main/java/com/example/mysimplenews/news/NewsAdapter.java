package com.example.mysimplenews.news;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysimplenews.R;
import com.example.mysimplenews.beans.NewsBean;
import com.example.mysimplenews.utils.ImageLoaderUtils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM=0;
    public static final int TYPE_FOOTER=1;

    private List<NewsBean> mData;
    private boolean mShowFooter=true;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public NewsAdapter(Context context){
        this.mContext=context;
    }

    public void setmData(List<NewsBean> data){
        this.mData=data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //最后一个item设置为FooterView
        if (!mShowFooter){
            return TYPE_ITEM;
        }
        if (position+1==getItemCount()){return TYPE_FOOTER;}
        else {return TYPE_ITEM;}

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_ITEM){
            View v= LayoutInflater.from(mContext).inflate(R.layout.item_news,parent,false);
            ItemViewHolder vh=new ItemViewHolder(v);
            return vh;
        }
        else{
            View v= LayoutInflater.from(mContext).inflate(R.layout.footer,parent,false);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(v);

        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;

        public ItemViewHolder(@NonNull View v) {
            super(v);
            mTitle=(TextView)v.findViewById(R.id.tvTitle);
            mDesc=(TextView)v.findViewById(R.id.tvDesc);
            mNewsImg=(ImageView)v.findViewById(R.id.ivNews);
            v.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(view,this.getPosition());
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            NewsBean news=mData.get(position);
            if (news==null)return;
            ((ItemViewHolder)holder).mTitle.setText(news.getTitle());
            ((ItemViewHolder)holder).mDesc.setText(news.getDigest());
            ImageLoaderUtils.display(mContext,((ItemViewHolder)holder).mNewsImg,news.getImgsrc());
        }
    }

    @Override
    public int getItemCount() {
        int begin=mShowFooter?0:1;
        if (mData==null)return begin;
        return mData.size()+begin;
    }
    public NewsBean getItem(int position){
        return mData==null?null:mData.get(position);
    }
    public boolean isShowFooter(){
        return this.mShowFooter;
    }
    public void isShowFooter(boolean showFooter){
        this.mShowFooter=showFooter;
    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }



    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View v) {
            super(v);
        }
    }
}
