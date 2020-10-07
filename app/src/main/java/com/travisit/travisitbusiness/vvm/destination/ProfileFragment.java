package com.travisit.travisitbusiness.vvm.destination;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.data.Const;
import com.travisit.travisitbusiness.databinding.FragmentHomeBinding;
import com.travisit.travisitbusiness.databinding.FragmentProfileBinding;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.utils.ChosenAction;
import com.travisit.travisitbusiness.utils.FileType;
import com.travisit.travisitbusiness.utils.PathUtil;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.OffersVM;
import com.travisit.travisitbusiness.vvm.vm.ProfileVM;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_IMAGE_LOGO = 125;
    private FragmentProfileBinding binding;
    public SharedPrefManager preferences;
    private ProfileVM vm;
    private String logoPath = "";
    private Business user = null;
    private boolean isEditMode = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new SharedPrefManager(getActivity());
        vm = ViewModelProviders.of(this).get(ProfileVM.class);
        user = preferences.getUser();
        if (user == null) {
            vm.getProfile();
            vm.businessMutableLiveData.observe(getActivity(), new Observer<Business>() {
                @Override
                public void onChanged(Business business) {
                    if (business != null) {
                        user = business;
                        preferences.saveUser(business);
                        updateUI(true);
                    }
                }
            });
        } else {
            updateUI(true);
        }
        switchMode();
        handleUserInteractions(view);

    }

    private void handleUserInteractions(View view) {
        binding.fProfileTvChangeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_IMAGE_LOGO);
            }
        });
        binding.fProfileIvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("smth", "clicked");
                showMenu(v);
            }
        });
    }

    private void updateUI(Boolean changePhoto) {
        if (changePhoto) {
            String userPhotoPath = Const.IMAGES_SERVER_ADDRESS + user.getLogo();
            binding.fProfileSdvLogo.setImageURI(Uri.parse(userPhotoPath));
        }
        binding.fProfileTvBusinessName.setText(user.getName());
        binding.fProfileEtBusinessName.setText(user.getName());
        binding.fProfileTietBusinessEmail.setText(user.getEmail());
        binding.fProfileTietGvmntNo.setText(user.getGovernmentIssuedNumber());
        binding.fProfileTvNoOfBranches.setText(user.getBranchesCount());
        binding.fProfileTvNoOfOffers.setText(user.getOffersCount());
    }

    private void switchMode() {
        if (isEditMode) {
            binding.fProfileTietBusinessEmail.setCursorVisible(true);
            binding.fProfileTietBusinessEmail.setFocusable(true);
            binding.fProfileTietBusinessEmail.setFocusableInTouchMode(true);
            binding.fProfileTietGvmntNo.setCursorVisible(true);
            binding.fProfileTietGvmntNo.setFocusable(true);
            binding.fProfileTietGvmntNo.setFocusableInTouchMode(true);
            binding.fProfileEtBusinessName.setVisibility(View.VISIBLE);
            binding.fProfileTvBusinessName.setVisibility(View.INVISIBLE);
            binding.fProfileTvChangeLogo.setVisibility(View.VISIBLE);
        } else {
            binding.fProfileTietBusinessEmail.setCursorVisible(false);
            binding.fProfileTietBusinessEmail.setFocusable(false);
            binding.fProfileTietBusinessEmail.setFocusableInTouchMode(false);
            binding.fProfileTietGvmntNo.setCursorVisible(false);
            binding.fProfileTietGvmntNo.setFocusable(false);
            binding.fProfileTietGvmntNo.setFocusableInTouchMode(false);
            binding.fProfileEtBusinessName.setVisibility(View.INVISIBLE);
            binding.fProfileTvBusinessName.setVisibility(View.VISIBLE);
            binding.fProfileTvChangeLogo.setVisibility(View.GONE);
        }
    }

    private void pickImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri thumbnail = data.getData();
            PathUtil pathUtil = new PathUtil(getActivity());
            String path = pathUtil.getPath(thumbnail);
            Log.d("Profile", "Selected Image path: " + path);

            if (requestCode == REQUEST_IMAGE_LOGO) {
                logoPath = path;
                binding.fProfileSdvLogo.setImageURI(thumbnail);
            }
        }
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);

        if (isEditMode) {
            Log.d("smth", "edt");
            popup.inflate(R.menu.menu_edit_profile_options);

        } else {
            Log.d("smth", "prf");
            popup.inflate(R.menu.menu_profile_options);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_option:
                        //TODO Change Selected Categories
                        vm.editProfile(
                                getFieldText("name"),
                                getFieldText("email"),
                                getFieldText("government issued number")
                        );
                        vm.profileMutableLiveData.observe(getActivity(), new Observer<JsonObject>() {
                            @Override
                            public void onChanged(JsonObject jsonObject) {
                                user.setName(getFieldText("name"));
                                user.setEmail(getFieldText("email"));
                                user.setGovernmentIssuedNumber(getFieldText("government issued number"));
                                preferences.saveUser(user);
                                isEditMode = false;
                                switchMode();
                                updateUI(false);
                            }
                        });
                        if (logoPath != "") {
                            Log.d("hereCCC", logoPath);
                            vm.uploadFile(logoPath, FileType.LOGO);
                            vm.fileLMutableLiveData.observe(getActivity(), new Observer<Business>() {
                                @Override
                                public void onChanged(Business business) {
                                    Log.d("hereCCC", business.getLogo());
                                    logoPath = "";
                                    user.setLogo(business.getLogo());
                                    preferences.saveUser(user);
                                }
                            });
                        }
                        return true;
                    case R.id.cancel_option:
                        isEditMode = false;
                        switchMode();
                        logoPath = "";
                        return true;
                    case R.id.edit_option:
                        isEditMode = true;
                        switchMode();
                        logoPath = "";
                        return true;
                    default:
                        return false;
                }
            }
        });

        Log.d("smth", "tshow");
        popup.show();
    }

    private String getFieldText(String fieldName) {
        switch (fieldName) {
            case "name":
                return binding.fProfileEtBusinessName.getText().toString();
            case "email":
                return binding.fProfileTietBusinessEmail.getText().toString();
            case "government issued number":
                return binding.fProfileTietGvmntNo.getText().toString();
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