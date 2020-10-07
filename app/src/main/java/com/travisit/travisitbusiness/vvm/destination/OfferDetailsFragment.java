package com.travisit.travisitbusiness.vvm.destination;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentOfferDetailsBinding;
import com.travisit.travisitbusiness.model.Offer;
import com.travisit.travisitbusiness.model.OfferComment;
import com.travisit.travisitbusiness.utils.PaginationListener;
import com.travisit.travisitbusiness.vvm.AppActivity;
import com.travisit.travisitbusiness.vvm.adapter.CommentsAdapter;
import com.travisit.travisitbusiness.vvm.vm.OffersVM;

import java.util.ArrayList;

import static com.travisit.travisitbusiness.utils.PaginationListener.PAGE_START;

public class OfferDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentOfferDetailsBinding binding;
    private Offer offer;
    private OffersVM vm;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetControl;
    private ConstraintLayout bottomSheet;
    private Handler handler;
    private int currentPageNumber = 1;
    private CommentsAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPages = 100;
    private boolean isLoading = false;
    private LinearLayoutManager linearLayoutManager;
    int itemCount = 0;

    public OfferDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE, false);
        binding = FragmentOfferDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = ViewModelProviders.of(this).get(OffersVM.class);
        offer = OfferDetailsFragmentArgs.fromBundle(getArguments()).getOffer();
        if (offer == null) {
            Toast.makeText(getActivity(), getString(R.string.missing_item), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();
        }
        bottomSheet = view.findViewById(R.id.f_offer_details_comments);
        bottomSheetControl = BottomSheetBehavior.from(bottomSheet);
        bottomSheetControl.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.fOfferDetailsComments.bsSwipeRefresh.setOnRefreshListener(this);
        /*binding.fOfferDetailsComments.bsCommentsRvRecyclerView.setHasFixedSize(true);*/
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.fOfferDetailsComments.bsCommentsRvRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CommentsAdapter(new ArrayList<>());
        binding.fOfferDetailsComments.bsCommentsRvRecyclerView.setAdapter(adapter);
        /*binding.fOfferDetailsComments.bsCommentsRvRecyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                fillBottomSheet();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
*/
 /*       handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                fillBottomSheet();
            }
        });*/
        binding.fOfferDetailsComments.bsCommentsRvRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Integer totalItems = linearLayoutManager.getItemCount();
                Integer visibleItemsCount = linearLayoutManager.getChildCount();
                Integer firstVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if(firstVisibleItem + visibleItemsCount >= totalItems/2){
                    binding.fOfferDetailsComments.bsCommentsRvRecyclerView.removeOnScrollListener(this);
                    currentPageNumber++;
                    fillBottomSheet();
                }
            }
        });
        handleUserInteractions();
        updateUI();
    }

    private void fillBottomSheet() {
        //adapter.clear();
        Log.d("sayOk","currentPage: "+ currentPageNumber);
        vm.getOfferComments(offer.getId(), currentPageNumber, 15);
        vm.offerCommentsMutableLiveData.observe(getActivity(), new Observer<ArrayList<OfferComment>>() {
            @Override
            public void onChanged(ArrayList<OfferComment> offerComments) {
/*
                Log.d("hey","hey"+offerComments.size());
                if (offerComments.size() < 30) {
                    totalPages = currentPage;
                }
                if (currentPage != PAGE_START) {
                    adapter.removeLoading();
                }
                adapter.addItems(offerComments);
                binding.fOfferDetailsComments.bsSwipeRefresh.setRefreshing(false);
*/

                // check weather is last page or not
/*
                if (currentPage < totalPages) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
*/
                adapter.appendComments(offerComments);
            }


    });
}

    private void handleUserInteractions() {
        binding.fOfferDetailsIvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        binding.fOfferDetailsIvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetControl.setState(BottomSheetBehavior.STATE_EXPANDED);
                fillBottomSheet();
            }
        });
    }

    private void updateUI() {
        binding.fOfferDetailsTvTitle.setText(offer.getTitle());
        binding.fOfferDetailsTvAvailability.setText(offer.getStartDate() + getString(R.string.to) + offer.getEndDate());
        binding.fOfferDetailsTvDescription.setText(offer.getDescription());
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_option:
                        NavDirections action = OfferDetailsFragmentDirections.actionFromOfferDetailsToEditOffer().setOffer(offer);
                        Navigation.findNavController(v).navigate(action);
                        return true;
                    case R.id.delete_option:
                        showAlertDialog(offer.getId(), v);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_options);
        popup.show();
    }

    private void showAlertDialog(int offerID, View view) {
        Log.d("hereWW", String.valueOf(offerID));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.delete_alert);
        builder.setMessage(getActivity().getString(R.string.confirm_delete_message));
        builder.setPositiveButton(getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vm.deleteOffer(offerID);
                vm.deletingMutableLiveData.observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String string) {
                        if (string.equals("done")) {
                            Navigation.findNavController(view).navigateUp();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton(getActivity().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        fillBottomSheet();
    }
}