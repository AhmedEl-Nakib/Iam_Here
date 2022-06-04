package com.nakib.iamhere

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nakib.iamhere.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import android.content.Intent

import android.content.IntentFilter

import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.widget.Toast

import com.nakib.iamhere.utils.locationService.GPSService

import android.os.Build

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import java.util.*

import java.util.concurrent.TimeUnit
import android.os.CountDownTimer
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class MainActivity : AppCompatActivity() {
    //http://masry.live/app/json/Attendance.php?action=insert // background location
    lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by inject()

    var userId : String = ""
    lateinit var navController : NavController
    lateinit var navHostFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContext(this)
        init()
        viewModel.initViewModel(this)
        handleObserver()
        setApplication(this)
        setViewModel(viewModel)
    }

//        handleWorkManagerObserver()



//    private fun handleWorkManagerObserver() {
//        mWorkManager!!.getWorkInfoByIdLiveData(mRequest!!.id)
//            .observe(this, { workInfo ->
//                if (workInfo != null) {
//                    if (workInfo.outputData.getString("WorkObserver") == "Success") {
//                        Log.i("BeforeService","Working")
//                        locationStatus.value = "StartLocation"
////
//
//                        object : CountDownTimer(TimeUnit.MINUTES.toMillis(timeInMin), 60000) {
//                            override fun onTick(millisUntilFinished: Long) {
//                            }
//
//                            override fun onFinish() {
//                                locationStatus.value = "StopLocation"
////                                stopService(Intent(this@MainActivity, GPSService::class.java))
//                                for(item in homeData.value!!.iterator()){
//                                    if(!item.checked){
//                                        item.checked = true
//                                        timeInMin = item.preiod.toLong()
//                                        startWork(item.fromTime.substring(0,2).toInt(),item.fromTime.substring(3,5).toInt())
//                                        break
//                                    }
//                                }
//                            }
//                        }.start()
//                    }
//                    //startServiceOfLocation();
//                    // Do something with progress
//                }
//            })
//    }

    private fun handleObserver() {
        viewModel.homeData.observe(this,{
            if(it.isNotEmpty()){
                for(i in it){
                    if(!i.checked){
                        i.checked = true
                        viewModel.timeInMin = i.preiod.toLong()
                        viewModel.startWork(i.fromTime.substring(0,2).toInt(),i.fromTime.substring(3,5).toInt())
                        break
                    }
                }
            }
        })
        viewModel.locationStatus.observe(this,{
            if(it){
                viewModel.outputWorkInfos.observe(this,{ work ->
                    if (work != null) {
                        if (work.outputData.getString("WorkObserver") == "Success") {
                            Log.i("BeforeService","Working")
//                        locationStatus.value = "StartLocation"
//                            startServiceOfLocation()

                            object : CountDownTimer(TimeUnit.MINUTES.toMillis(viewModel.timeInMin), 60000) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
//                                locationStatus.value = "StopLocation"
                                    stopService(Intent(this@MainActivity, GPSService::class.java))
//                                stopService(Intent(this@MainActivity, GPSService::class.java))
                                    for(item in viewModel.homeData.value!!.iterator()){
                                        if(!item.checked){
                                            item.checked = true
                                            viewModel.timeInMin = item.preiod.toLong()
                                            viewModel.startWork(item.fromTime.substring(0,2).toInt(),item.fromTime.substring(3,5).toInt())
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

                viewModel.locationStatus.value = false
            }
        })
//

    }

    fun getStarted(userId:String){
        this.userId = userId
        setUserId(userId)
        viewModel.getHomeList(this,userId)
    }

    private fun init() {

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

    private fun startServiceOfLocation() {
//        GPSService.LocationInterval = 60000
        GPSService.LocationInterval = 300000
//        GPSService.LocationFastestInterval = 300000
        GPSService.LocationFastestInterval = 300000
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

    companion object {
        private lateinit var mApplication: MainActivity
        private lateinit var mViewModel: MainActivityViewModel
        private lateinit var mUserId: String
        private lateinit var mCtx: Context
        fun setApplication(application: MainActivity) {
            mApplication = application
        }
        fun setViewModel(viewModel: MainActivityViewModel) {
            mViewModel = viewModel
        }
        fun setUserId(userId: String) {
            mUserId = userId
        }
        fun setContext(context: Context) {
            mCtx = context
        }
        fun startServiceCompanion(){
            GPSService.LocationInterval = 300000
//        GPSService.LocationFastestInterval = 300000
            GPSService.LocationFastestInterval = 300000
            val contentIntent = PendingIntent.getActivity(
                mCtx, 0,
                Intent(mCtx, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
            )
            GPSService.contentIntent = contentIntent
            GPSService.NotificationTitle = "your app is tracking you"
            GPSService.NotificationTxt = "Amazing Stuff"
            GPSService.drawable_small = R.drawable.ic_university_icon
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mCtx.startForegroundService(Intent(mCtx, GPSService::class.java))
            } else {
                mCtx.startService(Intent(mCtx, GPSService::class.java))
                Toast.makeText(mCtx, "Tracking Started..", Toast.LENGTH_SHORT).show()
            }
            LocalBroadcastManager.getInstance(mCtx)
                .registerReceiver(mMessageReceiver, IntentFilter("com.highbryds.tracker"))

            CoroutineScope(Dispatchers.Main).async {
                startCounter()
            }
        }

        private suspend fun startCounter() {
            object : CountDownTimer(TimeUnit.MINUTES.toMillis(mViewModel.timeInMin), 60000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i("onTick",(millisUntilFinished / 60000).toString());
                }

                override fun onFinish() {
//                                locationStatus.value = "StopLocation"
                    mCtx.stopService(Intent(mCtx, GPSService::class.java))
//                                stopService(Intent(this@MainActivity, GPSService::class.java))
                    for(item in mViewModel.homeData.value!!.iterator()){
                        if(!item.checked){
                            item.checked = true
                            mViewModel.timeInMin = item.preiod.toLong()
                            mViewModel.startWork(item.fromTime.substring(0,2).toInt(),item.fromTime.substring(3,5).toInt())
                            break
                        }
                    }
                }
            }.start()
        }

        private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                // Get extra data included in the Intent
                val latitude = intent.getStringExtra("latitude")
                val longitude = intent.getStringExtra("longitude")
                mViewModel.addAttendance(mApplication.applicationContext,mUserId,latitude!!,longitude!!)
//                Log.i("latitudeMain", latitude!!)

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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        navHostFragment = supportFragmentManager.findFragmentById(R.id.navGraphHome) as NavHostFragment
//        navController = navHostFragment.navController
//
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.home_menu, menu)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if(destination.id == R.id.splashFragment ||destination.id == R.id.privacyPolicyFragment ||destination.id == R.id.loginFragment ) {
//
//            } else{
//
//            }
//        }
//        return true
//    }
}