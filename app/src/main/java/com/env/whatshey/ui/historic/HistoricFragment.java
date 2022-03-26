package com.env.whatshey.ui.historic;

import static com.env.whatshey.utilities.MaskEditUtils.unmask;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.env.whatshey.R;
import com.env.whatshey.databinding.FragmentHistoricBinding;
import com.env.whatshey.utilities.MaskEditUtils;
import com.env.whatshey.utilities.OrientationUtils;

import java.util.List;
import java.util.UUID;

import br.kleberf65.androidutils.ads.InterstitialAds;
import br.kleberf65.androidutils.base.BaseBindingFragment;

public class HistoricFragment extends BaseBindingFragment<FragmentHistoricBinding> {
    private HistoricPreferences historicPreferences;
    private HistoricAdapter historicAdapter;
    private final int[] menuItemsIds = new int[]{R.id.menuDelete, R.id.menuSend,
            R.id.menuShare, R.id.menuDeleteAll, R.id.menuShareApp};
    private final MenuItem[] menuItems = new MenuItem[menuItemsIds.length];
    private boolean isDeleteAllVisible = true;
    private InterstitialAds interstitialAds;
    private LinearLayoutManager linearLayoutManager;
    private OrientationUtils orientationUtils;
    private Historic historicSelected;

    public int getLayout() {
        return R.layout.fragment_historic;
    }

    @Override
    public void initializeUi() {

        initializeVariables();
        setupInputAndSender();
        setupOnBackPressedCallback();
        setupLoadHistoricRecyclerView();
        setupMonetization();
        setHasOptionsMenu(true);
    }

    private void initializeVariables() {
        historicPreferences = new HistoricPreferences(requireContext());
        historicAdapter = new HistoricAdapter(requireContext());
         linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        orientationUtils = new OrientationUtils(context, linearLayoutManager);
        //Hide toolbar here...
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().show();
        }
    }

    private void setupLoadHistoricRecyclerView() {
        List<Historic> historicList = historicPreferences.loadHistoric();

        if (orientationUtils.isLastVisibleItemPosition())
            linearLayoutManager.setStackFromEnd(true);
        else
            binding.rvHistoric.addOnScrollListener(orientationUtils);

        binding.rvHistoric.setLayoutManager(linearLayoutManager);
        binding.rvHistoric.setAdapter(historicAdapter);
        binding.rvHistoric.setHasFixedSize(true);

        historicAdapter.addData(historicList);
        hideLoadingContentView();

        historicAdapter.setHistoricClickListener(new HistoricAdapter.ClickListener() {
            @Override
            public void onItemClick(Historic historic) {
                if(!isDeleteAllVisible){
                    showOptionMenu();
                } else {
                    openChat(historic.getWhatsNumber());
                }
            }

            @Override
            public void onItemLongClick(int position, Historic historic) {
                historicSelected = historic;
                if(isDeleteAllVisible) {
                    showOptionMenu();
                }
            }
        });


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
            binding.editSend.setText(null);
            openChat(number);
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
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, unmask(number)), number));
        } catch (ActivityNotFoundException e) {
            showMessage(e.getMessage());
        }
    }

    private void shareApp(){
        String uri = ("com.env.whatshey");

        try {
            android.content.pm.PackageInfo pi = requireContext()
                    .getPackageManager()
                    .getPackageInfo(uri, android.content.pm.PackageManager.GET_ACTIVITIES);
            String apk = pi.applicationInfo.publicSourceDir;

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());

            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                    .setType("apk application/vnd.android.package-archive")
                    .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(apk))), "Whats Hey"));

        } catch (Exception e) {
            showMessage(e.toString());
        }
    }

    private void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setTitle("Deseja apagar esta conversa?")
                .setMessage("Ao apagar a conversa você apagará todo o histórico")
                .setNegativeButton("CANCELAR", (dialogInterface, i) -> {

                })
                .setPositiveButton("APAGAR CONVERSA", (dialogInterface, i) -> {
                    historicPreferences.clear();
                    historicAdapter.clean();
                    orientationUtils.clear();
                    setupLoadHistoricRecyclerView();
                }).create().show();
    }

    private void setupMonetization() {
        // TODO: 25/03/2022 implemntar anuncios
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu actionMenu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_historic, actionMenu);
        for (int i = 0; i < menuItemsIds.length; i++) {
            menuItems[i] = actionMenu.findItem(menuItemsIds[i]);
            menuItems[i].setVisible(false);
        }
        menuItems[menuItemsIds.length - 1].setVisible(true);
        menuItems[menuItemsIds.length - 2].setVisible(true);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDeleteAll:
                showDialog();
                break;
            case R.id.menuShareApp:
                shareApp();
                break;
            case R.id.menuSend:
                openChat(historicSelected.getWhatsNumber());
                showOptionMenu();
                break;
            case R.id.menuShare:
                shareNumber(historicSelected.getWhatsNumber());
                showOptionMenu();
                break;
            case R.id.menuDelete:
                historicAdapter.deleteItem(historicSelected);
                showOptionMenu();
        }

        return super.onOptionsItemSelected(item);
    }
}
