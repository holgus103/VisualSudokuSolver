# VisualSudokuSolver

Visual Sudoku Solver is an app that is meant to recognize sudoku puzzles and solve them. 

<img src="https://upload.wikimedia.org/wikipedia/commons/3/32/OpenCV_Logo_with_text_svg_version.svg" width=50/> <img src="https://upload.wikimedia.org/wikipedia/commons/d/db/Android_robot_2014.svg" width=50/>  <img src="https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin-logo.svg" width=50/> <img src="https://upload.wikimedia.org/wikipedia/commons/1/18/ISO_C%2B%2B_Logo.svg" width=50/>

## Warning

The app is aimed to recognize and solve sudoku puzzles. For now it's performance is far from perfect, depending on photo quality an average of about 5 digits need to be corrected by hand.

## Implementation details

The apps front-end and most of its logic were implementing using Kotlin, yet more demanding tasks such as solving sudokus use the 
advantages of Android's NDK and were therefore written in C++. For recognition a KNN model was created using OpenCV and a self-made 
dataset. OpenCV was also used for preprocessing the image.
