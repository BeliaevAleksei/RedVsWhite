# RedVsWhite
  The game on the field 10x10. Done using Java FX.

  You can start the application by going to the appropriate folder "mixedStrategy" or "onlyStrategy" ".\out\artifacts\RedVsWhite_jar"

  This paper deals with the problem of developing strategies for a game with a non-fixed amount.

  Rules of the game:
  The playing field is divided into 100 cells (10 in each row). The first player occupies the top left cell, the second - the bottom right one. Players take turns making turns; starts the first player. The game consists of two stages. At the first stage, participants in the game take turns capturing free cells (in which no one has yet been). As soon as both players are at a deadlock (there are no free cells around), the second stage begins. At this stage, players can move to any cell in which there is no opponent. The game ends after 100 moves of the second stage. Each turn brings the player from 0 to 3 points. The player who scores more points by the end of the game becomes the winner.