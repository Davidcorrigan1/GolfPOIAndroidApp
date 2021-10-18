package org.wit.golfpoi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.golfpoi.R
import org.wit.golfpoi.databinding.ActivityGolfPoiloginBinding
import org.wit.golfpoi.main.MainApp
import org.wit.golfpoi.models.GolfUserModel
import timber.log.Timber.i

class GolfPOILoginActivity : AppCompatActivity() {

    var user = GolfUserModel()

    lateinit var app: MainApp
    private lateinit var binding: ActivityGolfPoiloginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGolfPoiloginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.btnLogin.setOnClickListener() {
            i("Check the user exists and check password")
            var loggedInUser: GolfUserModel? = app.golfPOIData.findUser(binding.editTextEmail.text.toString())

            if (loggedInUser != null && (loggedInUser.userPassword.equals(binding.editTextPassword.text.toString()))) {

                app.golfPOIData.setCurrentUser(loggedInUser)
                val launcherIntent = Intent(this, GolfPOIListActivity::class.java)
                //launcherIntent.putExtra("loggedin_user", user)
                startActivityForResult(launcherIntent, 0)

            } else {
                Snackbar
                    .make(it, R.string.login_error_message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnRegister.setOnClickListener() {
            i("Sending user to register")

            val launcherIntent = Intent(this, GolfPOIRegisterActivity::class.java)
            //launcherIntent.putExtra("loggedin_user", user)
            startActivityForResult(launcherIntent, 0)

        }
    }

}