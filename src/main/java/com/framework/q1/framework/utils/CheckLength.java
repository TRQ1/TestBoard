package com.framework.q1.framework.utils;

public class CheckLength {

    public String checkString(String checkTitle) {
        int titleLen = checkTitle.length();
        String title = null;

        if (titleLen > 200) { // 200 이상인 경우 titlelen 숫자에서 - 10 을 한후 뒤에 ... 을 붙인다.
            title = checkTitle.substring(0, titleLen - 10) + "...";
        } else {
            title = checkTitle;
        } return title;
    }
}
