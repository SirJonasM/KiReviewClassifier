package classes;

public class Review {
    String text;
    String evaluation;

    public Review(String text, String evaluation){
        this.text = text;
        this.evaluation = evaluation;

    }

    @Override
    public String toString() {
        return "'" + text + "', " + evaluation;
    }
}
