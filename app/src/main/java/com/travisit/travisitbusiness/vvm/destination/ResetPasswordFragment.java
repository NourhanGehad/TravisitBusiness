package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentForgotPasswordBinding;
import com.travisit.travisitbusiness.databinding.FragmentResetPasswordBinding;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.AuthenticationVM;


public class ResetPasswordFragment extends Fragment {
    private AuthenticationVM vm;
    private FragmentResetPasswordBinding binding;
    private SharedPrefManager preferences;
    private String code;
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            if (getFieldText("password").length() < 6 ){
                binding.fResetPasswordBtnChangePassword.setEnabled(false);
            } else {
                binding.fResetPasswordBtnChangePassword.setEnabled(true);
            }
        }
    };

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        code = ResetPasswordCodeFragmentArgs.fromBundle(getArguments()).getCode();
        if(code == null){
            preferences = new SharedPrefManager(getActivity());
            code = preferences.getPasswordResetCode();
        }
        vm = ViewModelProviders.of(this).get(AuthenticationVM.class);
        handleUserInteractions(view);
    }
    private void handleUserInteractions(final View view) {
        binding.fResetPasswordBtnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code != null){
                    if(codeIsCorrect()){
                        //TODO: Change message way
                        vm.passwordReset(code, getFieldText("password"));
                        vm.passwordResetMutableLiveData.observe(getActivity(), new Observer<JsonObject>() {
                            @Override
                            public void onChanged(JsonObject jsonObject) {
                                Log.d("here", jsonObject.get("result").getAsString());
                                Toast.makeText(getActivity(), R.string.password_changed_successfully, Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigateUp();
                            }
                        });
                    }
                } else {
                    //TODO: Change message way
                    Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.fResetPasswordTietNewPassword.addTextChangedListener(watcher);
    }
    private boolean codeIsCorrect(){
        return getFieldText("code").equals(code);
    }
    private String getFieldText(String fieldName){
        switch (fieldName){
            case "password": return binding.fResetPasswordTietNewPassword.getText().toString();
            default:
           return "invalid";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
