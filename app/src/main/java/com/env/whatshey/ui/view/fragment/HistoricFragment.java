package com.env.whatshey.ui.view.fragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.env.whatshey.R;
import com.env.whatshey.adapter.HistoricAdapter;
import com.env.whatshey.databinding.FragmentHistoricBinding;
import com.env.whatshey.helper.HistoricPreferences;
import com.env.whatshey.model.Historic;
import com.env.whatshey.util.MaskEditUtil;

import java.util.List;
import java.util.UUID;

import br.kleberf65.androidutils.base.BaseBindingFragment;

public class HistoricFragment extends BaseBindingFragment<FragmentHistoricBinding> {
    private HistoricPreferences historicPreferences;
    private HistoricAdapter historicAdapter;
    private final int[] menuItemsIds = new int[]{R.id.menuDelete, R.id.menuSend,
            R.id.menuShare, R.id.menuDeleteAll};
    private final MenuItem[] menuItems = new MenuItem[menuItemsIds.length];
    private boolean isDeleteAllVisible = true;


    public int getLayout() {
        return R.layout.fragment_historic;
    }

    @Override
    public void initializeUi() {

        initializeVariables();
        setupInputAndSender();
        setupOnBackPressedCallback();
        setupLoadHistoricRecyclerView();
        setHasOptionsMenu(true);
    }

    private void initializeVariables() {
        historicPreferences = new HistoricPreferences(requireContext());
        historicAdapter = new HistoricAdapter();
        //Hide toolbar here...
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().show();
        }
    }

    private void setupLoadHistoricRecyclerView() {
        binding.rvHistoric.setAdapter(historicAdapter);
        List<Historic> historicList = historicPreferences.loadHistoric();
        historicAdapter.addData(historicList);
        historicAdapter.setHistoricClickListener(new HistoricAdapter.ClickListener() {
            @Override
            public void onItemClick(Historic historic) {
                showMessage(historic.getNumber());
            }

            @Override
            public void onItemLongClick(Historic historic) {
                if (isDeleteAllVisible) showOptionMenu();
            }
        });
        hideLoadingContentView();
    }

    private void setupInputAndSender() {
        binding.editSend.addTextChangedListener(MaskEditUtil.mask(binding.editSend,
                MaskEditUtil.FORMAT_FONE));
        binding.imgSender.setOnClickListener(view -> addToHistoric());
    }

    private void setupOnBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isDeleteAllVisible) showOptionMenu();
                else {
                    this.remove();
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private void addToHistoric() {
        long timeMillis = System.currentTimeMillis();
        Historic historic = new Historic.Builder()
                .setId(UUID.randomUUID().toString())
                .setNumber(binding.editSend.getText().toString())
                .setTime(timeMillis)
                .build();
        historicPreferences.addToHistoric(historic);
        historicAdapter.addData(historic);
        binding.rvHistoric.smoothScrollToPosition(historicAdapter.getItemCount() - 1);
        binding.editSend.setText(null);

    }

    private void showOptionMenu() {
        for (MenuItem menuItem : menuItems) {
            menuItem.setVisible(isDeleteAllVisible);
        }
        isDeleteAllVisible = !isDeleteAllVisible;
        menuItems[menuItems.length - 1].setVisible(isDeleteAllVisible);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu actionMenu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, actionMenu);
        for (int i = 0; i < menuItemsIds.length; i++) {
            menuItems[i] = actionMenu.findItem(menuItemsIds[i]);
            menuItems[i].setVisible(i == menuItemsIds.length - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuDeleteAll) {
            historicPreferences.clear();
            historicAdapter.clean();
        }
        return super.onOptionsItemSelected(item);
    }
}
