package com.parabolagames.glassmaze.classic

import com.badlogic.gdx.scenes.scene2d.Stage
import com.google.common.base.Preconditions.checkState
import com.google.common.eventbus.EventBus
import com.parabolagames.glassmaze.framework.DialogController
import com.parabolagames.glassmaze.framework.ForGame
import com.parabolagames.glassmaze.shared.ApprovalDialog
import com.parabolagames.glassmaze.shared.Assets
import com.parabolagames.glassmaze.shared.IAddsController
import com.parabolagames.glassmaze.shared.InformationDialog
import com.parabolagames.glassmaze.shared.event.EventOpenStoreDialog
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@ForGame
internal class ClassicModeScreenDialogController
@Inject constructor(
        private val classicModeGameController: ClassicModeGameController,
        private val subMode: ClassicModeSubMode,
        private val game: IGlassMazeClassic,
        private val assets: Assets,
        private val levelNumberProvider: ILevelNumberProvider?,
        @param:Named(ClassicModeModule2.CLASSIC_DIALOG_UI_STAGE) private val dialogUiStage: Stage,
        private val gameExplanationDialogFactory: GameExplanationDialog.GameExplanationDialogFactory,
        private val candyGainDialogProvider: Provider<CandyGainDialog>,
        private val addsController: IAddsController,
        private val allLevelsCompletedDialogFactory: AllLevelsCompletedDialog.AllLevelsCompletedDialogFactory,
        @param:Named(ClassicModeModule2.CLASSIC_RANDOM_PLAY_LEVEL_LIMIT) private val levelLimitForRandomLevel: Int,
        private val eventBus: EventBus) {

    val isDialogOpen
        get() = DialogController.isDialogOpen

    /**
     * Shows the explanation dialog in levels 1,2,3
     *
     * @param closeAction this action is ran whether the the dialog is shown or not
     */
    fun showExplanationDialog(closeAction: () -> Unit) {
        checkState(levelNumberProvider != null, "cannot be called while levelNumberProvider is null")
        if (levelNumberProvider!!.levelNumber <= 3) {
            gameExplanationDialogFactory.create(closeAction, levelNumberProvider.levelNumber).show(dialogUiStage)
        } else {
            closeAction()
        }
    }

    fun showQuitModeDialog() {
        ApprovalDialog(
                assets,
                if (subMode == ClassicModeSubMode.CLASSIC) "Quit Classic Mode ?" else "Quit Random Mode ?",
                {
                    classicModeGameController.doBeforeQuitJobs()
                    game.setMenuScreenReVisible()
                },
                null,
                addsController).show(dialogUiStage)
    }

    fun showReplayDialog() {
        ApprovalDialog(assets,
                "Replay level-${levelNumberProvider!!.levelNumber} ?",
                {
                    classicModeGameController.doBeforeQuitJobs()
                    when (subMode) {
                        ClassicModeSubMode.CLASSIC -> game.setClassicModeScreenVisibleForLevelClassicMode(levelNumberProvider.levelNumber)
                        ClassicModeSubMode.RANDOM -> game.setClassicModeScreenVisibleForLevelRandomMode(levelNumberProvider.levelNumber)
                        else -> throw IllegalStateException();
                    }
                },
                null,
                addsController,
                lateButtons = true).show(dialogUiStage)
    }

    fun showCandyGainDialog() {
        candyGainDialogProvider.get().show(dialogUiStage)
    }


    fun showHintDialog() {
        HintDialog(
                assets,
                levelNumberProvider!!.candyGlassCountToManualBreak,
                levelNumberProvider.spinyGlassCountToManualBreak,
                addsController).show(dialogUiStage)
    }

    fun showAllLevelsCompletedDialog() {
        allLevelsCompletedDialogFactory.create().show(dialogUiStage)
    }

    fun showRandomModeNotReadyDialog() {
        InformationDialog(assets,
                """To continue, please finish
                    |${levelLimitForRandomLevel} level in "Classic Maze"""".trimMargin()) { game.setMenuScreenReVisible() }
                .also { it.show(dialogUiStage) }
    }

    fun showHandCountIsZeroAndUserPressedToHandSpinyOrCandyDialog() {
        GoToStoreApprovalDialog(assets) { eventBus.post(EventOpenStoreDialog()) }.show(dialogUiStage)
    }
}