package com.nakib.iamhere.ui.addUser

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nakib.iamhere.networking.AppRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class AddUserViewModel(private val serviceGeneral: AppRequests) : ViewModel() {

    var loader = MutableLiveData<Boolean>(false)
    var userId = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var title = MutableLiveData<String>("")
    var department = MutableLiveData<String>("")
    var phone = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var isAdmin = MutableLiveData<String>("0")

    fun addUser(context: Context) {
        try {
            if(name.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter name", Toast.LENGTH_SHORT).show()
                return
            }
            if(title.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter title", Toast.LENGTH_SHORT).show()
                return
            }
            if(department.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter department", Toast.LENGTH_SHORT).show()
                return
            }
            if(phone.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter phone", Toast.LENGTH_SHORT).show()
                return
            }
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
                    runCatching { serviceGeneral.addNewUser(name.value!!,title.value!!,department.value!!,phone.value!!,email.value!!, password.value!!,isAdmin.value!!) }
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

    fun updateUser(context: Context) {
        try {
            if(userId.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter user ID", Toast.LENGTH_SHORT).show()
                return
            }
            if(name.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter name", Toast.LENGTH_SHORT).show()
                return
            }
            if(title.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter title", Toast.LENGTH_SHORT).show()
                return
            }
            if(department.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter department", Toast.LENGTH_SHORT).show()
                return
            }
            if(phone.value!!.isEmpty()){
                Toast.makeText(context, "You need to enter phone", Toast.LENGTH_SHORT).show()
                return
            }
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
                    runCatching { serviceGeneral.updateUser(userId.value!!,name.value!!,title.value!!,department.value!!,phone.value!!,email.value!!, password.value!!,isAdmin.value!!) }
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