package br.com.learningwithme.learningwithme.modules.shared.api

enum class DocumentType {
    CPF {
        override fun validate(value: String): Boolean {
            val digits = value.filter { it.isDigit() }

            if (digits.length != 11) return false
            if (digits.all { it == digits[0] }) return false

            val numbers = digits.map { it.digitToInt() }

            val firstSum = (0..8).sumOf { (numbers[it] * (10 - it)) }
            val firstDigit = ((firstSum * 10) % 11).let { if (it == 10) 0 else it }

            if (firstDigit != numbers[9]) return false

            val secondSum = (0..9).sumOf { (numbers[it] * (11 - it)) }
            val secondDigit = ((secondSum * 10) % 11).let { if (it == 10) 0 else it }

            return secondDigit == numbers[10]
        }
    },
    CNPJ {
        override fun validate(value: String): Boolean {
            TODO("Not yet implemented")
        }
    }, ;

    abstract fun validate(value: String): Boolean
}
