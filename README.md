# TripleTriad

Triple Triad is a mini card game released along Final Fantasy VIII where two players fight with cards on a 3X3 board.  
The goal for each player is to own the most cards at the end of the game by "flipping" the opponent's cards. Various rules can be enabled to make the game more or less complex.
## The Game

| <img src="https://image.ibb.co/i9wqnR/signup.png" width="200" />  | <img src="https://image.ibb.co/jyOvnR/loggedinmenu.png" width="200" /> | <img src="https://preview.ibb.co/btHwSR/settings.png" width="200" />
| :---: | :---:  | :---: |
| *Sign up form* | *Main menu* | *Settings*

| <img src="https://image.ibb.co/fTGRu6/2players_Choice.png" width="200" /> | <img src="https://preview.ibb.co/cuTjgm/2players.png" width="200" /> | <img src="https://image.ibb.co/kjNAMm/1player.png" width="200"/>
| :---: | :---: | :---: |
| *Choose a game/new opponent* | *PvP* | *Game against the AI*

This game is mainly based on the graphics of the original game.

**Demo** - https://triple-triad-online.herokuapp.com

### Controls

The game can be played with either a mouse or a keyboard.

* <kbd>↑</kbd> , <kbd>↓</kbd> , <kbd>←</kbd> , <kbd>→</kbd> - Move the cursor
* <kbd>Enter</kbd> - Validate
* <kbd>Esc</kbd> - Cancel

### Cards

A Triple Triad card is a card with four numbers on its top, left, right and bottom sides.  
Blue cards belong to the first player and red cards to the second one.
### Rules

 Basically, a player takes (flips) the opponent's card by playing a card with a higher number on the side facing the opponent's card.

However, more rules can be enabled in the settings:  
* **Open** - Each player can see the opponent's cards.
* **War** - When the number of an adjacent card matches, if the sum of all the numbers of the opponent's card is smaller, then it is flipped.
* **Same** - If the numbers of a card equal to the numbers of two or more adjacent cards of the opponent, they will be flipped.
* **Plus** - If a card is placed down that adds up to the same value on two or more adjacent cards of the opponent, those cards are flipped.
* **Combo** - All cards which were turned over by the Same or Plus rule can turn over surrounding opponent's cards if the former have a greater value.

## Development

### Stack

* [Java 8](https://www.java.com)
* [Play Framework](https://www.playframework.com/)(Akka, Ebean, Twirl...)
* HTML/CSS/Javascript (ES6)
* [jquery](https://jquery.com)
* [RequireJS](http://requirejs.org)
* [Handlebars](http://handlebarsjs.com)
* [npm](https://www.npmjs.com)
* [Gulp](https://gulpjs.com)
* [BEM](http://getbem.com)

### Key features

* **Online game** - two human players can play online
* **Saved game** - save automatically games to be resumed later
* **MVC architecture**
* **Responsive design** - adapts perfectly to the screen
* **Multi-languages** - English and French are currently supported

## Installation

* Clone the repository
```
git clone https://github.com/J-GG/TripleTriadOnline.git
```

* Start the application with play

<br />


* For development purpose, install the dependencies with npm and run the watcher to update the css file
```
npm install
gulp dev
```