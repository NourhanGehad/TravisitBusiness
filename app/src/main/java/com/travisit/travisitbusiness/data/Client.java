package com.travisit.travisitbusiness.data;

import android.content.Context;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.model.forms.EditProfileForm;
import com.travisit.travisitbusiness.model.forms.EmailForm;
import com.travisit.travisitbusiness.model.forms.ResetPasswordForm;
import com.travisit.travisitbusiness.model.forms.SignInForm;
import com.travisit.travisitbusiness.model.forms.SignUpForm;
import com.travisit.travisitbusiness.utils.SharedPrefManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static OkHttpClient okHttpClient;
    private Services services;
    private static Client INSTANCE;

    private static OkHttpClient initHttpClient(String token){
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(Const.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Const.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Const.REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json");
                     /*   .addHeader("Accept", "application/json")*/
                if (token != null)
                requestBuilder.addHeader("Authorization", "JWT "+token);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    public Client() {
        if(okHttpClient == null){
            reinstantiateClient(null);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.DEFAULT_SERVER_ADDRESS)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        services = retrofit.create(Services.class);
    }

    public static Client getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Client();
        }
        return INSTANCE;
    }

    public static void reinstantiateClient(String token) {
        okHttpClient = initHttpClient(token);
        INSTANCE = new Client();
    }
    public Observable<Business> signUp(SignUpForm userData) {
        return services.signUpBusiness(userData);
    }

    public Observable<Business> signIn(SignInForm userData) {
        return services.signInBusiness(userData);
    }

    public Observable<JsonObject> requestResetPasswordCode(EmailForm email) {
        return services.sendResetPasswordCode(email);
    }

    public Observable<JsonObject> confirmResetCode(String resetCode) {
        return services.confirmResetCode(resetCode);
    }

    public Observable<JsonObject> resetPassword(ResetPasswordForm newPassword) {
        return services.resetPassword(newPassword);
    }

    public Observable<JsonObject> editProfile(EditProfileForm userData) {
        return services.editProfile(userData);
    }
    public Observable<JsonObject> getCategories() {
        return services.getCategories();
    }

    public Observable<Business> changeBusinessLogo(MultipartBody.Part logoFile) {
        return services.changeBusinessLogo(logoFile);
    }

    public Observable<Business> uploadIssuedNumberLogo(MultipartBody.Part issuedNumberFile) {
        return services.uploadIssuedNumberLogo(issuedNumberFile);
    }

    public Observable<JsonObject> getOffers(String startDate, String endDate) {
        return services.getOffers(startDate, endDate);
    }

    public Observable<JsonObject> addOffer(Offer offer) {
        return services.addOffer(offer);
    }

    public Observable<JsonObject> updateOffer(Offer offer) {
        return services.updateOffer(offer.getId(), offer);
    }

    public Observable<JsonObject> uploadOfferPhotos(MultipartBody.Part photoFile) {
        return services.uploadOfferPhotos(photoFile);
    }

    public Observable<JsonObject> getBranches() {
        return services.getBranches();
    }

    public Observable<JsonObject> addBranch(Branch branch) {
        return services.addBranch(branch);
    }

}
