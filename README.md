<!-- prettier-ignore -->
<div align="center">

# PathLens

*A JavaFX desktop app for visualizing weighted-maze pathfinding algorithms.*

![Java](https://img.shields.io/badge/Java-11-ed8b00?style=flat-square&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-13-2563eb?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-build-c71a36?style=flat-square&logo=apachemaven&logoColor=white)
![Algorithms](https://img.shields.io/badge/Algorithms-Dijkstra%20%7C%20A*%20%7C%20GA-0f766e?style=flat-square)

[Overview](#overview) • [Features](#features) • [Get started](#get-started) • [Usage](#usage) • [Maze format](#maze-format) • [Project structure](#project-structure)

</div>

PathLens is a JavaFX desktop application for exploring how different pathfinding algorithms behave on weighted mazes. It loads bundled maze files, draws the grid, highlights the returned path, and reports total path cost, explored steps, and execution time.

The project currently compares three approaches: Bidirectional Dijkstra, A* with Manhattan-distance heuristic, and a configurable Genetic Algorithm.

> [!NOTE]
> The repository is still named `MazeVisualizer`, but **PathLens** is a shorter project name that better fits the app's purpose: inspecting pathfinding behavior visually.

## Overview

```text
Maze files
  Weighted grid maps with walls, start cells, and goal cells

JavaFX UI
  Maze canvas, control panel, algorithm selector, statistics, zoom, and pan

Algorithm layer
  Shared strategy interface with Dijkstra, A*, and Genetic Algorithm implementations
```

PathLens is useful for learning, demos, and quick visual comparisons between deterministic shortest-path methods and an evolutionary search approach.

## Features

- **Weighted maze rendering** with walls, start points, goals, and path highlighting.
- **13 bundled mazes** from `15x15` up to `100x100`.
- **Algorithm selection** through the JavaFX control panel.
- **Bidirectional Dijkstra** search from both start and goal.
- **A*** search with a Manhattan-distance heuristic and no diagonal movement.
- **Genetic Algorithm** with editable population size, mutation rate, crossover rate, maximum generations, and maximum moves.
- **Runtime statistics** for execution time, explored steps, and total path cost.
- **Interactive viewport** with mouse-wheel zoom and drag panning.

> [!IMPORTANT]
> The Genetic Algorithm is heuristic and stochastic. It can be useful for comparison and experimentation, but it is not guaranteed to return the optimal path the way Dijkstra-style shortest-path search is expected to.

## Get started

### Prerequisites

- JDK 11
- Apache Maven

Check your environment:

```bash
java -version
mvn -version
```

### Run the app

From the repository root:

```bash
mvn clean javafx:run
```

The JavaFX Maven plugin launches the app through `cpe231.App`.

### Build the project

```bash
mvn clean package
```

The compiled output is written to `target/`.

## Usage

1. Open the app with `mvn clean javafx:run`.
2. Select a maze from **Select Maze**.
3. Select an algorithm from **Select Algorithm**.
4. If using **Genetic Algorithm**, tune the displayed parameters or keep the defaults.
5. Click **Start Solving**.
6. Review the highlighted path and the reported time, step count, and distance.

> [!TIP]
> Use the mouse wheel to zoom into large mazes. Drag the maze viewport to pan around the grid.

## Algorithms

| Algorithm | Role |
| --- | --- |
| Bidirectional Dijkstra | Searches from both the start and goal to find a weighted shortest path. |
| A* (Manhattan) | Uses Manhattan distance as a heuristic for grid movement without diagonals. |
| Genetic Algorithm | Evolves candidate paths using configurable population, mutation, crossover, generation, and move limits. |

## Maze format

Bundled mazes live in [`src/main/resources/cpe231/maze/`](./src/main/resources/cpe231/maze/). The parser reads each maze line by line and supports walls, start cells, goal cells, and integer movement costs.

| Token | Meaning |
| --- | --- |
| `#` | Wall; cannot be traversed. |
| `S` | Start cell with cost `0`. |
| `G` | Goal cell with cost `0`. |
| Integer | Traversable cell whose value is its movement cost. |

Example:

```text
S123
1##4
111G
```

## Available mazes

| Range | Details |
| --- | --- |
| Small | `15x15`, `24x20`, `30x30`, `33x35` |
| Medium | `40x40`, `40x45`, `45x45`, `50x50`, `60x60` |
| Large | `70x60`, `80x50`, `100x90`, `100x100` |

## Project structure

```text
.
├── pom.xml
└── src/main
    ├── java/cpe231
    │   ├── App.java
    │   ├── algorithm/      # Pathfinding strategies and strategy context
    │   ├── components/     # Maze canvas and zoomable viewport
    │   ├── constants/      # Available algorithms and bundled mazes
    │   ├── helper/         # Result and node helper models
    │   ├── maze/           # Maze parser and block model
    │   ├── responsives/    # Responsive JavaFX sizing utilities
    │   └── ui/             # Main window, controls, and statistics
    └── resources/cpe231
        ├── fonts/
        ├── image/
        ├── maze/
        └── styles/
```

## Testing

This repository does not currently include a Maven test suite. The `cpe231.testAlgorithm` class provides manual algorithm execution and warm-up code, but it is not wired as an automated test.

For manual verification:

```bash
mvn clean javafx:run
```

Then run each bundled algorithm against small, medium, and large mazes and compare the displayed path cost, step count, and execution time.
