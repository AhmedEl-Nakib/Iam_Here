package com.example.iamhere.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iamhere.model.login.LoginResponseModel
import com.example.iamhere.networking.AppRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class LoginViewModel(private val serviceGeneral: AppRequests) : ViewModel() {

    var userData = MutableLiveData<LoginResponseModel>()
    var loader = MutableLiveData<Boolean>(false)
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    fun getUserData(context: Context) {
        try {
            if(email.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter email", Toast.LENGTH_SHORT).show()
                return
            }
            if(password.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter password", Toast.LENGTH_SHORT).show()
                return
            }
            else {
                loader.value = true
                CoroutineScope(Dispatchers.IO).async {
                    runCatching { serviceGeneral.getUserLogin(email.value!!, password.value!!) }
                        .onSuccess {
                            withContext(Dispatchers.Main) {
                                try {
                                    if (it.isSuccessful && it.body()!!.userId!!.isNotEmpty()) {
                                        userData.value = it.body()
                                        loader.value = false
                                    }
                                    else if(it.isSuccessful && it.code() != 200){
                                        Toast.makeText(
                                            context,
                                            "Invalid email of password" ,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        loader.value = false
                                    }
                                    else {
                                        Toast.makeText(
                                            context,
                                            "Invalid email of password\n${it.message()}",
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