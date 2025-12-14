package cpe231.constants;

import java.util.List;

public class MazeData {

    private final String name;
    private final String path;

    public MazeData(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public static final List<MazeData> allMazeData = List.of(
            new MazeData("Maze 1 : 15x15", "/cpe231/maze/m15_15.txt"),
            new MazeData("Maze 2 : 24x20", "/cpe231/maze/m24_20.txt"),
            new MazeData("Maze 3 : 30x30", "/cpe231/maze/m30_30.txt"),
            new MazeData("Maze 4 : 33x35", "/cpe231/maze/m33_35.txt"),
            new MazeData("Maze 5 : 40x40", "/cpe231/maze/m40_40.txt"),
            new MazeData("Maze 6 : 40x45", "/cpe231/maze/m40_45.txt"),
            new MazeData("Maze 7 : 45x45", "/cpe231/maze/m45_45.txt"),
            new MazeData("Maze 8 : 50x50", "/cpe231/maze/m50_50.txt"),
            new MazeData("Maze 9 : 60x60", "/cpe231/maze/m60_60.txt"),
            new MazeData("Maze 10 : 70x60", "/cpe231/maze/m70_60.txt"),
            new MazeData("Maze 11 : 80x50", "/cpe231/maze/m80_50.txt"),
            new MazeData("Maze 12 : 100x90", "/cpe231/maze/m100_90.txt"),
            new MazeData("Maze 13 : 100x100", "/cpe231/maze/m100_100.txt"));

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
