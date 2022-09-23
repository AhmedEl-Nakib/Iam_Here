package com.nakib.iamhere.ui.updatePassword

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakib.iamhere.networking.AppRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class UpdatePasswordViewModel (private val serviceGeneral: AppRequests) : ViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var oldPassword = MutableLiveData<String>("")
    var newPassword = MutableLiveData<String>("")

    fun updatePassword(context: Context,userId:String , drName:String) {
        try {
            if(oldPassword.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter old password", Toast.LENGTH_SHORT).show()
                return
            }
            if(newPassword.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter new password", Toast.LENGTH_SHORT).show()
                return
            }
            else {
                loader.value = true
                CoroutineScope(Dispatchers.IO).async {
                    runCatching { serviceGeneral.updatePassword(userId,drName,oldPassword.value!!, newPassword.value!!) }
                        .onSuccess {
                            withContext(Dispatchers.Main) {
                                try {
                                    if (it.isSuccessful ) {
                                        Toast.makeText(context,it.body()!!.message.toString(),Toast.LENGTH_SHORT).show()
                                        loader.value = false
                                    }

                                    else {
                                        Toast.makeText(
                                            context,
                                            "${it.message()}",
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
        }catch (e:Exception){
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}