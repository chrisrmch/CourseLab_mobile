package org.courselab.app.org.courselab.app.ui.screens.log_in.dto

object Validator {
    fun validateEmail(email: String): Boolean {
        return email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
    }
    fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }
    fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
    fun validateName(name: String): Boolean {
        return name.isNotBlank()
    }
    fun validateLastName(lastName: String): Boolean {
        return lastName.isNotBlank()
    }
    fun validatePhone(phone: String): Boolean {
        return phone.matches(Regex("[0-9]+")) && phone.length == 10
    }
    fun validateAddress(address: String): Boolean {
        return address.isNotBlank()
    }
    fun validateCity(city: String): Boolean {
        return city.isNotBlank()
    }
    fun validateCountry(country: String): Boolean {
        return country.isNotBlank()
    }
    fun validatePostalCode(postalCode: String): Boolean {
        return postalCode.matches(Regex("[0-9]+")) && postalCode.length == 5
    }
}
