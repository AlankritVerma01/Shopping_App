package com.example.b07_project;

import static org.mockito.Mockito.*;

import com.example.b07_project.mvp_interface.LoginPageContract;
import com.example.b07_project.presenter.LoginPagePresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class LoginPagePresenterTest {
    @Mock
    LoginPageContract.View view;
    @Mock
    LoginPageContract.Model model;
    @InjectMocks
    LoginPagePresenter presenter;
    @Captor
    ArgumentCaptor<Runnable> modelOnSuccess;
    @Captor
    ArgumentCaptor<Consumer<Exception>> modelOnFailure;

    /**
     * Tests whether error is displayed and login is not attempted when email is formatted incorrectly
     */
    @Test
    public void testInvalidEmailError() {
        when(model.verifyEmailFormat(any())).thenReturn(false);
        presenter.onLoginButtonClicked();
        verify(view).toastEmailFormatError();
        verify(model, never()).login(any(), any(), any(), any());
    }

    /**
     * Tests whether login is attempted when email is formatted correctly
     */
    @Test
    public void testValidEmailLogin() {
        when(model.verifyEmailFormat(any())).thenReturn(true);
        presenter.onLoginButtonClicked();
        verify(model).login(any(), any(), any(), any());
    }

    /**
     * Tests whether page switches to success page when login is successful
     */
    @Test
    public void testValidCredentialsNavigate() {
        when(model.verifyEmailFormat(any())).thenReturn(true);
        presenter.onLoginButtonClicked();
        verify(model).login(any(), any(), modelOnSuccess.capture(), any());
        modelOnSuccess.getValue().run();
        verify(view).navigateLoginSuccessPage();
    }

    /**
     * Tests whether error is displayed and page is not switched when login fails
     */
    @Test
    public void testInvalidCredentialsError() {
        when(model.verifyEmailFormat(any())).thenReturn(true);
        presenter.onLoginButtonClicked();
        verify(model).login(any(), any(), any(), modelOnFailure.capture());
        modelOnFailure.getValue().accept(any());
        verify(view).toastException(any());
        verify(view, never()).navigateRegisterPage();
    }

    /**
     * Tests whether page switches to register page when register button is clicked
     */
    @Test
    public void testRegisterButtonClicked() {
        presenter.onRegisterButtonClicked();
        verify(view).navigateRegisterPage();
    }
}
