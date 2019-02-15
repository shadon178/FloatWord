package com.hcf.floatword;

public class Word {

    private String english;

    private String soundmark;

    private String chinese;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getSoundmark() {
        return soundmark;
    }

    public void setSoundmark(String soundmark) {
        this.soundmark = soundmark;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return "Word{" +
                "english='" + english + '\'' +
                ", soundmark='" + soundmark + '\'' +
                ", chinese='" + chinese + '\'' +
                '}';
    }
}
