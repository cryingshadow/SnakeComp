SnakeComp is an AI competition based on the popular snake game. By implementing 
the SnakeControl interface (deciding in each turn in which direction a snake 
moves), participants can submit their snake control and let several snakes 
compete with each other.

Besides being fun, this project can be used to learn programming with Java or to facilitate small programming 
competitions.

*Getting started*
The project can be built using gradle creating an executable jar file. But you can also start it from the main class 
(control.Main) directly.

There are already some simple snake controls in the package control.samples. You can take these as a starting point how 
to implement your own snake control. To integrate your snake control into the competition, you need to implement the 
control.SnakeControl interface and make your class part of the control.samples package. Please keep all code needed for 
your snake control (except imports of course) within one file. You can either select a folder in the UI where your 
snake control file is located or you can specify that folder as a parameter when executing the jar file or the main 
class.

The UI will show you the maze where the snakes will move and some buttons and sliders to configure the settings of the 
competition. You can specify the size of the maze, the number of walls, whether the maze should have a border of walls 
(then it is called an arena), the zoom level, how many pieces of food per snake will be in the maze, how long the 
snakes are initially, how fast the snakes move, how long snakes survive without eating, and whether snakes re-appear 
after they die. On clicking the start button, the competition will start. You can either wait until the competition is 
finished or you can abort the competition by clicking the abort button. Moreover, the UI shows some status information 
about the snakes (their names, their current length, whether they are alive, and the maximum length they ever reached 
if snakes re-appear after they die).

*Rules*
In each turn, each snake moves one field into some direction (UP, LEFT, DOWN, or RIGHT). If a snake moves on a field 
with food, its length increases by one. If a snake moves on a field with a wall or another snake part (possibly from 
itself), it crashes and dies. Moreover, if a snake does not move on a field with food for too many turns, it dies of 
hunger.

The snake control needs to decide on a given maze and position of the snake's head in which direction to move next. 
This decision has to be computed within 200 milliseconds. If a snake control takes too long to compute the next 
direction, the next direction is always set to UP and the snake dies if its control took too long for three times (not 
necessarily in a row).

*How to contribute*
Pull requests are always welcome.

