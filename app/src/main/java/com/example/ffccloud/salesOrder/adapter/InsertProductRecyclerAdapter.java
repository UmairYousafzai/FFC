package com.example.ffccloud.salesOrder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.databinding.InsertProductCardBinding;

import java.util.List;

public class InsertProductRecyclerAdapter extends RecyclerView.Adapter<InsertProductRecyclerAdapter.ProductViewHolder> {


    private LayoutInflater layoutInflater;
    private List<InsertProductModel> productModelList;
    private final Fragment fragment;

    public InsertProductRecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        InsertProductCardBinding binding = InsertProductCardBinding.inflate(layoutInflater,parent,false);

        return new ProductViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        InsertProductModel productModel;
        productModel = productModelList.get(position);

        holder.mBinding.setProduct(productModel);
        holder.mBinding.executePendingBindings();



    }

    @Override
    public int getItemCount() {
        return productModelList==null? 0:productModelList.size();
    }





    public  void setProductModelList(List<InsertProductModel> list)
    {
        if (list!=null)
        {
            productModelList = list;
        }
        else
        {
            productModelList.clear();
        }
        notifyDataSetChanged();
    }



    public  class ProductViewHolder extends RecyclerView.ViewHolder
    {
        InsertProductCardBinding mBinding;

        public ProductViewHolder(@NonNull InsertProductCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;



        }
    }
}
