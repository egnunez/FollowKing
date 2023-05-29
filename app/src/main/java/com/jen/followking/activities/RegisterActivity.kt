package com.jen.followking.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jen.followking.databinding.ActivityRegisterBinding
import com.jen.followking.models.Client
import com.jen.followking.providers.AuthProvider
import com.jen.followking.providers.ClientProvider
import com.jen.followking.activities.MapActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authProvider = AuthProvider()
    private val clientProvider = ClientProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.btnGoToLogin.setOnClickListener{ goToLogin() }
        binding.btnRegister.setOnClickListener{ register()}
    }

    private fun register(){
        val name = binding.textfieldName.text.toString()
        val lastname = binding.textfieldApellido.text.toString()
        val email = binding.textfieldEmail.text.toString()
        val phone = binding.textfieldPhone.text.toString()
        val password = binding.textfieldPassword.text.toString()
        val confirmPassword = binding.textfieldCPassword.text.toString()

        if(isValidForm(name, lastname, email, phone, password, confirmPassword))

        authProvider.register(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    //Toast.makeText(this@RegisterActivity,"Registro exitoso",Toast.LENGTH_LONG).show()
                    val client = Client(
                        id = authProvider.getId(),
                        name = name,
                        lastname = lastname,
                        phone = phone,
                        email = email
                    )
                    clientProvider.create(client).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this@RegisterActivity, "Registro OK",Toast.LENGTH_SHORT).show()
                            goToMap()
                        }
                        else{

                            Toast.makeText(this@RegisterActivity, "Registro NO OK ${it.exception.toString()}",Toast.LENGTH_SHORT).show()
                            Log.d("Firebase", "Error: ${it.exception.toString()}")
                        }
                    }
                }
                else{
                    Toast.makeText(this@RegisterActivity,"Registro fallido ${it.exception.toString()}",Toast.LENGTH_LONG).show()
                    Log.d("Firebase", "Error: ${it.exception.toString()}")

                }
        }

            }

    private fun goToMap(){
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun isValidForm(
        name: String,
        lastname: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String): Boolean {

        if(name.isEmpty()){
            Toast.makeText(this,"Debes ingresar tu nombre",Toast.LENGTH_SHORT).show()
            return false
        }
        if(lastname.isEmpty()){
            Toast.makeText(this,"Debes ingresar tu apellido",Toast.LENGTH_SHORT).show()
            return false
        }
        if(email.isEmpty()){
            Toast.makeText(this,"Debes ingresar tu email",Toast.LENGTH_SHORT).show()
            return false
        }
        if(phone.isEmpty()){
            Toast.makeText(this,"Debes ingresar tu telefono",Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isEmpty()){
            Toast.makeText(this,"Debes ingresar tu password",Toast.LENGTH_SHORT).show()
            return false
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(this,"Debes confirmar tu password",Toast.LENGTH_SHORT).show()
            return false
        }
        if(password!=confirmPassword){
            Toast.makeText(this,"Las password deben coincidir",Toast.LENGTH_SHORT).show()
            return false

        }
        if(password.length < 8){
            Toast.makeText(this,"La password debe contener al menos 8 caracteres",Toast.LENGTH_SHORT).show()
            return false

        }
        return true
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

}