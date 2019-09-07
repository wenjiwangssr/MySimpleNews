package com.example.mysimplenews.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysimplenews.R;
import com.example.mysimplenews.beans.ImageBean;

import com.example.mysimplenews.utils.ImageLoaderUtils;
import com.example.mysimplenews.utils.ToolsUtil;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.VH> {
    private List<ImageBean> mData;
    private Context mContext;
    private int mMaxWidth;
    private int mMaxHeight;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public ImageAdapter(Context context) {
        mContext = context;
        mMaxWidth = ToolsUtil.getWidthInPx(mContext) - 20;
        mMaxHeight = ToolsUtil.getHeightInPx(mContext) - ToolsUtil.getStatusHeight(mContext) -
                ToolsUtil.dip2px(mContext, 96);
    }

    public void setmData(List<ImageBean> data){
        this.mData=data;
        this.notifyDataSetChanged();
    }





    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        VH vh=new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {              //数据填充
        ImageBean imageBean=mData.get(position);
        if(imageBean==null)return;
        holder.mTitle.setText(imageBean.getTitle());
        float scale = (float)imageBean.getWidth() / (float) mMaxWidth;
        int height = (int)(imageBean.getHeight() / scale);
        if(height > mMaxHeight) {
            height = mMaxHeight;
        }
        holder.mImage.setLayoutParams(new LinearLayout.LayoutParams(mMaxWidth, height));
        ImageLoaderUtils.display(mContext, holder.mImage, imageBean.getThumburl());
    }

    @Override
    public int getItemCount() {
        if (mData==null)return 0;
        return mData.size();
    }

    public ImageBean getItem(int position){
        return mData==null?null:mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTitle;
        public ImageView mImage;
        public VH(@NonNull View itemView) {
            super(itemView);
            mTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            mImage=(ImageView) itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(view, this.getPosition());
        }
    }
}
