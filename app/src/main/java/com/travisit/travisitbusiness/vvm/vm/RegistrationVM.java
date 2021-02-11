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
        Log.e("PVMError","ssssss");
        SignUpForm signUpForm = new SignUpForm(name, email, password);
        Observable<Business> observable = Client.getINSTANCE().signUp(signUpForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->{
                    Log.e("PVMError","ssssss");
            if (o.getError()!=null){
                Log.e("PVMError","Error");
                Toast.makeText(TravisitApp.getAppINSTANCE().getApplicationContext(),o.getError(),Toast.LENGTH_LONG).show();
            }else {
                Log.e("PVMError","Done");
                Toast.makeText(TravisitApp.getAppINSTANCE().getApplicationContext(),"Sign Up Successfully",Toast.LENGTH_LONG).show();
                businessTokenLiveData.setValue(o);
            }
        }, e-> {   Toast.makeText(TravisitApp.getAppINSTANCE().getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            Log.e("PVMError",e.getMessage().toString());}
            ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
