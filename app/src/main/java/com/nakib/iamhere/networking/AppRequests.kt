package com.nakib.iamhere.networking

import com.nakib.iamhere.model.addAtendanceLocation.AddAtendanceLocationResponseModel
import com.nakib.iamhere.model.addReservation.AddReservationResponseModel
import com.nakib.iamhere.model.admin.AdminDoctorResponseModel
import com.nakib.iamhere.model.home.DeleteRecordResponseModel
import com.nakib.iamhere.model.home.NormalHomeResponseModel
import com.nakib.iamhere.model.login.LoginResponseModel
import com.nakib.iamhere.model.places.PlacesResponseModel
import retrofit2.Response
import retrofit2.http.*


interface AppRequests {
    

//    @Headers("Content-Type: application/json; charset=utf-8")
//    @GET("/api/hours")
//    suspend fun getLearningLeaders(): Response<LearningLeadersResponse>
//
    @FormUrlEncoded
    @POST("Users.php?action=logIn")
    suspend fun getUserLogin(@Field("Username") Username:String ,
                                   @Field("Password") Password:String ): Response<LoginResponseModel>

    @GET("Places.php?action=getAll")
    suspend fun getAllPlaces() : Response<List<PlacesResponseModel>>

    @FormUrlEncoded
    @POST("Reservation.php?action=insert")
    suspend fun addReservation(
        @Field("userId") userId:String ,
        @Field("attPlaceId") attPlaceId:String ,
        @Field("attDate") attDate:String ,
        @Field("fromTime") fromTime:String ,
        @Field("toTime") toTime:String ,
    ) : Response<List<AddReservationResponseModel>>

    @FormUrlEncoded
    @POST("Reservation.php?action=getUserDay")
    suspend fun getUserHomeNormal(@Field("userId") userId:String,@Field("dayDate") dayDate:String) : Response<List<NormalHomeResponseModel>>


    @FormUrlEncoded
    @POST("Attendance.php?action=insert")
    suspend fun addAttendance(
        @Field("userId") userId:String ,
        @Field("latitude") latitude:String ,
        @Field("longitude") longitude:String ,
        @Field("send_date") send_date:String ,
        @Field("send_time") send_time:String ,
    ) : Response<List<AddAtendanceLocationResponseModel>>

    @GET("Reservation.php?action=delete")
    suspend fun deleteRecord(@Query("userId") userId:String , @Query("recordId") recordId:String) :Response<List<DeleteRecordResponseModel>>

    @GET("Attendance.php?action=GetUsersDayAttendance")
    suspend fun getHomeAdmin(@Query("day_date") day_date:String ) :Response<List<AdminDoctorResponseModel>>

}