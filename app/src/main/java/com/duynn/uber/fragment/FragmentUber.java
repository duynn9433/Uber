package com.duynn.uber.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duynn.uber.R;
import com.duynn.uber.actitivy.LoginActivity;
import com.duynn.uber.actitivy.RiderActivity;
import com.duynn.uber.actitivy.ViewRequestActivity;
import com.parse.ParseUser;

public class FragmentUber extends Fragment {
    private Switch userTypeSwitch;
    private Button button;

    private String userType;

    //onClick for start button
    public void getStarted(View view) {
        //redirect to rider or driver activity
        redirectActivity();

    }

    //redirect to rider or driver activity
    public void redirectActivity() {
        if(ParseUser.getCurrentUser().get("userType").equals("Rider")){
            Intent intent = new Intent(getActivity(), RiderActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getActivity(), ViewRequestActivity.class);
            startActivity(intent);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uber, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ParseUser.getCurrentUser() == null) {
            //ve man hinh dang nhap
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        userType = (String) ParseUser.getCurrentUser().get("userType");
        button = view.findViewById(R.id.buttonStart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarted(v);
            }
        });

    }
}
