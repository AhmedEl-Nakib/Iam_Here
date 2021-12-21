package com.nakib.iamhere.ui.admin

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakib.iamhere.model.admin.AdminDoctorResponseModel
import com.nakib.iamhere.networking.AppRequests
import com.nakib.iamhere.utils.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class AdminViewModel(private val serviceGeneral: AppRequests) : ViewModel()  {

    var homeData = MutableLiveData<List<AdminDoctorResponseModel>>()
    var loader = MutableLiveData<Boolean>(false)
    var date = MutableLiveData<String>()
    var searchWord = MutableLiveData<String>("")

    fun getHomeList(context: Context) {
        try {

            loader.value = true
            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.getHomeAdmin(CommonUtils.getCurrentDate()) }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            try {
                                if (it.isSuccessful ) {
                                    if(it.body()!!.isNotEmpty()){
                                        homeData.value = it.body()
                                        loader.value = false
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "No Data",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        homeData.value = ArrayList<AdminDoctorResponseModel>()
                                        loader.value = false

                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error data not found " + it.message(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loader.value = false

                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    e.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                loader.value = false

                            }
                        }
                    }.onFailure {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                        loader.value = false
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}