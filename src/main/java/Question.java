public class Question {
    private String question;
    private String first;
    private String second;
    private String third;
    private String fourth;
    private String rightAns;

    public Question(){

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getFourth() {
        return fourth;
    }

    public void setFourth(String fourth) {
        this.fourth = fourth;
    }

    public String getRightAns() {
        return rightAns;
    }

    public void setRightAns(String rightAns) {
        this.rightAns = rightAns;
    }

    @Override
    public String toString() {
        return question;
    }
}
