package com.travisit.travisitbusiness.vvm.destination;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentOfferDetailsBinding;
import com.travisit.travisitbusiness.databinding.FragmentOfferManagementBinding;
import com.travisit.travisitbusiness.model.Branch;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.utils.ManagementOption;
import com.travisit.travisitbusiness.utils.PathUtil;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.vm.BranchesVM;
import com.travisit.travisitbusiness.vvm.vm.OffersVM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class
OfferManagementFragment extends Fragment {
    private static final int REQUEST_FIRST_IMAGE = 127;
    private static final int REQUEST_SECOND_IMAGE = 128;
    private static final int REQUEST_THIRD_IMAGE = 129;
    private String firstImagePath = "";
    private String secondImagePath = "";
    private String thirdImagePath = "";
    private boolean isNew = true;
    FragmentOfferManagementBinding binding;
    Offer offer;
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
                    getFieldText("description").length() == 0 ||
                    getFieldText("start date").length() == 0 ||
                    getFieldText("end date").length() == 0 ) {
                binding.fOfferManagementMtbtnSave.setEnabled(false);
                binding.fOfferManagementMtbtnSave.setAlpha(0.5f);
            } else {
                binding.fOfferManagementMtbtnSave.setEnabled(true);
                binding.fOfferManagementMtbtnSave.setAlpha(1f);
            }
        }
    };
    private OffersVM vm;
    private BranchesVM branchesVM;
    private ArrayList<Integer> branchesIDs = new ArrayList<>();
    private ArrayList<String> branchesNames = new ArrayList<>();
    private int selectedBranchIndex;
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();
    String myFormat = "yyyy-MMM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
    final DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startDateCalendar.set(Calendar.YEAR, year);
            startDateCalendar.set(Calendar.MONTH, monthOfYear);
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.fOfferManagementTietStartDate.setText(sdf.format(startDateCalendar.getTime()));
        }
    };
    final DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, monthOfYear);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.fOfferManagementTietEndDate.setText(sdf.format(endDateCalendar.getTime()));
        }
    };
    public OfferManagementFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = FragmentOfferManagementBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = ViewModelProviders.of(this).get(OffersVM.class);
        branchesVM = ViewModelProviders.of(this).get(BranchesVM.class);
        offer = OfferManagementFragmentArgs.fromBundle(getArguments()).getOffer();
        if(offer == null){
            updateUI(ManagementOption.ADD);
            isNew = true;
        } else {
            updateUI(ManagementOption.EDIT);
            isNew = false;
        }
        handleUserInteractions(view);
    }
    private void handleUserInteractions(View view) {
        binding.fOfferManagementSdv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_FIRST_IMAGE);
            }
        });
        binding.fOfferManagementSdv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_SECOND_IMAGE);
            }
        });
        binding.fOfferManagementSdv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(REQUEST_THIRD_IMAGE);
            }
        });

        binding.fOfferManagementTietBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBranchIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.fOfferManagementMtbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offer == null){
                    createOfferObject();
                    vm.addOffer(offer);
                } else {
                    uploadPhotos(view);
                    offer.setTitle(getFieldText("name"));
                    offer.setDescription(getFieldText("description"));
                    offer.setStartDate(getFieldText("start date"));
                    offer.setEndDate(getFieldText("end date"));
                    offer.setBranchID(Integer.parseInt(getFieldText("branch id")));
                    vm.editOffer(offer);
                }
                vm.offerMutableLiveData.observe(getActivity(), new Observer<Offer>() {
                    @Override
                    public void onChanged(Offer offer) {
                        if(isNew){
                            uploadPhotos(view);
                        }
                    }
                });
            }
        });

        binding.fOfferManagementTietStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), startDatePickerListener, startDateCalendar
                        .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                        startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.fOfferManagementTietEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), endDatePickerListener, endDateCalendar
                        .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.fOfferManagementTietTitle.addTextChangedListener(watcher);
        binding.fOfferManagementTietDescription.addTextChangedListener(watcher);
        binding.fOfferManagementTietStartDate.addTextChangedListener(watcher);
        binding.fOfferManagementTietEndDate.addTextChangedListener(watcher);
    }
    private void createOfferObject() {
        offer = new Offer(
                getFieldText("title"),
                getFieldText("description"),
                getFieldText("start date"),
                getFieldText("end date"),
                vm.selectedTags,
                Integer.parseInt(getFieldText("branch id"))
        );
    }
    private void updateUI(ManagementOption option) {
        branchesVM.getBranches();
        branchesVM.branchesMutableLiveData.observe(getActivity(), new Observer<ArrayList<Branch>>() {
            @Override
            public void onChanged(ArrayList<Branch> branches) {
                for(Branch branch : branches){
                    branchesIDs.add(branch.getId());
                    branchesNames.add(branch.getName());
                }
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(
                                getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                branchesNames);
                binding.fOfferManagementTietBranch.setAdapter(adapter);
            }
        });

        switch(option){
            case ADD:{
                binding.fOfferManagementTvTitle.setText(getString(R.string.offer_add_header));
                binding.fOfferManagementMtbtnSave.setText(getString(R.string.add_now));
                break;
            }
            case EDIT: {
                binding.fOfferManagementTvTitle.setText(getString(R.string.offer_edit_header));
                binding.fOfferManagementMtbtnSave.setText(getString(R.string.update_now));
                binding.fOfferManagementTietTitle.setText(offer.getTitle());
                binding.fOfferManagementTietDescription.setText(offer.getDescription());
                binding.fOfferManagementTietStartDate.setText(offer.getStartDate());
                binding.fOfferManagementTietEndDate.setText(offer.getEndDate());
                break;
            }
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
    private void uploadPhotos(View view){
        vm.uploadFiles(firstImagePath,secondImagePath,thirdImagePath);
        vm.photosMutableLiveData.observe(getActivity(), new Observer<Offer>() {
            @Override
            public void onChanged(Offer offer) {
                Navigation.findNavController(view).navigateUp();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri thumbnail = data.getData();
            PathUtil pathUtil = new PathUtil(getActivity());
            String path = pathUtil.getPath(thumbnail);

            if (requestCode == REQUEST_FIRST_IMAGE) {
                firstImagePath = path;
                binding.fOfferManagementSdv1.setImageURI(thumbnail);
            } else if (requestCode == REQUEST_SECOND_IMAGE) {
                secondImagePath = path;
                binding.fOfferManagementSdv2.setImageURI(thumbnail);
            } else if (requestCode == REQUEST_THIRD_IMAGE) {
                thirdImagePath = path;
                binding.fOfferManagementSdv3.setImageURI(thumbnail);
            }
        }
    }
    private String getFieldText(String fieldName) {
        switch (fieldName) {
            case "name":
                return binding.fOfferManagementTietTitle.getText().toString();
            case "description":
                return binding.fOfferManagementTietDescription.getText().toString();
            case "start date":
                return binding.fOfferManagementTietStartDate.getText().toString();
            case "end date":
                return binding.fOfferManagementTietEndDate.getText().toString();
            case "branch id":
                return branchesIDs.get(selectedBranchIndex).toString();
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