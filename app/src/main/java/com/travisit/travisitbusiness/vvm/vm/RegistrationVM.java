package com.travisit.travisitbusiness.vvm.vm;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.forms.SignUpForm;
import com.travisit.travisitbusiness.utils.TravisitApp;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegistrationVM extends ViewModel {

    public MutableLiveData<Business> businessTokenLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable;
    public void signUpBusiness(String name, String email, String password){
        SignUpForm signUpForm = new SignUpForm(name, email, password);
        Observable<Business> observable = Client.getINSTANCE().signUp(signUpForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->setBusinessToken(o), e-> Log.d("PVMError",e.fillInStackTrace().toString())));
    }
    private void setBusinessToken(Business business){
     //   Log.e("333332233232",Object.toString());
      //  Business business=new Business(Object.get(0).get("token").getAsString(),Object.get(0).get("name").getAsString(),Object.get(0).get("email").getAsString());
        if (business.getEmail()==null||business.getEmail().isEmpty()){
            Toast.makeText(TravisitApp.getAppINSTANCE().getApplicationContext(),"This Email is Already Exist",Toast.LENGTH_LONG);
        }else {
            businessTokenLiveData.setValue(business);
        }
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
