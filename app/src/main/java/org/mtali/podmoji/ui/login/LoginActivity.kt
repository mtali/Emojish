package org.mtali.podmoji.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.mtali.podmoji.R
import org.mtali.podmoji.shared.EventObserver
import org.mtali.podmoji.theme.PodmojiTheme
import org.mtali.podmoji.ui.MainActivity
import org.mtali.podmoji.ui.components.SignInButton
import timber.log.Timber

class LoginActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_client_id))
            .requestEmail()
            .build()
    }

    // Google login receiver
    private val googleSignInReceiver =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Timber.w(e, "Google sign in failed")
                }
            } else {
                Timber.d("Sign in failed")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContent { LoginScreen(loginViewModel) }
        setObservers()
    }

    private fun setObservers() {
        loginViewModel.loginEvent.observe(this, EventObserver {
            val client = GoogleSignIn.getClient(this, gso)
            val signInIntent = client.signInIntent
            googleSignInReceiver.launch(signInIntent)
        })
    }


    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        // Navigation to MainActivity
        if (user == null) {
            Timber.w("User is null not going to navigate")
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(this) { task ->
                loginViewModel.loading(false)
                if (task.isSuccessful) {
                    Timber.d("signInWithCredentials:success")
                    val user = auth.currentUser
                    updateUI(user = user)
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
                    Timber.w(task.exception, "signInWithCredentials:failure")
                    updateUI(user = null)
                }
            }
    }
}

@Composable
private fun LoginScreen(vm: LoginViewModel = viewModel()) {
    PodmojiTheme {
        val isLoading = vm.isLoading.observeAsState(false)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignInButton(
                text = "Sign in with Google",
                loadingText = "Signing in...",
                isLoading = isLoading.value,
                icon = painterResource(id = R.drawable.ic_google_icon),
                onClick = {
                    vm.onClickLogin()
                }
            )
        }
    }
}