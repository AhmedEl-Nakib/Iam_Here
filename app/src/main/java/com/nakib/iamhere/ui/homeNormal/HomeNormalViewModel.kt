package com.nakib.iamhere.ui.homeNormal

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakib.iamhere.model.home.NormalHomeResponseModel
import com.nakib.iamhere.networking.AppRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class HomeNormalViewModel(private val serviceGeneral: AppRequests): ViewModel() {
    var homeData = MutableLiveData<List<NormalHomeResponseModel>>()
    var loader = MutableLiveData<Boolean>(false)
    var deleteRecord = MutableLiveData<Boolean>(false)
    var date = MutableLiveData<String>()

    fun getHomeList(context: Context,userId:String) {
        try {

            loader.value = true
            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.getUserHomeNormal(userId,date.value!!) }
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
                                        homeData.value = ArrayList<NormalHomeResponseModel>()
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

    fun deleteRecord(context: Context,userId:String,recordId:String) {
        try {

            loader.value = true
            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.deleteRecord(userId,recordId) }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            try {
                                if (it.isSuccessful ) {
                                    if(it.body()!!.isNotEmpty() ){
                                        if(it.body()!![0].delete == 1) {
                                            deleteRecord.value = true
                                        }else{
                                            Toast.makeText(
                                                context,
                                                "No Item Deleted",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

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