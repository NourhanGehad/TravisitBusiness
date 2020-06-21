package com.travisit.travisitbusiness.vvm.vm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.model.forms.SignUpForm;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegistrationVM extends ViewModel {
//    public Model model = new Model();
    public MutableLiveData<Business> businessTokenLiveData = new MutableLiveData<>();
    CompositeDisposable compositeDisposable;
    public void signUpBusiness(String name, String email, String password){
        SignUpForm signUpForm = new SignUpForm(name, email, password);
        Observable<Business> observable = Client.getINSTANCE().signUp(signUpForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribe(o->businessTokenLiveData.setValue(o), e-> Log.d("PVMError",e.getMessage())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
