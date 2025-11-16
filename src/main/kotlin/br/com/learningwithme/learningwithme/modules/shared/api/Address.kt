package br.com.learningwithme.learningwithme.modules.shared.api

data class Address(
    val street: String,
    val number: String,
    val complement: String?,
    val district: String,
    val city: String,
    val state: String,
    val zipCode: String,
) {
    init {
        require(street.isNotBlank()) { "Street cannot be blank." }
        require(number.isNotBlank()) { "Number cannot be blank." }
        require(zipCode.matches(Regex("\\d{5}-?\\d{3}"))) { "Invalid Zip Code format." }
    }

    fun formatted(): String = "$street, $number - $district, $city/$state - ZIP $zipCode"
}
