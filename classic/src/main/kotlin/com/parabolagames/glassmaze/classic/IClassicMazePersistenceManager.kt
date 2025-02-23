package com.parabolagames.glassmaze.classic

import com.parabolagames.glassmaze.shared.IClassicMazeCandyHandPersistenceManager
import com.parabolagames.glassmaze.shared.IClassicMazeSpinyHandPersistenceManager

interface IClassicMazePersistenceManager : ILastLevelNumberProvider, IClassicMazeCandyHandPersistenceManager, IClassicMazeSpinyHandPersistenceManager {
    var totalScore: Int
    override var levelNumber: Int
    fun saveClassicMaze()
}