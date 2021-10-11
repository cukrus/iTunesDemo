package net.cukrus.itunesdemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class ApiResult<T extends Wrapper> {
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss.SSS")
    private Date started;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss.SSS")
    private Date ended;
    private long took;
    private List<T> result;
    private String error;
    private ApiResultStatus status;

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getEnded() {
        return ended;
    }

    public void setEnded(Date ended) {
        this.ended = ended;
    }

    public long getTook() {
        return took;
    }

    public void setTook(long took) {
        this.took = took;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ApiResultStatus getStatus() {
        return status;
    }

    public void setStatus(ApiResultStatus status) {
        this.status = status;
    }
}
