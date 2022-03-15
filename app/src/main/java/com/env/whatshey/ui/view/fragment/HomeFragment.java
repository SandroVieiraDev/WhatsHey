package com.env.whatshey.ui.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.env.whatshey.R;
import com.env.whatshey.base.BaseFragment;
import com.env.whatshey.ui.view.binding.HomeFragmentViewBinding;
import com.env.whatshey.util.MaskEditUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private HomeFragmentViewBinding binding;

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
        init();
        return viewRoot;
    }

    @Override
    protected void init() {
        binding.linearSend.setOnClickListener(this);
        binding.editText.addTextChangedListener(MaskEditUtil.mask(binding.editText, "(##)#####-####"));
    }

    @Override
    public void onClick(View view) {
        String textNumber = binding.editText.getText().toString();
        if(textNumber.length() < 14){
            msg("Digite um número de telefone válido");
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("whatsapp://send?phone=+55" + textNumber)));
        }
    }

    public void msg(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

}