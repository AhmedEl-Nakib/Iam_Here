package com.nakib.iamhere.ui.addNewTime

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakib.iamhere.model.places.PlacesResponseModel
import com.nakib.iamhere.networking.AppRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class AddNewTimeViewModel(private val serviceGeneral: AppRequests) : ViewModel() {
    var placesData = MutableLiveData<List<PlacesResponseModel>>()
    var loader = MutableLiveData<Boolean>(false)

    var attPlaceId = MutableLiveData<String>("")
    var fromTime = MutableLiveData<String>("")
    var date = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")

    fun getPlaces(context: Context) {
        try {

            loader.value = true
            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.getAllPlaces() }
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            try {
                                if (it.isSuccessful && it.body()!!.isNotEmpty()) {
                                    placesData.value = it.body()
                                    loader.value = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error " + it.message(),
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

    fun addReservation(context: Context,userId:String ) {
        try {
            if(attPlaceId.value!!.isEmpty()){
                Toast.makeText(context,"You need to select place",Toast.LENGTH_SHORT).show()
                return
            }
            if(fromTime.value!!.isEmpty()){
                Toast.makeText(context,"You need to select start time",Toast.LENGTH_SHORT).show()
                return
            }
            if(toTime.value!!.isEmpty()){
                Toast.makeText(context,"You need to select end time",Toast.LENGTH_SHORT).show()
                return
            }
            if(date.value!!.isEmpty()){
                Toast.makeText(context,"You need to select date",Toast.LENGTH_SHORT).show()
                return
            }else {
                loader.value = true
                CoroutineScope(Dispatchers.IO).async {
                    runCatching {
                        serviceGeneral.addReservation(
                            userId,
                            attPlaceId.value!!,
                            date.value!!,
                            fromTime.value!!,
                            toTime.value!!
                        )
                    }
                        .onSuccess {
                            withContext(Dispatchers.Main) {
                                try {
                                    if (it.isSuccessful && it.body()!!.isNotEmpty()) {
                                        Toast.makeText(context,it.body()!![0].save,Toast.LENGTH_SHORT).show()
                                        loader.value = false
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error " + it.message(),
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
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}