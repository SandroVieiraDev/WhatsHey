package com.env.whatshey.ui.historic;

import static com.env.whatshey.utilities.MaskEditUtils.unmask;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.env.whatshey.R;
import com.env.whatshey.databinding.FragmentHistoricBinding;
import com.env.whatshey.utilities.MaskEditUtils;

import java.util.List;
import java.util.UUID;

import br.kleberf65.androidutils.base.BaseBindingFragment;

public class HistoricFragment extends BaseBindingFragment<FragmentHistoricBinding> {
    private HistoricPreferences historicPreferences;
    private HistoricAdapter historicAdapter;
    private final int[] menuItemsIds = new int[]{R.id.menuDelete, R.id.menuSend,
            R.id.menuShare, R.id.menuDeleteAll, R.id.menuShareApp};
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
                openChat(historic.getWhatsNumber());
            }

            @Override
            public void onItemLongClick(int position, Historic historic) {
                if (isDeleteAllVisible) {
                    historic.setItemSelected(true);
                    historicAdapter.notifyItemChanged(position, historic);
                    showOptionMenu();
                }
            }
        });
        hideLoadingContentView();
    }

    private void setupInputAndSender() {
        binding.editSend.addTextChangedListener(MaskEditUtils.mask(binding.editSend,
                MaskEditUtils.FORMAT_FONE));
        binding.imgSender.setOnClickListener(view -> addToHistoric());
    }

    private void setupOnBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isDeleteAllVisible) {
                    showOptionMenu();
                    historicAdapter.cleanSelectedHistoric();
                } else {
                    this.remove();
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private void addToHistoric() {
        String number = binding.editSend.getText().toString();
        if (number.length() < 14) {
            showMessage(getString(R.string.phone_invalid_message));
        } else {
            long timeMillis = System.currentTimeMillis();
            Historic historic = new Historic(UUID.randomUUID().toString(), timeMillis,
                    Historic.TYPE_CHAT_RIGHT, number);
            historicPreferences.addToHistoric(historic);
            historicAdapter.addData(historic);
            binding.rvHistoric.smoothScrollToPosition(historicAdapter.getItemCount() - 1);
            openChat(number);
            binding.editSend.setText(null);
        }
    }

    private void showOptionMenu() {
        for (MenuItem menuItem : menuItems) {
            menuItem.setVisible(isDeleteAllVisible);
        }
        isDeleteAllVisible = !isDeleteAllVisible;
        menuItems[menuItems.length - 1].setVisible(isDeleteAllVisible);
        menuItems[menuItems.length - 2].setVisible(isDeleteAllVisible);
    }

    private void openChat(String number) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("whatsapp://send?phone=+55" + unmask(number))));
        } catch (ActivityNotFoundException e) {
            showMessage(e.getMessage());
        }
    }

    private void shareNumber(String number) {
        try {
            startActivity(new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, unmask(number)));
        } catch (ActivityNotFoundException e) {
            showMessage(e.getMessage());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu actionMenu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_historic, actionMenu);
        for (int i = 0; i < menuItemsIds.length; i++) {
            menuItems[i] = actionMenu.findItem(menuItemsIds[i]);
            menuItems[i].setVisible(i == menuItemsIds.length - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDeleteAll:
                historicPreferences.clear();
                historicAdapter.clean();
                break;
            case R.id.menuSend:

                break;
            case R.id.menuShare:

                break;
            case R.id.menuDelete:

        }

        return super.onOptionsItemSelected(item);
    }
}
