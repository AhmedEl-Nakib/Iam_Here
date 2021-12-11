package com.example.iamhere

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.iamhere.databinding.ActivityMainBinding
import com.example.iamhere.ui.login.LoginViewModel
import org.koin.android.ext.android.inject
import android.content.Intent

import android.content.IntentFilter

import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.widget.Toast

import com.example.iamhere.utils.locationService.GPSService

import android.os.Build

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import java.util.*
import com.example.iamhere.utils.locationService.UploadWorker

import androidx.work.OneTimeWorkRequest

import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.work.WorkInfo
import android.os.CountDownTimer
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    //http://masry.live/app/json/Attendance.php?action=insert // background location
    lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by inject()
    var mWorkManager: WorkManager? = null
    var constraints: Constraints? = null
    var mRequest: OneTimeWorkRequest? = null
    var timeInMin : Long = 0
    var userId : String = ""
    private val WORK_TAG = "AllowLocationBackground"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        init()
        handleObserver()
//        handleWorkManagerObserver()
    }

    private fun handleWorkManagerObserver() {
        mWorkManager!!.getWorkInfoByIdLiveData(mRequest!!.id)
            .observe(this, { workInfo ->
                if (workInfo != null) {
                    if (workInfo.outputData.getString("WorkObserver") == "Success") {
                        Log.i("BeforeService","Working")
                        startServiceOfLocation()

                        object : CountDownTimer(TimeUnit.MINUTES.toMillis(timeInMin), 60000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                stopService(Intent(this@MainActivity, GPSService::class.java))
                                for(item in viewModel.homeData.value!!.iterator()){
                                    if(!item.checked){
                                        item.checked = true
                                        timeInMin = item.preiod.toLong()
                                        startWork(item.fromTime.substring(0,2).toInt(),item.fromTime.substring(3,5).toInt())
                                        break
                                    }
                                }
                            }
                        }.start()
                    }
                    //startServiceOfLocation();
                    // Do something with progress
                }
            })
    }

    private fun handleObserver() {
        viewModel.homeData.observe(this,{
            if(it.isNotEmpty()){
                for(i in it){
                    if(!i.checked){
                        i.checked = true
                        timeInMin = i.preiod.toLong()
                        startWork(i.fromTime.substring(0,2).toInt(),i.fromTime.substring(3,5).toInt())
                        break
                    }
                }
            }
        })
//

    }

    fun getStarted(userId:String){
        this.userId = userId
        viewModel.getHomeList(this,userId)
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent =
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                startActivityForResult(intent, 0)
            }
        }

        mWorkManager = WorkManager.getInstance(this)
        constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        mWorkManager!!.cancelAllWorkByTag(WORK_TAG)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    finish()
                }
            }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION),1)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            }
        }
    }

    fun startWork(hour: Int, minute: Int) {

        mRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints!!)
            .setInitialDelay(getDiffTime(hour, minute), TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()
        mWorkManager!!.enqueue(mRequest!!)
        handleWorkManagerObserver()
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

    private fun startServiceOfLocation() {
//        GPSService.LocationInterval = 60000
        GPSService.LocationInterval = 60000
//        GPSService.LocationFastestInterval = 300000
        GPSService.LocationFastestInterval = 60000
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        GPSService.contentIntent = contentIntent
        GPSService.NotificationTitle = "your app is tracking you"
        GPSService.NotificationTxt = "Amazing Stuff"
        GPSService.drawable_small = R.drawable.ic_university_icon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, GPSService::class.java))
        } else {
            startService(Intent(this, GPSService::class.java))
            Toast.makeText(this, "Tracking Started..", Toast.LENGTH_SHORT).show()
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter("com.highbryds.tracker"))
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            val latitude = intent.getStringExtra("latitude")
            val longitude = intent.getStringExtra("longitude")
//            viewModel.addAttendance(this@MainActivity,userId,latitude!!,longitude!!)
            Log.i("latitudeMain", latitude!!)

            //            if(Double.parseDouble(message)== 37.3129483)
//            {
//                mWorkManager.cancelWorkById(mRequest.getId());
//                stopService(new Intent(WorkManagerActivity.this, GPSService.class));
//            }
//            Log.i("latitudeMain", latitude!!)

            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}