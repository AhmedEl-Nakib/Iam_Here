package com.example.iamhere.ui.homeNormal

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iamhere.model.home.NormalHomeResponseModel
import com.example.iamhere.model.places.PlacesResponseModel
import com.example.iamhere.networking.AppRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class HomeNormalViewModel(private val serviceGeneral: AppRequests): ViewModel() {
    var homeData = MutableLiveData<List<NormalHomeResponseModel>>()
    var loader = MutableLiveData<Boolean>(false)
    var date = MutableLiveData<String>()

    fun getHomeList(context: Context,userId:String) {
        try {

            loader.value = true
            CoroutineScope(Dispatchers.IO).async {
                runCatching { serviceGeneral.getUserHomeNormal(userId,date.value!!) }
                    .onSuccess {
                        Log.i("Resultt","0 ${it.body()!!.toString()}")
                        withContext(Dispatchers.Main) {
                            try {
                                Log.i("Resultt","1")
                                if (it.isSuccessful ) {
                                    Log.i("Resultt","2")
                                    if(it.body()!!.isNotEmpty()){
                                        homeData.value = it.body()
                                        loader.value = false
                                        Log.i("Resultt","3")
                                    }else{
                                        Toast.makeText(
                                            context,
                                            "No Data",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        loader.value = false
                                        Log.i("Resultt","4")

                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error data not found " + it.message(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loader.value = false
                                    Log.i("Resultt","5")

                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    e.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                                loader.value = false
                                Log.i("Resultt","6")

                            }
                        }
                    }.onFailure {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                        loader.value = false
                        Log.i("Resultt","7")
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}