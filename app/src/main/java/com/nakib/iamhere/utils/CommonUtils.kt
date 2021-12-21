package com.nakib.iamhere.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Patterns
import androidx.annotation.LayoutRes
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {
    val DATE_PICKER_DAY_FORMAT = SimpleDateFormat("EEE d", Locale.ENGLISH)
    val DATE_PICKER_YYYY_MM_DD_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val DATE_PICKER_MMMM_YYYY_FORMAT = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val DATE_PICKER_24H_FORMAT =
        SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val DATE_PICKER_HH_MM_A_FORMAT = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showLoadingDialog(context: Context, @LayoutRes resId: Int): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.apply {
            setContentView(resId)
            isIndeterminate = true
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        return progressDialog
    }

    fun createImageFolder(context: Context): File {
        val storageDir = File(context.filesDir, "Photo")
        if (!storageDir.exists())
            storageDir.mkdirs()
        return storageDir
    }

    fun shareImage(uri: Uri, context: Context, text: String?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        // sharingIntent.type = "image/*"
        sharingIntent.type = "*/*"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(sharingIntent, "Share Image Using"))
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            UUID.randomUUID().toString() + ".png",
            "drawing"
        )
        return Uri.parse(path)
    }

    fun loadJSONFromAsset(
        context: Context,
        jsonFileName: String
    ): String {
        val manager = context.assets
        val `is`: InputStream = manager.open(jsonFileName)
        val size: Int = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        return String(buffer)
    }

    fun getAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt
    }

    fun getMonthAndYearFrom_yyyy_mm_dd(date: String): String? {
        return DATE_PICKER_MMMM_YYYY_FORMAT.format(DATE_PICKER_YYYY_MM_DD_FORMAT.parse(date)!!)
    }

    fun getDAY_FORMATFrom_yyyy_mm_dd(date: String): String? {
        return DATE_PICKER_DAY_FORMAT.format(DATE_PICKER_YYYY_MM_DD_FORMAT.parse(date)!!)
    }

    fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        return DATE_PICKER_YYYY_MM_DD_FORMAT.format(date)
    }

    fun getCurrentTime(): String {
        val date = Calendar.getInstance().time
        return DATE_PICKER_24H_FORMAT.format(date)
    }

    fun parse24HourToDate(time: String): Date {
        return DATE_PICKER_24H_FORMAT.parse(time)!!
    }

    fun getDateBeforeOrAfter(date: String, days: Int): String {
        val myDate = DATE_PICKER_YYYY_MM_DD_FORMAT.parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = myDate!!
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return DATE_PICKER_YYYY_MM_DD_FORMAT.format(calendar.time)
    }

    fun getTime_AM_PM_From_HH_MM(date: String): String {
        return DATE_PICKER_HH_MM_A_FORMAT.format(parse24HourToDate(date))
    }

    fun getTIME_AMPM(date: String): String {
        var spf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.ENGLISH)
        val newDate = spf.parse(date)

        spf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return spf.format(newDate!!)
    }

    fun convertTimeStampToDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy", calendar).toString()
        return date
    }

    fun convertTimeStampToDate_YYYY_MM_DD(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("yyyy-MM-dd", calendar).toString()
        return date
    }

    fun convertTimeStampToDate_MMMM_dd_yyyy_Format(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("MMMM dd, yyyy", calendar).toString()
        return date
    }

    fun convertTimeStampToDate_EEE_dd_mm_yyyy_Format(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format("EEEE dd/MM/yyyy", calendar).toString()
    }

    fun getDayOfTheWeek(date: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatted = format.parse(date)
        return DateFormat.format("EEEE", dateFormatted) as String
    }

    fun convertTimeStampToDate_dd_mm_hh_mm_a_Format(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format("dd MMM hh:mm a", calendar).toString()
    }
}
