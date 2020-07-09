package com.example.chapter2.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter2.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TestData> mDataset = new ArrayList<>();
    private IOnItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIndex;
        private TextView tvName;
        private TextView tvMessage;
        private ImageView ivImage;
        private TextView tvTime;
        private View contentView;

        public MyViewHolder(View v) {
            super(v);
            contentView = v;
            tvIndex = v.findViewById(R.id.tv_index);
            tvName = v.findViewById(R.id.tv_name);
            tvMessage = v.findViewById(R.id.tv_message);
            ivImage = v.findViewById((R.id.iv_image));
            tvTime = v.findViewById(R.id.tv_time);
        }

        public static int getResId(String variableName, Class<?> c) {
            try {
                Field idField = c.getDeclaredField(variableName);
                return idField.getInt(idField);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        public void onBind(int position, TestData data) {
            tvIndex.setText(new StringBuilder().append(position).toString());
            tvName.setText(data.name);
            tvMessage.setText(data.message);
            int id = MyViewHolder.getResId(data.picture, R.drawable.class);
            ivImage.setImageResource(id);
            tvTime.setText(data.time);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                contentView.setOnClickListener(listener);
            }
        }
    }

    public MyAdapter(List<TestData> myDataset) {
        mDataset.addAll(myDataset);
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.onBind(position+1, mDataset.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemCLick(position+1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface IOnItemClickListener {
        void onItemCLick(int position);
    }
}
