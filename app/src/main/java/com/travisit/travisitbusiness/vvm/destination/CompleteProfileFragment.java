package com.travisit.travisitbusiness.vvm.destination;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.travisit.travisitbusiness.data.Client;
import com.travisit.travisitbusiness.databinding.FragmentCompleteProfileBinding;
import com.travisit.travisitbusiness.model.Business;
import com.travisit.travisitbusiness.utils.FileType;
import com.travisit.travisitbusiness.vvm.adapter.CategoriesAdapter;
import com.travisit.travisitbusiness.model.Category;
import com.travisit.travisitbusiness.utils.PathUtil;
import com.travisit.travisitbusiness.utils.SharedPrefManager;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.observer.BaseBackPressedListener;
import com.travisit.travisitbusiness.vvm.observer.IOnBackPressed;
import com.travisit.travisitbusiness.vvm.vm.ProfileVM;

import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;


public class CompleteProfileFragment extends Fragment {
    private static final int REQUEST_IMAGE_LOGO = 125;
    private static final int REQUEST_IMAGE_GIN = 126;
    private static final int REQUEST_READ = 127;
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
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
            Client.reinstantiateClient(
                    user.getToken()
            );
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

                    vm.editProfile(getFieldText("name"),
                            getFieldText("email"),
                            getFieldText("government issued number"));
                    vm.profileMutableLiveData.observe(getActivity(), new Observer<JsonObject>() {
                        @Override
                        public void onChanged(JsonObject jsonObject) {
                            //TODO: Move the user to the waiting for approval screen
                            /*56656546564546564564546546565465465464565446*/
                            //vm.getProfile();
//                            final NavController navController=Navigation.findNavController(view);
//                            CompleteProfileFragmentDirections.ActionFromCompleteProfileToShowAccountStatus actionToShowStatusFragment= CompleteProfileFragmentDirections.actionFromCompleteProfileToShowAccountStatus().setIsVerified(false);
//                            navController.navigate(actionToShowStatusFragment);
                        }
                    });
//                    vm.businessMutableLiveData.observe(getActivity(), new Observer<Business>() {
//                        @Override
//                        public void onChanged(Business business) {
//                            preferences.saveUser(business);
//                        }
//                    });
                    if (!logoPath.equals("") && !gviPath.equals("")) {
                        Boolean hasReadPermission = isReadStoragePermissionGranted();
                        if(hasReadPermission){
                            vm.uploadFiles(logoPath,gviPath);
                            vm.photosMutableLiveData.observe(getActivity(), new Observer<Business>() {
                                @Override
                                public void onChanged(Business business) {
                                    Log.d("YOU", "DID IT");
                                    preferences.saveUser(business);
                                    final NavController navController=Navigation.findNavController(view);
                                    CompleteProfileFragmentDirections.ActionFromCompleteProfileToShowAccountStatus actionToShowStatusFragment= CompleteProfileFragmentDirections.actionFromCompleteProfileToShowAccountStatus().setIsVerified(false);
                                    navController.navigate(actionToShowStatusFragment);
                                }
                            });
                            vm.editProfile(getFieldText("name"),
                                    getFieldText("email"),
                                    getFieldText("government issued number"));
                          /*  vm.profileMutableLiveData.observe(getActivity(), new Observer<JsonObject>() {
                                @Override
                                public void onChanged(JsonObject jsonObject) {
                                    //TODO TODO TODO
                                    try {
                                        Thread.sleep(350);
                                        //((AppActivity)getActivity()).setOnBackPressedListener(null);
                                       // Navigation.findNavController(view).navigate(R.id.action_from_complete_profile_to_home);
                                        Navigation.findNavController(view).navigate(R.id.action_from_complete_profile_to_home);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });*/
                        } else {
                            //REQUEST
                            //isReadStoragePermissionGranted();
                        }

                    } else {
                        //TODO: Tell the user to insert both images
                        Toast.makeText(getContext(), "Please Select Images", Toast.LENGTH_SHORT).show();
                    }
                    if (!logoPath.equals("")) {
                       /* Bitmap bitmap = getBitmapFromPath(logoPath);
                        Bitmap compressedBitmap = compressImage(bitmap);*/
//                        vm.uploadFile(logoPath, getActivity(), FileType.LOGO);
                        vm.uploadFile(logoPath, FileType.LOGO);
                        vm.fileLMutableLiveData.observe(getActivity(), new Observer<Business>() {
                            @Override
                            public void onChanged(Business business) {
                                preferences.saveUser(business);
                                Log.d("businessXXXXX", business.getLogo());
                            }
                        });
                    }
                    if (!gviPath.equals("")) {
//                        vm.uploadFile(gviPath, getActivity(), FileType.GOVERNMENT_ISSUED_NUMBER);
                        vm.uploadFile(gviPath, FileType.GOVERNMENT_ISSUED_NUMBER);
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
                    Toast.makeText(getContext(), "Please Select Some Categories", Toast.LENGTH_SHORT).show();
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
        binding.fCompleteProfileIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

/*
    private Bitmap getBitmapFromPath(String path) {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd+path, String.valueOf(Math.random()));
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
    }
*/

    /*private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
        int options = 90;
        while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
            baos.reset();//Reset baos is empty baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
            options -= 10;//Every time reduced by 10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
        return BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation
    }*/
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
            } else if (requestCode == REQUEST_READ) {
                Toast.makeText(getActivity(),"granted read", Toast.LENGTH_LONG).show();
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
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("AppActivity","Permission is granted");
                return true;
            } else {
                Log.v("AppActivity","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("AppActivity","Permission is granted1");
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
