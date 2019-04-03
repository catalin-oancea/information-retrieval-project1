package CoreLogic;

public class Result {
    private String filePath;
    private Float score;

    Result(String filePath, Float score) {
        this.filePath = filePath;
        this.score = score;
    }

    public String getFilePath() { 
        return this.filePath;
    }

    public Float getScore() {
        return this.score;
    }
}