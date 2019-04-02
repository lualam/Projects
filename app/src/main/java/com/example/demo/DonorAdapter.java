package com.example.demo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder>{

    private List<DonorModel> list;
    private Activity mActivity;
    private OnClickSelectListener mListener;

    public interface OnClickSelectListener {

        void onPhoneButtonClicked(String phone);
    }


    public DonorAdapter(Activity mActivity, List<DonorModel> list, OnClickSelectListener listener) {
        this.list = list;
        this.mActivity = mActivity;

        mListener = listener;
    }

    public void notifyChange(List<DonorModel> newList) {
        list.clear();
        list.addAll(newList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final DonorModel donor = list.get(position);
        holder.tvName.setText(donor.getName());
        holder.tvEmail.setText(donor.getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textView11)
        TextView tvName;
        @BindView(R.id.textView13)
        TextView tvEmail;
        @BindView(R.id.imageView4)
        ImageView btnphone;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btnphone.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == btnphone){
                if (mListener != null) {
                    mListener.onPhoneButtonClicked(list.get(getAdapterPosition()).getPhone());
                }
            }
        }
    }
}
