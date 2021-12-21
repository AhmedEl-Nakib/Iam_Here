package com.nakib.iamhere.ui.homeNormal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakib.iamhere.databinding.NormalTableItemBinding
import com.nakib.iamhere.model.home.NormalHomeResponseModel

class HomeNormalTableAdapter(
    private var dataList: List<NormalHomeResponseModel>,
    var deleteItem: DeleteItem
    )
    : RecyclerView.Adapter<HomeNormalTableAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NormalTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),deleteItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position] )
    }


    class ViewHolder(private var binding: NormalTableItemBinding,var deleteItem: DeleteItem) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NormalHomeResponseModel) {
            binding.model = item
            binding.executePendingBindings()
            binding.deleteId.setOnClickListener {
                deleteItem.deleteItem(item)
            }
        }

    }

    interface DeleteItem{
        fun deleteItem(item : NormalHomeResponseModel)
    }

}