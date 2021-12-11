package com.example.iamhere

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iamhere.model.home.NormalHomeResponseModel
import com.example.iamhere.networking.AppRequests
import com.example.iamhere.utils.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainActivityViewModel (private val serviceGeneral: AppRequests): ViewModel() {
    var homeData = MutableLiveData<List<NormalHomeResponseModel>>()

    fun getHomeList(context: Context, userId:String) {
        try {

            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.getUserHomeNormal(userId,CommonUtils.getCurrentDate()) }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            try {
                                if (it.isSuccessful && it.body()!!.isNotEmpty()) {
                                    homeData.value = it.body()

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error data not found " + it.message(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    e.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }.onFailure {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    fun addAttendance(context: Context, userId:String,lat:String , lon:String) {
        try {

            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.addAttendance(userId,lat,lon,CommonUtils.getCurrentDate(),CommonUtils.getCurrentTime()) }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            try {
                                if (it.isSuccessful && it.body()!!.isNotEmpty() && it.body()!![0].save.lowercase() == "saved") {
                                    Toast.makeText(
                                        context,
                                        "Location Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error Add Attendance " + it.message(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    e.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }.onFailure {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}