package com.nakib.iamhere.model.admin

data class AdminDoctorResponseModel(
    val Depatrment: String,
    val DrName: String,
    val Title: String,
    val att_date: String,
    val latitude: String,
    val longitude: String,
    val timing: String,
    val userId: String
){
    companion object{
        fun getList(): ArrayList<AdminDoctorResponseModel> {
            var list = ArrayList<AdminDoctorResponseModel>()
            list.add(AdminDoctorResponseModel("X","Ahmed","IT","2021","30.1364368","31.3734138","",""))
            return list
        }
    }
}