package org.wit.golfpoi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.wit.golfpoi.R
import org.wit.golfpoi.databinding.ActivityGolfPoiregisterBinding
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfUserModel
import timber.log.Timber
import java.time.LocalDate

class GolfPOIRegisterActivity : AppCompatActivity() {
    var user = GolfUserModel()

    lateinit var binding: ActivityGolfPoiregisterBinding
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGolfPoiregisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.btnRegister.setOnClickListener() {
            Timber.i("Check the user already exists for email address")
            val existingUser: GolfUserModel? = app.golfPOIs.findUser(binding.editTextEmail.text.toString())
            if (existingUser == null) {
                if (binding.editTextEmail.text.toString() != "" &&
                    binding.editTextFirstName.text.toString() != "" &&
                    binding.editTextLastName.text.toString() != "" &&
                    binding.editTextPassword.text.toString() != "") {
                    user.userEmail = binding.editTextEmail.text.toString()
                    user.firstName = binding.editTextFirstName.text.toString()
                    user.lastName = binding.editTextLastName.text.toString()
                    user.userPassword = binding.editTextPassword.text.toString()
                    user.lastLoginDate = LocalDate.now()
                    user.loginCount = 1
                    app.golfPOIs.createUser(user)

                    val launcherIntent = Intent(this, GolfPOIListActivity::class.java)
                    //launcherIntent.putExtra("loggedin_user", user)
                    startActivityForResult(launcherIntent, 0)
                } else {
                    Snackbar
                        .make(it, R.string.register_input_message, Snackbar.LENGTH_LONG)
                        .show()
                }

            } else {
                Snackbar
                    .make(it, R.string.register_error_message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnLogin.setOnClickListener() {
            Timber.i("Sending user to Login")

            val launcherIntent = Intent(this, GolfPOILoginActivity::class.java)
            //launcherIntent.putExtra("loggedin_user", user)
            startActivityForResult(launcherIntent, 0)

        }
    }
}