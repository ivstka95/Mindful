package ivan.karpiuk.mindful.domain.model

sealed interface BlockingCommand {
    data class ShowOverlay(
        val packageName: String,
        val reason: BlockReason,
    ) : BlockingCommand

    data object DoNothing : BlockingCommand

    data object DismissOverlay : BlockingCommand
}
