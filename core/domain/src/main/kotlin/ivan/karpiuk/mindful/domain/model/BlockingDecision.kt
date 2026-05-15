package ivan.karpiuk.mindful.domain.model

sealed interface BlockingDecision {
    data object Allow : BlockingDecision

    data class Block(
        val reason: BlockReason,
    ) : BlockingDecision
}
