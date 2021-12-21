package com.nakib.iamhere

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.nakib.iamhere.model.home.NormalHomeResponseModel
import com.nakib.iamhere.networking.AppRequests
import com.nakib.iamhere.utils.CommonUtils
import com.nakib.iamhere.utils.locationService.UploadWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

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
//                                    Toast.makeText(
//                                        context,
//                                        "Location Saved",
//                                        Toast.LENGTH_SHORT
//                                    ).show()

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


    var mWorkManager: WorkManager? = null
    var constraints: Constraints? = null
    var mRequest: OneTimeWorkRequest? = null
    private val WORK_TAG = "AllowLocationBackground"
    var timeInMin : Long = 0
    var locationStatus = MutableLiveData<Boolean>(false)
    lateinit var outputWorkInfos: LiveData<WorkInfo>


    fun initViewModel(context: Context){
        mWorkManager = WorkManager.getInstance(context)
        constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        mWorkManager!!.cancelAllWorkByTag(WORK_TAG)
    }

    private fun getDiffTime(hour: Int, minute: Int): Long {
        val calendar: Calendar = Calendar.getInstance()
        val nowMillis: Long = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis - nowMillis
    }

    fun startWork(hour: Int, minute: Int) {

        mRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints!!)
            .setInitialDelay(getDiffTime(hour, minute), TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()
        mWorkManager!!.enqueue(mRequest!!)
        outputWorkInfos = mWorkManager!!.getWorkInfoByIdLiveData(mRequest!!.id)
//        locationStatus.value = true
//        handleWorkManagerObserver()
    }


}