package com.nakib.iamhere.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakib.iamhere.databinding.AdminDoctorsItemBinding
import com.nakib.iamhere.model.admin.AdminDoctorResponseModel

class HomeAdminAdapter(
    private var dataList: ArrayList<AdminDoctorResponseModel>,
    var onDoctorClicked: OnDoctorClicked
)
    : RecyclerView.Adapter<HomeAdminAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdminDoctorsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),onDoctorClicked)
    }

    fun submitList(newList: ArrayList<AdminDoctorResponseModel>){
        dataList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position] )
        holder.itemView.setOnClickListener {
            onDoctorClicked.onDoctorClicked(dataList[position])
        }
    }


    class ViewHolder(private var binding: AdminDoctorsItemBinding,var onDoctorClicked: OnDoctorClicked) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdminDoctorResponseModel) {
            binding.model = item
            binding.executePendingBindings()
            binding.editId.setOnClickListener {
                onDoctorClicked.onDoctorEditClicked(item)
            }
        }

    }

    interface OnDoctorClicked{
        fun onDoctorClicked(item : AdminDoctorResponseModel)
        fun onDoctorEditClicked(item : AdminDoctorResponseModel)

    }
}