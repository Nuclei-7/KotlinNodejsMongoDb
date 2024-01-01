package com.company.kotlinnodejsmongodb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.company.kotlinnodejsmongodb.Retrofit.LoginResult
import com.company.kotlinnodejsmongodb.Retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class loginActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null
    private val BASE_URL = "http://192.168.0.104:7000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        try {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofitInterface = retrofit!!.create(RetrofitInterface::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the initialization failure here
            Toast.makeText(this, "Retrofit initialization failed", Toast.LENGTH_LONG).show()
        }

        handleLoginDialog()
    }
    private fun handleLoginDialog() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailLogin = findViewById<EditText>(R.id.emailLogin)
        val passwordLogin = findViewById<EditText>(R.id.passwordLogin)

        loginButton.setOnClickListener {
            val email = emailLogin.text.toString()
            val password = passwordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val paramMap = HashMap<String?, String?>()
                paramMap["email"] = email
                paramMap["password"] = password

                // Making a POST request to execute login using Retrofit
                val call: Call<LoginResult?>? = retrofitInterface?.executeLogin(paramMap)

                call?.enqueue(object : Callback<LoginResult?> {
                    override fun onResponse(call: Call<LoginResult?>, response: Response<LoginResult?>) {
                        val result = response.body()

                        if (result != null) {
                            when (response.code()) {
                                200 -> {
                                    Toast.makeText(this@loginActivity, "Logged in Successfully", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@loginActivity, homeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                404 -> {
                                    Toast.makeText(this@loginActivity, "Wrong Credentials", Toast.LENGTH_LONG).show()
                                }
                                // Handle other response codes here
                            }
                        } else {
                            Toast.makeText(this@loginActivity, "Received null response", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
                        Toast.makeText(this@loginActivity, t.message, Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this@loginActivity, "Email or password is empty", Toast.LENGTH_LONG).show()
            }
        }
    }
}

