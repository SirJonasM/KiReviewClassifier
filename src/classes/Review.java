package classes;

public class Review {
    String text;
    String evaluation;

    public Review(String text, String evaluation){
        this.text = text;
        this.evaluation = evaluation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "'" + text + "', " + evaluation;
    }
}
