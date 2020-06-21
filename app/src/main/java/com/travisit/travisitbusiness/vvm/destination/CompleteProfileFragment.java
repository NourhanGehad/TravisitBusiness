package com.travisit.travisitbusiness.vvm.destination;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travisit.travisitbusiness.databinding.FragmentCompleteProfileBinding;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.vvm.adapter.CategoriesAdapter;
import com.travisit.travisitbusiness.model.Category;
import com.travisit.travisitbusiness.utils.FileType;
import com.travisit.travisitbusiness.utils.PathUtil;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.ProfileVM;

import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;


public class CompleteProfileFragment extends Fragment {
    private static final int REQUEST_IMAGE_LOGO = 125;
    private static final int REQUEST_IMAGE_GIN = 126;
    private ProfileVM vm;
    private FragmentCompleteProfileBinding binding;
    public SharedPrefManager preferences;
    private String logoPath = "";
    private String gviPath = "";
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (getFieldText("name").length() == 0 ||
                    getFieldText("email").length() == 0 ||
                    getFieldText("government issued number").length() == 0) {
                binding.fCompleteProfileBtnSubmit.setEnabled(false);
            } else {
                binding.fCompleteProfileBtnSubmit.setEnabled(true);
            }
        }
    };
    private Business user;

    public CompleteProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE);
        binding = FragmentCompleteProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = new SharedPrefManager(getActivity());
        vm = ViewModelProviders.of(this).get(ProfileVM.class);
        handleUserInteractions(view);
        user = CompleteProfileFragmentArgs.fromBundle(getArguments()).getUser();
        if (user != null) {
            updateUI();
        }
        vm.getCategories();
        vm.categoriesMutableLiveData.observe(getActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                initRecyclerView(categories);
            }
        });
//        File imgFile = new  File("/data/user/0/com.travisit.travisitbusiness/files/userfiles/FB_IMG_9002321506008079793.jpg");
//        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//          //  binding.fCompleteProfileIvGovernmentIssuedId.setImageBitmap(myBitmap);
//            binding.fCompleteProfileIvGovernmentIssuedId.setImageURI(Uri.fromFile(imgFile));
//        }

    }

    private void initRecyclerView(ArrayList<Category> categories) {
        Collections.sort(categories, Category.categoryComparator);
        binding.fCompleteProfileRvCategories.setAdapter(new CategoriesAdapter(
                categories,
                getActivity(),
                new CategoriesAdapter.SelectionPropagator() {
                    @Override
                    public void chipSelected(Category category) {
                        Log.d("businessXX", category.toString());
                        if (category.isSelected()) {
                            vm.selectedCategories.add(category.getId());
                        } else {
                            vm.selectedCategories.remove(category.getId());
                        }
                    }
                })
        );
        binding.fCompleteProfileRvCategories.setLayoutManager(new GridLayoutManager(
                getActivity(),
                3,
                RecyclerView.HORIZONTAL,
                false
        ));
    }

    private void updateUI() {
        binding.fCompleteProfileTietBusinessName.setText(user.getName());
        binding.fCompleteProfileTietEmailAddress.setText(user.getEmail());
    }

    private void handleUserInteractions(final View view) {
        binding.fCompleteProfileBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vm.selectedCategories.isEmpty()) {
/*
                    vm.editProfile(getFieldText("name"),
                            getFieldText("email"),
                            getFieldText("government issued number"));
                    vm.profileMutableLiveData.observe(getActivity(), new Observer<JsonObject>() {
                        @Override
                        public void onChanged(JsonObject jsonObject) {
                            //TODO: Move the user to the waiting for approval screen
                        }
                    });
*/

                    if (!logoPath.equals("")) {
                        vm.uploadFile(logoPath, getActivity(), FileType.LOGO);
                        vm.fileLMutableLiveData.observe(getActivity(), new Observer<Business>() {
                            @Override
                            public void onChanged(Business business) {
                                preferences.saveUser(business);
                                Log.d("businessXXXXX", business.getLogo());
                            }
                        });
                    }
                    if (!gviPath.equals("")) {
                        vm.uploadFile(gviPath, getActivity(), FileType.GOVERNMENT_ISSUED_NUMBER);
                        vm.fileGMutableLiveData.observe(getActivity(), new Observer<Business>() {
                            @Override
                            public void onChanged(Business business) {
                                preferences.saveUser(business);
                                Log.d("businessXXXXX", business.getGovernmentIssuedNumberImage());
                            }
                        });
                    }

                } else {
                    //TODO Show a message with missing categories
                }
            }
        });
        binding.fCompleteProfileCivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_IMAGE_LOGO);
            }
        });
        binding.fCompleteProfileIvGovernmentIssuedId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_IMAGE_GIN);
            }
        });
        binding.fCompleteProfileTietBusinessName.addTextChangedListener(watcher);
        binding.fCompleteProfileTietEmailAddress.addTextChangedListener(watcher);
        binding.fCompleteProfileTietGovernmentIssuedId.addTextChangedListener(watcher);
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
            Log.d("CompleteProfile", "Selected Image path: " + path);

            if (requestCode == REQUEST_IMAGE_LOGO) {
                logoPath = path;
                binding.fCompleteProfileCivLogo.setImageURI(thumbnail);
            } else if (requestCode == REQUEST_IMAGE_GIN) {
                gviPath = path;
                binding.fCompleteProfileIvGovernmentIssuedId.setImageURI(thumbnail);
            }
        }
    }

    private String getFieldText(String fieldName) {
        switch (fieldName) {
            case "name":
                return binding.fCompleteProfileTietBusinessName.getText().toString();
            case "email":
                return binding.fCompleteProfileTietEmailAddress.getText().toString();
            case "government issued number":
                return binding.fCompleteProfileTietGovernmentIssuedId.getText().toString();
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
