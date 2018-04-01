package com.example.mustafa.exchange;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class fragmentPhone extends Fragment {

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_phone,container,false);

         textView=view.findViewById(R.id.fragmentPhone);
        return view;
    }
}
