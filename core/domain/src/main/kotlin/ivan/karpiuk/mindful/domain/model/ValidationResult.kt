package ivan.karpiuk.mindful.domain.model

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null,
)
