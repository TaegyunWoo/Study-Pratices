package practice.testCode;

public class Study {

    private StudyStatus studyStatus = StudyStatus.DRAFT;
    private int limit;
    private String name;

    public Study() {
    }

    public Study(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit은 0보다 작을 수 없다.");
        }
        this.limit = limit;
    }

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public StudyStatus getStudyStatus() {
        return this.studyStatus;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "Study{" +
                "studyStatus=" + studyStatus +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }
}
