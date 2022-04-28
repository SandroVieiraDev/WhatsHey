package com.env.whatshey.ui.historic;

import static com.env.whatshey.utilities.AppUtils.openChatWhatsapp;
import static com.env.whatshey.utilities.AppUtils.shareApplication;
import static com.env.whatshey.utilities.AppUtils.shareNumber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionTracker;

import com.env.whatshey.R;
import com.env.whatshey.databinding.FragmentHistoricBinding;
import com.env.whatshey.utilities.MaskEditUtils;
import com.env.whatshey.utilities.Pic;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.List;

import br.kleberf65.androidutils.base.BaseBindingFragment;

public class HistoricFragment extends BaseBindingFragment<FragmentHistoricBinding> {

    private HistoricPreferences historicPreferences;
    private HistoricAdapter historicAdapter;
    private SelectionTracker<Long> selectionTracker;
    private Menu actionMenu;
    private Historic selectedHistoric;
    private RewardedAd mRewardedAd;
    private final String ID_PREMIADO = "ca-app-pub-5364358208843672/9482861905";
    private final String ID_TEST = "ca-app-pub-3940256099942544/5224354917";
    private boolean adsCompleto = true;
    //private Pic pic = new Pic(Pic.O);

    public int getLayout() {
        return R.layout.fragment_historic;
    }

    @Override
    public void initializeUi() {

        loadAd();
        initializeVariables();
        setupLoadHistoricRecyclerView();
        setupOnBackPressedCallback();
        setupInputAndSender();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!adsCompleto){
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new HistoricFragment()).commit();
        }
    }

    private void initializeVariables() {
        //Hide toolbar here...
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().show();
        }

        historicPreferences = new HistoricPreferences(requireContext(), new HistoricPreferences.HistoricListener() {
            @Override
            public void onAddedHistoric(Historic historic) {
                historicAdapter.addHistoric(historic);
                binding.rvHistoric.smoothScrollToPosition(historicAdapter.getItemCount() - 1);
            }

            @Override
            public void onDeleteHistoric(int position) {
                historicAdapter.removeHistoric(position);
            }

            @Override
            public void onAddedError() {
                showMessage(getString(R.string.phone_invalid_message));
            }
        });

    }

    private void setupLoadHistoricRecyclerView() {
        List<Historic> loadHistoric = historicPreferences.loadHistoric();

        historicAdapter = new HistoricAdapter(new HistoricAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, Historic historic) {
                rewardLoadList(historic);
            }

            @Override
            public void onItemLongClick(View view, Historic historic) {
                selectedHistoric = historic;
                showMainMenuActions(true);
            }
        });

        historicAdapter.addData(loadHistoric);
        binding.rvHistoric.setAdapter(historicAdapter);

        if(historicAdapter.getItemCount() > 2){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.rvHistoric.smoothScrollToPosition(historicAdapter.getItemCount() - 1);
                }
            }, 10);
        }

        //dismiss progressbar
        hideLoadingContentView();
    }

    private void setupInputAndSender() {
        binding.editSend.addTextChangedListener(MaskEditUtils.mask(binding.editSend,
                MaskEditUtils.FORMAT_FONE));
        binding.imgSender.setOnClickListener(view -> {
            if(!packInstalled("com.whatsapp") ||
            !packInstalled("com.whatsapp.w4b")){
                rewardLoadButton();
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
            }
        });
    }

    private boolean packInstalled(String pack){
        PackageManager pm = requireContext().getPackageManager();
        try {
            pm.getPackageInfo(pack, android.content.pm.PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void setupOnBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (historicAdapter.isItemSelectedExists()) {
                    historicAdapter.notifyItemSelectedOrClean(false, 0);
                    showMainMenuActions(false);
                } else {
                    this.remove();
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setTitle("Deseja apagar esta conversa?")
                .setMessage("Ao apagar a conversa você apagará todo o histórico")
                .setNegativeButton("CANCELAR", (dialogInterface, i) -> {

                })
                .setPositiveButton("APAGAR CONVERSA", (dialogInterface, i) -> {
                    historicPreferences.clear();
                    historicAdapter.clearAll();
                }).create().show();
    }

    private void showMainMenuActions(boolean show) {
        actionMenu.setGroupVisible(R.id.mainActions, show);
        actionMenu.setGroupVisible(R.id.othersActions, !show);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu actionMenu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_historic, actionMenu);
        this.actionMenu = actionMenu;
        showMainMenuActions(false);
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDeleteAll:
                showDialog();
                break;
            case R.id.menuShareApp:
                shareApplication(context);
                break;
            case R.id.menuSend:
                openChatWhatsapp(context, selectedHistoric.getWhatsNumber());
                break;
            case R.id.menuShare:
                shareNumber(context, selectedHistoric.getWhatsNumber());
                break;
            case R.id.menuDelete:
                historicPreferences.deleteHistoric(selectedHistoric);
        }
        historicAdapter.notifyItemSelectedOrClean(false, 0);
        showMainMenuActions(false);
        return super.onOptionsItemSelected(item);
    }

    private void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(requireContext(), ID_PREMIADO,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        loadAd();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });
    }

    private void rewardLoadButton(){
        adsCompleto = false;

        if (mRewardedAd == null) {
            adsCompleto = true;
            rewardComplete();
        } else {
            Activity activityContext = requireActivity();
            mRewardedAd.show(activityContext, rewardItem -> {
                adsCompleto = true;
                mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loadAd();
                    rewardComplete();
                }
                    });
            });
        }
    }

    private void rewardComplete(){
        String whatsNumber = binding.editSend.getText().toString();
        historicPreferences.addToHistoric(whatsNumber);
        binding.editSend.setText("");
        binding.editSend.setEnabled(false);
        binding.editSend.setEnabled(true);
    }

    private void rewardLoadList(Historic historic){
        adsCompleto = false;

        if (mRewardedAd == null) {
            rewardComplete(historic);
        } else {
            Activity activityContext = requireActivity();
            mRewardedAd.show(activityContext, rewardItem -> {
                adsCompleto = true;
                mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        loadAd();
                        rewardComplete(historic);
                    }
                });
            });
        }
    }

    private void rewardComplete(Historic historic){
        historicPreferences.addToHistoric(historic.getWhatsNumber());
    }


}
