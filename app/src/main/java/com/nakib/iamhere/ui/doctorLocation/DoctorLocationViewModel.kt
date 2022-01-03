package com.nakib.iamhere.ui.doctorLocation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakib.iamhere.model.doctorLocation.DoctorLocationResponseModel
import com.nakib.iamhere.model.home.NormalHomeResponseModel
import com.nakib.iamhere.networking.AppRequests
import com.nakib.iamhere.utils.CommonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DoctorLocationViewModel(private val serviceGeneral: AppRequests) : ViewModel() {
    var doctorLocationData = MutableLiveData<List<DoctorLocationResponseModel>>()
    var loader = MutableLiveData<Boolean>(false)
//    var date = MutableLiveData<String>()

    fun getDoctorLocationList(context: Context, userId:String,date:String) {
        try {

            loader.value = true
            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.getDoctorLocations(userId,date) }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            try {
                                if (it.isSuccessful ) {
                                    if(it.body()!!.isNotEmpty()){
                                        doctorLocationData.value = it.body()
                                        loader.value = false
                                    }else{
//                                        Toast.makeText(
//                                            context,
//                                            "No Data",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                        doctorLocationData.value = ArrayList<DoctorLocationResponseModel>()
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