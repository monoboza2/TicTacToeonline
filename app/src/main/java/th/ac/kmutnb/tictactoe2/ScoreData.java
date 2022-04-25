package th.ac.kmutnb.tictactoe2;

public class ScoreData {
    String name;
    Integer Score;

    public ScoreData() {
    }

    public ScoreData(String name, Integer score) {
        this.name = name;
        this.Score = score;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return Score;
    }

    public void setScore(Integer score) {
        this.Score = score;
    }
}
