package com.example.buynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.buynotes.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class EmptyNotesFragment extends Fragment {

    public static final String TAG = "EmptyNotesFragment";
    public static final String AUTH_RESULT = "AUTH_RESULT";
    public static final String EMAIL = "EMAIL";
    public static final int AUTH_REQUEST_ID = 1;

    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;

    public static EmptyNotesFragment newInstance() {
        Bundle args = new Bundle();

        EmptyNotesFragment fragment = new EmptyNotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), options);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_empty_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInButton = view.findViewById(R.id.signin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, AUTH_REQUEST_ID);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_REQUEST_ID) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            Bundle args = new Bundle();
            args.putString(EMAIL, accountTask.getResult().getEmail());
            getParentFragmentManager().setFragmentResult(AUTH_RESULT, args);
            signInButton.setVisibility(View.GONE);

            try {
                GoogleSignInAccount account = accountTask.getResult();
            } catch (Exception ex) {
                Toast.makeText(requireContext(), "Auth Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
