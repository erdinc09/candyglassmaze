package com.parabolagames.glassmaze.store.classicmaze

import com.parabolagames.glassmaze.shared.IClassicMazeCandyHandPersistenceManager
import com.parabolagames.glassmaze.shared.IClassicMazeSpinyHandPersistenceManager

interface IStorePersistenceManagerClassicMaze : IClassicMazeCandyHandPersistenceManager, IClassicMazeSpinyHandPersistenceManager {
    var totalScore: Int
    fun saveClassicMaze()
}