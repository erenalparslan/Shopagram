package com.example.shopagram.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation{
    if (email.isEmpty())
        return RegisterValidation.Failed("E-mail boş olamaz !")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Yanlış e-mail formatı !")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if (password.isEmpty())
        return RegisterValidation.Failed("Parola boş olamaz !")

    if (password.length < 6)
        return RegisterValidation.Failed("Parola 6 karakter içermelilir !")

    return RegisterValidation.Success
}