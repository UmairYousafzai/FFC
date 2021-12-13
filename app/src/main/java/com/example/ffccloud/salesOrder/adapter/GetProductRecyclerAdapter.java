package com.example.ffccloud.salesOrder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.GetProductModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.databinding.GetProductCardBinding;
import com.example.ffccloud.salesOrder.AddProductFragmentDirections;
import com.example.ffccloud.utils.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetProductRecyclerAdapter extends RecyclerView.Adapter<GetProductRecyclerAdapter.ProductViewHolder> implements Filterable {


    private LayoutInflater layoutInflater;
    private List<GetProductModel> productModelList,productModelListFull;
    private final Fragment fragment;
    private int key=0, supplierID=0;
    private String date="";

    public GetProductRecyclerAdapter(Fragment fragment) {
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

        GetProductCardBinding binding = GetProductCardBinding.inflate(layoutInflater,parent,false);

        return new ProductViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        GetProductModel productModel = new GetProductModel();
        productModel = productModelList.get(position);

        holder.mBinding.setProduct(productModel);
        holder.mBinding.executePendingBindings();



    }

    @Override
    public int getItemCount() {
        return productModelList==null? 0:productModelList.size();
    }



    public void setKey(int key)
    {
        this.key= key;
    }

    public void setSupplierID(int supplierID)
    {
        this.supplierID= supplierID;
    }

    public void setDate(String date)
    {
        this.date= date;
    }
    public  void setProductModelList(List<GetProductModel> list)
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


            List<GetProductModel> filterList= new ArrayList<>();

            if (constraint==null||constraint.length()==0)
            {
                filterList= productModelListFull;
            }
            else {
                String filterPattern= constraint.toString().toLowerCase().trim();
                for (GetProductModel model:productModelListFull)
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
        GetProductCardBinding mBinding;

        public ProductViewHolder(@NonNull GetProductCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
            mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InsertProductModel productModel = new InsertProductModel();

                    int unitID= (int) productModelList.get(getAdapterPosition()).getUnitId();

                    productModel.setRate((long)productModelList.get(getAdapterPosition()).getSaleRate());
                    productModel.setUnit_Id(unitID);
                    productModel.setTitleProduct(productModelList.get(getAdapterPosition()).getTitle());
                    productModel.setUnitProduct(productModelList.get(getAdapterPosition()).getUnit());
                    productModel.setItem_Code((int)productModelList.get(getAdapterPosition()).getItCode());
                    NavController navController = NavHostFragment.findNavController(fragment);

                    if (key==1)
                    {
                        Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set(CONSTANTS.PRODUCT_MODEL,productModel);
                        navController.popBackStack();
                    }
                    else {
                        mBinding.btnAdd.setEnabled(false);
                        AddProductFragmentDirections.ActionAddProductFragmentToProductInfoBottomSheetDialogFragment action = AddProductFragmentDirections.actionAddProductFragmentToProductInfoBottomSheetDialogFragment(productModel);
                       action.setDate(date);
                       action.setSupplierId(supplierID);
                        navController.navigate(action);
                    }


                }
            });


        }
    }
}
