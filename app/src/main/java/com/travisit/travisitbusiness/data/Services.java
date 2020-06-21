package com.travisit.travisitbusiness.data;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.model.forms.EditProfileForm;
import com.travisit.travisitbusiness.model.forms.EmailForm;
import com.travisit.travisitbusiness.model.forms.ResetPasswordForm;
import com.travisit.travisitbusiness.model.forms.SignInForm;
import com.travisit.travisitbusiness.model.forms.SignUpForm;

public interface Services {
    //Authentication & Registration
    @POST("business/signup")
    Observable<Business> signUpBusiness(@Body SignUpForm userData);

    @POST("business/login")
    Observable<Business> signInBusiness(@Body SignInForm userData);

    @POST("business/resetPasswordCode")
    Observable<JsonObject> sendResetPasswordCode(@Body EmailForm email);

//    @GET("business/confirmResetCode/?{resetCode}")
    @GET("business/confirmResetCode")
    Observable<JsonObject> confirmResetCode(@Query("resetCode") String resetCode);

    @POST("business/resetPassword")
    Observable<JsonObject> resetPassword(@Body ResetPasswordForm newPassword);

    //Profile
    @PUT("business/me")
    Observable<JsonObject> editProfile(@Body EditProfileForm userData);

    @Multipart
    @PUT("business/me/images")
    Observable<Business> changeBusinessLogo(@Part MultipartBody.Part logoFile);

    @Multipart
    @PUT("business/me/images")
    Observable<Business> uploadIssuedNumberLogo(@Part MultipartBody.Part issuedNumberFile);

    //Categories
    @GET("categories")
    Observable<JsonObject> getCategories();

    //Offers
    @GET("offers")
    Observable<JsonObject> getOffers(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate );

    @POST("offers")
    Observable<JsonObject> addOffer(@Body Offer offer); //TODO : Add offer attributes

    @DELETE("offers/{offer_id}")
    Observable<JsonObject> deleteOffer(@Path("offer_id") int id);

    @PUT("offers/{offer_id}")
    Observable<JsonObject> updateOffer(
            @Path("offer_id") int id,
            @Body Offer offer);

    @Multipart
    @PUT("offers/images/{offer_id}")
    Observable<JsonObject> uploadOfferPhotos(@Part MultipartBody.Part photo);

    //branches
    @GET("branches")
    Observable<JsonObject> getBranches();

    @POST("branches")
    Observable<JsonObject> addBranch(@Body Branch branch);

}
