package com.example.movie_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movie_app.models.LoginResponse;
import com.example.movie_app.models.RegisterResponse;
import com.example.movie_app.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private AuthRepository repository;

    public AuthViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<LoginResponse> performLogin(String email, String password) {
        return repository.login(email, password);
    }

    public LiveData<RegisterResponse> performRegister(String name, String email, String password) {
        return repository.register(name, email, password);
    }

}