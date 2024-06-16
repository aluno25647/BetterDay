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
        // Trim the username to remove leading and trailing spaces
        val trimmedUsername = username.trim()

        // Check if the trimmed username length is at least 5 characters
        return trimmedUsername.length >= 5
    }

    /**
     * Validates if the password meets the minimum length requirement and does not contain spaces
     *
     * @param password The password to be validated.
     * @return True if the password meets the minimum length requirement and does not contain spaces, false otherwise.
     */
    fun isPasswordLengthValid(password: String): Boolean {
        // Check if the password length is at least 6 characters and does not contain spaces
        return password.length >= 6 && !password.contains(" ")
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

}
