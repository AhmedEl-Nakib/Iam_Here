package com.example.iamhere.ui.homeNormal

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.iamhere.databinding.NormalTableItemBinding
import com.example.iamhere.model.home.NormalHomeResponseModel

class HomeNormalTableAdapter(
    private var dataList: List<NormalHomeResponseModel>
    )
    : RecyclerView.Adapter<HomeNormalTableAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NormalTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position] )
    }


    class ViewHolder(private var binding: NormalTableItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NormalHomeResponseModel) {
            binding.model = item
            binding.executePendingBindings()

        }

    }

}