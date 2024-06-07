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
     * Validates if the password meets the requirements:
     * at least 6 characters long, contains at least one uppercase letter and one digit.
     *
     * @param password The password to be validated.
     * @return True if the password is valid, false otherwise.
     */
    fun isValidPassword(password: String): Boolean {
        val containsUpperCase = password.any { it.isUpperCase() }
        val containsDigit = password.any { it.isDigit() }
        return password.length >= 6 && containsUpperCase && containsDigit
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
