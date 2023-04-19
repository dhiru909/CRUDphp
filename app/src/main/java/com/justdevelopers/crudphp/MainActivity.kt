package com.justdevelopers.crudphp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.justdevelopers.crudphp.databinding.ActivityMainBinding
import org.json.JSONObject




class MainActivity : AppCompatActivity(),  View.OnClickListener {
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val btnRegister = binding?.btnRegister
        btnRegister?.setOnClickListener(this)
    }
    private fun registerUser(){
        binding?.etAlreadyRegistered?.visibility = View.INVISIBLE
        val email= binding?.etEmail?.text.toString().trim()
        val userName = binding?.etUserName?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()

        val url = Constants.URL_REGISTER

// Request a string response from the provided URL.
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    when {
                        jsonObject.getString("message")=="User already exists, please login" -> {
                            binding?.etAlreadyRegistered?.visibility = View.VISIBLE
                            Toast.makeText(this@MainActivity, "User already exists, please login", Toast.LENGTH_SHORT).show()
                        }
                        jsonObject.getString("message")=="user registered successfully" -> {
                            Toast.makeText(this@MainActivity, "user registered successfully", Toast.LENGTH_SHORT).show()
                        }
                        jsonObject.getString("message")=="Some error occurred" -> {
                            Toast.makeText(this@MainActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Toast.makeText(this@MainActivity, "Data Inserted..", Toast.LENGTH_SHORT).show()
                }, Response.ErrorListener { error -> // displaying toast message on response failure.
                Log.e("tag", "error is " + error!!.toString())
                Toast.makeText(this@MainActivity, "Fail to insert data..", Toast.LENGTH_SHORT)
                    .show()
            })
            {
                override fun getParams(): MutableMap<String, String> {

                    // below line we are creating a map for storing
                    // our values in key and value pair.
                    val params: MutableMap<String, String> = HashMap()

                    // on below line we are passing our key
                    // and value pair to our parameters.
                    params["username"] = userName
                    params["email"] = email
                    params["password"] = password

                    // at last we are
                    // returning our params.
                    return params
                }
            }
        RequestHandler.getInstance(this).addToRequestQueue(request)
        // Add the request to the RequestQueue.
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnRegister ->{
                registerUser()
            }

        }
    }

}