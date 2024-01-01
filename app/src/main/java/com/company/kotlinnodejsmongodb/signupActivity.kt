package com.company.kotlinnodejsmongodb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.company.kotlinnodejsmongodb.Retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class signupActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null
    private val BASE_URL = "http://192.168.0.104:7000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInterface = retrofit!!.create(RetrofitInterface::class.java)

        handleSignupDialog()
    }

    private fun handleSignupDialog() {
        val signupButton = findViewById<Button>(R.id.signupButton)
        val nameSignup = findViewById<EditText>(R.id.nameSignup)
        val emailSignup = findViewById<EditText>(R.id.emailSignup)
        val passwordSignup = findViewById<EditText>(R.id.passwordSignup)

        signupButton.setOnClickListener {
            val map: HashMap<String?, String?>? = hashMapOf(
                "name" to nameSignup.text.toString(),
                "email" to emailSignup.text.toString(),
                "password" to passwordSignup.text.toString()
            )

            val call: Call<Void?>? = retrofitInterface?.executeSignup(map)

            call?.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    when (response.code()) {
                        200 -> {
                            Toast.makeText(this@signupActivity, "Signed up successfully", Toast.LENGTH_LONG).show()
                            // Start loginActivity upon successful signup
                            val intent = Intent(this@signupActivity, loginActivity::class.java)
                            startActivity(intent)
                            finish() // Finish the current activity to prevent going back to it using the back button
                        }

                        400 -> Toast.makeText(this@signupActivity, "Already registered", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(this@signupActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

