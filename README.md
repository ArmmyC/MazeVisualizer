# Pathfinding Algorithm Visualizer

A JavaFX desktop application for visualizing and comparing pathfinding algorithms on weighted mazes.

The application renders bundled maze files, highlights the path returned by the selected algorithm, and reports its total cost, explored steps, and execution time.

## Features

- Visualizes weighted mazes with walls, start points, and goals.
- Includes 13 maze sizes, ranging from 15x15 to 100x100.
- Supports three pathfinding approaches:
  - Bidirectional Dijkstra
  - A* with Manhattan-distance heuristic
  - Genetic Algorithm
- Lets you configure population size, mutation rate, crossover rate, maximum generations, and maximum moves for the Genetic Algorithm.
- Displays path cost, explored steps, and execution time in nanoseconds.
- Supports mouse-wheel zooming and click-and-drag panning.

## Tech Stack

- Java 11
- JavaFX 13 (`javafx-controls` and `javafx-fxml`)
- Apache Maven

## Getting Started

### Prerequisites

- JDK 11
- Apache Maven

Confirm that both tools are available:

```bash
java -version
mvn -version
```

### Run the Application

From the repository root, run:

```bash
mvn clean javafx:run
```

The Maven JavaFX plugin starts the application through `cpe231.App`.

## Usage

1. Select a maze from the **Select Maze** menu.
2. Select a pathfinding algorithm from the **Select Algorithm** menu.
3. When using the Genetic Algorithm, adjust its parameters or keep the displayed defaults.
4. Select **Start Solving**.
5. Review the highlighted path and the reported time, step count, and distance (total path cost).

Use the mouse wheel over the maze to zoom. Drag the maze viewport to pan around larger mazes.

## Available Commands

| Command | Description |
|---|---|
| `mvn clean javafx:run` | Cleans the project and launches the JavaFX application. |
| `mvn clean package` | Compiles the project and creates the Maven build output in `target/`. |

## Maze Format

Bundled mazes are stored in `src/main/resources/cpe231/maze/`. The parser recognizes:

| Token | Meaning |
|---|---|
| `#` | Wall; cannot be traversed. |
| `S` | Start cell with cost `0`. |
| `G` | Goal cell with cost `0`. |
| Integer | Traversable cell whose value is its movement cost. |

## Project Structure

```text
.
|-- pom.xml
`-- src/main
    |-- java
    |   |-- cpe231
    |   |   |-- algorithm/    # Pathfinding strategies and strategy context
    |   |   |-- components/   # Maze canvas and zoomable viewport
    |   |   |-- constants/    # Available algorithms and bundled mazes
    |   |   |-- helper/       # Algorithm result and A* node models
    |   |   |-- maze/         # Maze parser and block model
    |   |   |-- responsives/  # Responsive JavaFX sizing utilities
    |   |   `-- ui/           # Main window, controls, and statistics
    |   `-- module-info.java
    `-- resources/cpe231
        |-- fonts/
        |-- image/
        |-- maze/
        `-- styles/
```

## Testing

The repository does not currently contain an automated test suite or test framework configuration. The `cpe231.testAlgorithm` class provides manual algorithm execution and warm-up code, but it is not a Maven test.
