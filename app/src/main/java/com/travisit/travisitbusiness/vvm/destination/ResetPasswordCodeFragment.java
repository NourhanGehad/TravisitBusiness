package com.travisit.travisitbusiness.vvm.destination;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentForgotPasswordBinding;
import com.travisit.travisitbusiness.databinding.FragmentResetPasswordBinding;
import com.travisit.travisitbusiness.databinding.FragmentResetPasswordCodeBinding;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.AuthenticationVM;


public class ResetPasswordCodeFragment extends Fragment {
    private AuthenticationVM vm;
    private FragmentResetPasswordCodeBinding binding;
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            if (getFieldText("code").length() == 0){
                binding.fResetPasswordCodeBtnConfirmCode.setEnabled(false);
            } else {
                binding.fResetPasswordCodeBtnConfirmCode.setEnabled(true);
            }
        }
    };
    private SharedPrefManager preferences;
    private String code;
    public ResetPasswordCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = FragmentResetPasswordCodeBinding.inflate(inflater, container, false);
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
        Log.d("code", code);
        vm = ViewModelProviders.of(this).get(AuthenticationVM.class);
        handleUserInteractions(view);
    }
    private void handleUserInteractions(final View view) {
        binding.fResetPasswordCodeBtnConfirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code != null){
                    if(codeIsCorrect()){
                        //TODO: Change message way
                        Toast.makeText(getActivity(), R.string.code_confirmed, Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_from_reset_password_code_to_reset_password);
                    } else{
                        //TODO: Change message way
                        Toast.makeText(getActivity(), R.string.invalid_code, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //TODO: Change message way
                    Toast.makeText(getActivity(), R.string.invalid_code, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.fResetPasswordCodeTietCode.addTextChangedListener(watcher);

    }
    private boolean codeIsCorrect(){
        return getFieldText("code").equals(code);
    }
    private String getFieldText(String fieldName){
        switch (fieldName){
            case "code": return binding.fResetPasswordCodeTietCode.getText().toString();
            default: return "invalid";
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
