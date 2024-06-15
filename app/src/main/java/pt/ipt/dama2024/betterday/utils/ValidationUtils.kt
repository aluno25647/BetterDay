package pt.ipt.dama2024.betterday.utils

/**
 * Utility object for validation functions.
 */
object ValidationUtils {

    /**
     * Validates if the username is at least 5 characters long.
     *
     * @param username The username to be validated.
     * @return True if the username is valid, false otherwise.
     */
    fun isValidUsername(username: String): Boolean {
        return username.length >= 5
    }

    /**
     * Validates if the password meets the minimum length requirement.
     *
     * @param password The password to be validated.
     * @return True if the password meets the minimum length requirement, false otherwise.
     */
    fun isPasswordLengthValid(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Validates if the password contains at least one uppercase letter and one digit.
     *
     * @param password The password to be validated.
     * @return True if the password contains at least one uppercase letter and one digit, false otherwise.
     */
    fun isPasswordComplexityValid(password: String): Boolean {
        val containsUpperCase = password.any { it.isUpperCase() }
        val containsDigit = password.any { it.isDigit() }
        return containsUpperCase && containsDigit
    }

    /**
     * Validates if the email is in a proper format.
     *
     * @param email The email to be validated.
     * @return True if the email is valid, false otherwise.
     */
    fun isValidEmail(email: String): Boolean {
        // Regular expression for basic email validation
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
        return email.matches(emailPattern.toRegex())
    }
}
