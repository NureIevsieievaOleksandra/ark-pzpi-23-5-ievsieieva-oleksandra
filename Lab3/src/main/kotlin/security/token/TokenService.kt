package ua.nure.security.token

interface TokenService {
    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}