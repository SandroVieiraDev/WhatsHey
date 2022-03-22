package com.env.whatshey.ui.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.env.whatshey.R;
import com.env.whatshey.adapter.HomeAdapter;
import com.env.whatshey.base.BaseFragment;
import com.env.whatshey.data.ServiceHey;
import com.env.whatshey.helper.DateCustom;
import com.env.whatshey.helper.HistoricPreferences;
import com.env.whatshey.helper.RecyclerItemClickListener;
import com.env.whatshey.model.Historic;
import com.env.whatshey.ui.view.binding.HomeFragmentViewBinding;
import com.env.whatshey.util.MaskEditUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, Observer {
    private final int NO_SELECTED = -1;
    private int historicSelected = 0;

    private HomeFragmentViewBinding binding;
    private ServiceHey serviceHey;

    private HomeAdapter homeAdapter;
    private List<Historic> historicList = new ArrayList<>();
    private HistoricPreferences preferences;

    private MenuItem itemDeleteAll;
    private MenuItem itemDelete;
    private MenuItem itemShare;
    private MenuItem itemSend;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        binding = new HomeFragmentViewBinding(viewRoot);
        setHasOptionsMenu(true);
        init();
        return viewRoot;
    }

    @Override
    protected void init() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();

        serviceHey = new ServiceHey();
        serviceHey.addObserver(this);
        binding.linearSend.setOnClickListener(this);
        binding.editText.addTextChangedListener(MaskEditUtil.mask(binding.editText, MaskEditUtil.FORMAT_FONE));
        binding.textDate.setText(DateCustom.getCurrentDate());

        preferences = new HistoricPreferences(getContext());
        if(preferences.loadHistoric() != null){
            historicList.addAll(preferences.loadHistoric());
        }
        homeAdapter = new HomeAdapter(historicList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(homeAdapter);

        binding.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), binding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setMenuItemVisibility(false);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                homeAdapter.setIntBackground(position);
                setHistoricSelected(position);
                setMenuItemVisibility(true);;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }
        ));

    }

    @Override
    public void onClick(View view) {
        String textNumber = binding.editText.getText().toString();
        serviceHey.addHistoric(getActivity(), textNumber);
        serviceHey.openChat(textNumber);

    }

    @Override
    public void update(Observable observable, Object o) {

        if (o instanceof String){
            msg((String) o);
        } else if (o instanceof Intent){
            startActivity((Intent) o);
        } else if (o instanceof List){
            homeAdapter.notifyList((List<Historic>) o);
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

        itemDeleteAll = menu.findItem(R.id.menuDeleteAll);
        itemDelete = menu.findItem(R.id.menuDelete);
        itemShare = menu.findItem(R.id.menuShare);
        itemSend = menu.findItem(R.id.menuSend);

        setMenuItemVisibility(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSend:
                serviceHey.openChat(getContext(), historicSelected);
                break;
            case R.id.menuShare:
                serviceHey.shareNumber(getContext(), historicSelected);
                break;
            case R.id.menuDelete:
                serviceHey.deleteHistoric(getContext(), historicSelected);
                setMenuItemVisibility(false);
                break;
            case R.id.menuDeleteAll:
                showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Deseja apagar esta conversa?")
                .setMessage("Ao apagar a conversa você apagará todo o histórico")
                .setNegativeButton("CANCELAR", (dialogInterface, i) -> {

                })
                .setPositiveButton("APAGAR CONVERSA", (dialogInterface, i) -> {
                    serviceHey.deleteAll(getContext());
                    setMenuItemVisibility(false);
                }).create().show();
    }

    private void setHistoricSelected(int position){
        this.historicSelected = position;
    }

    private void setMenuItemVisibility(boolean visible){
        if (!visible){
            setHistoricSelected(NO_SELECTED);
            homeAdapter.setIntBackground(NO_SELECTED);
        }
        itemDeleteAll.setVisible(visible);
        itemDelete.setVisible(visible);
        itemShare.setVisible(visible);
        itemSend.setVisible(visible);
    }

    private void msg(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }
}