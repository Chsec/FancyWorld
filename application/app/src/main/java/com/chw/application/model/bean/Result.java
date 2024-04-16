package com.chw.application.model.bean;

public class Result {

    /**
     * 执行结果，true成功，false失败
     */
    private boolean resultState;

    /**
     * 执行结果种类,执行成功默认为0
     */
    private int resultSort;

    /**
     * 执行结果文字提示
     */
    private String resultTip;

    private Result() {

    }

    public Result(boolean resultState, String resultTip) {
        this.resultState = resultState;
        this.resultSort = 0;
        this.resultTip = resultTip;
    }

    public Result(boolean resultState, int resultSort) {
        this.resultState = resultState;
        this.resultSort = resultSort;
        this.resultTip = "";
    }

    public Result(boolean resultState, int resultSort, String resultTip) {
        this.resultState = resultState;
        this.resultSort = resultSort;
        this.resultTip = resultTip;
    }

    public boolean isResultState() {
        return resultState;
    }

    public void setResultState(boolean resultState) {
        this.resultState = resultState;
    }

    public int getResultSort() {
        return resultSort;
    }

    public void setResultSort(int resultSort) {
        this.resultSort = resultSort;
    }

    public String getResultTip() {
        return resultTip;
    }

    public void setResultTip(String resultTip) {
        this.resultTip = resultTip;
    }
}
