package com.travisit.travisitbusiness.vvm.vm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.forms.EmailForm;
import com.travisit.travisitbusiness.model.forms.ResetPasswordForm;
import com.travisit.travisitbusiness.model.forms.SignInForm;
import com.travisit.travisitbusiness.model.forms.SignUpForm;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AuthenticationVM extends ViewModel {
    public MutableLiveData<Business> businessMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<JsonObject> newCodeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<JsonObject> codeValidationMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<JsonObject> passwordResetMutableLiveData = new MutableLiveData<>();

    CompositeDisposable compositeDisposable;
    public void signInBusiness(String email, String password){
        SignInForm signInForm = new SignInForm(email, password);
        Observable<Business> observable = Client.getINSTANCE().signIn(signInForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->businessMutableLiveData.setValue(o), e-> Log.d("PVMError",e.getMessage())));
    }

    public void RequestNewPasswordCode(String email){
        EmailForm emailForm = new EmailForm(email);
        Observable<JsonObject> observable = Client.getINSTANCE().requestResetPasswordCode(emailForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->newCodeMutableLiveData.setValue(o), e-> Log.d("PVMError",e.getMessage())));
    }

    public void validateResetCode(String resetCode){
        Observable<JsonObject> observable = Client.getINSTANCE().confirmResetCode(resetCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->codeValidationMutableLiveData.setValue(o), e-> Log.d("PVMError",e.getMessage())));
    }

    public void passwordReset(String resetCode, String newPassword){
        ResetPasswordForm resetPasswordForm = new ResetPasswordForm(resetCode, newPassword);
        Observable<JsonObject> observable = Client.getINSTANCE().resetPassword(resetPasswordForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->passwordResetMutableLiveData.setValue(o), e-> Log.d("PVMError",e.getMessage())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
