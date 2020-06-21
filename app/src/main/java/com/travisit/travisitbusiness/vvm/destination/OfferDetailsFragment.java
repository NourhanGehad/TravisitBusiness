package com.travisit.travisitbusiness.vvm.destination;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.travisit.travisitbusiness.R;
import com.travisit.travisitbusiness.databinding.FragmentOfferDetailsBinding;
import com.travisit.travisitbusiness.vvm.AppActivity;

public class OfferDetailsFragment extends Fragment {
    FragmentOfferDetailsBinding binding;
    public OfferDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppActivity) getActivity()).changeBottomNavVisibility(View.GONE);
        binding = FragmentOfferDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleUserInteractions();
    }

    private void handleUserInteractions() {
        binding.fOfferDetailsIvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_option:
                        Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete_option:
                        Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_options);
        popup.show();
    }

}