package com.example.ffccloud.salesOrder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.ProductModel;
import com.example.ffccloud.databinding.ProductCardBinding;
import com.example.ffccloud.salesOrder.AddProductFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> implements Filterable {


    private LayoutInflater layoutInflater;
    private List<ProductModel> productModelList,productModelListFull;
    private final Fragment fragment;

    public ProductRecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
        productModelListFull = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ProductCardBinding binding = ProductCardBinding.inflate(layoutInflater,parent,false);

        return new ProductViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductModel productModel = new ProductModel();
        productModel = productModelList.get(position);

        holder.mBinding.setProduct(productModel);
        holder.mBinding.executePendingBindings();



    }

    @Override
    public int getItemCount() {
        return productModelList==null? 0:productModelList.size();
    }





    public  void setProductModelList(List<ProductModel> list)
    {
        if (list!=null)
        {
            productModelList = list;
            productModelListFull.addAll(productModelList);
        }
        else
        {
            productModelList.clear();
        }
        notifyDataSetChanged();
    }



    @Override
    public Filter getFilter() {
        return filter;
    }


    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            List<ProductModel> filterList= new ArrayList<>();

            if (constraint==null||constraint.length()==0)
            {
                filterList= productModelListFull;
            }
            else {
                String filterPattern= constraint.toString().toLowerCase().trim();
                for (ProductModel model:productModelListFull)
                {
                    if (model.getTitle().toLowerCase().trim().contains(filterPattern))
                    {
                        filterList.add(model);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values= filterList;
            return filterResults;



        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            productModelList.clear();
            productModelList.addAll((List)results.values);
            notifyDataSetChanged();



        }
    };


    public  class ProductViewHolder extends RecyclerView.ViewHolder
    {
        ProductCardBinding mBinding;

        public ProductViewHolder(@NonNull ProductCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
            mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(fragment);

                    NavDirections navDirections = AddProductFragmentDirections.actionAddProductFragmentToProductInfoBottomSheetDialogFragment();
                    navController.navigate(navDirections);
                }
            });


        }
    }
}
